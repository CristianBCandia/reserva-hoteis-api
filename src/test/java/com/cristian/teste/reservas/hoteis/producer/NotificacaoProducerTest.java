package com.cristian.teste.reservas.hoteis.producer;

import com.cristian.teste.reservas.hoteis.config.TestMailConfig;
import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.exception.NotificacaoNaoEnviadaException;
import com.cristian.teste.reservas.hoteis.utils.TestUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@EmbeddedKafka(
        topics = "notificacao",
        partitions = 1
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestMailConfig.class)
@ActiveProfiles("test")
public class NotificacaoProducerTest {

    private static final String TOPIC = "notificacao";

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private NotificacaoProducer producer;

    private KafkaConsumer<String, String> consumer;

    private static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    static {
        kafkaContainer.start();  // Inicia o container antes de executar os testes
    }

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(producer, "topicoDeNotificacoes", TOPIC);
    }

    @Test
    @DisplayName("deve consumir uma mensagem na fila")
    void t1() {
        Consumer<String, NotificacaoDTO> consumer = createConsumer(NotificacaoDTO.class);
        this.broker.consumeFromEmbeddedTopics(consumer, "notificacao");

        producer.sendMessage(TestUtils.notificacaoDTOMock());

        ConsumerRecords<String, NotificacaoDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> {
                    NotificacaoDTO notificacao = record.value();
                    NotificacaoDTO expected = TestUtils.notificacaoDTOMock();

                    return notificacao.getId().equals(expected.getId())
                            && notificacao.getEmailDestinatario().equals(expected.getEmailDestinatario())
                            && notificacao.getMensagem().equals(expected.getMensagem())
                            && notificacao.getTipoNotificacao().equals(expected.getTipoNotificacao());
                });

    }

    @Test
    @DisplayName("deve falhar ao tentar enviar uma mensagem devido a uma falha na configuração do Kafka")
    void shouldFailToSendMessage() {
        ReflectionTestUtils.setField(producer, "topicoDeNotificacoes", "tópico-inexistente");

        assertThatThrownBy(() -> producer.sendMessage(TestUtils.notificacaoDTOMock()))
                .isInstanceOf(NotificacaoNaoEnviadaException.class)
                .hasMessage("Falha ao enviar notificação para fila");
    }


    private <V> Consumer<String, V> createConsumer(Class<V> classType) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("notificacao", "true", this.broker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        DefaultKafkaConsumerFactory<String, V> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(classType, false)
        );

        return consumerFactory.createConsumer();
    }
}