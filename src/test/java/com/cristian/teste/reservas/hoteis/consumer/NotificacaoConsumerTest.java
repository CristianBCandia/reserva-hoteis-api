package com.cristian.teste.reservas.hoteis.consumer;

import com.cristian.teste.reservas.hoteis.config.TestMailConfig;
import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.exception.ConsumerException;
import com.cristian.teste.reservas.hoteis.service.NotificacaoService;
import com.cristian.teste.reservas.hoteis.utils.TestUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@EmbeddedKafka(
        controlledShutdown = true,
        topics = {"notificacao", "notificacao-dlq"},
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestMailConfig.class)
@ActiveProfiles("test")
public class NotificacaoConsumerTest {

    private static final String TOPIC = "notificacao";
    private static final String DLT_TOPIC = "notificacao-dlq";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private NotificacaoConsumer consumer;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private KafkaTemplate<String, NotificacaoDTO> kafkaTemplate;

    private static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    static {
        kafkaContainer.start();
    }

    @Test
    @DisplayName("deve consumir uma mensagem com sucesso")
    void shouldConsumeMessageSuccessfully() {
        Consumer<String, NotificacaoDTO> consumer = createConsumer(NotificacaoDTO.class);
        this.embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, TOPIC);

        NotificacaoDTO notificacaoDTO = TestUtils.notificacaoDTOMock();
        kafkaTemplate.send(TOPIC, notificacaoDTO);

        ConsumerRecords<String, NotificacaoDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> record.value().equals(notificacaoDTO));
    }

    @Test
    @DisplayName("deve enviar a mensagem para o tópico DLT em caso de erro")
    void shouldSendMessageToDLTOnFailure() {
        Consumer<String, NotificacaoDTO> consumer = createConsumer(NotificacaoDTO.class);
        this.embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, DLT_TOPIC);

        NotificacaoDTO notificacaoDTO = TestUtils.notificacaoDTOMock();
        notificacaoDTO.setEmailDestinatario("erro@teste.com");

        kafkaTemplate.send(TOPIC, notificacaoDTO);

        ConsumerRecords<String, NotificacaoDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));
        assertThat(records).hasSize(1)
                .allMatch(record -> record.value().equals(notificacaoDTO));
    }

    @Test
    @DisplayName("deve lançar ConsumerException em caso de falha de processamento")
    void shouldThrowExceptionOnProcessingFailure() {
        NotificacaoDTO notificacaoDTO = new NotificacaoDTO();

        assertThatThrownBy(() -> consumer.listen(notificacaoDTO))
                .isInstanceOf(ConsumerException.class)
                .hasMessageContaining("notificacao");
    }

    private <V> Consumer<String, V> createConsumer(Class<V> classType) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", this.embeddedKafkaBroker);
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