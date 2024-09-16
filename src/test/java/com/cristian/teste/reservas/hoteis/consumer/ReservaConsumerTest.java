package com.cristian.teste.reservas.hoteis.consumer;

import com.cristian.teste.reservas.hoteis.config.TestMailConfig;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
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

@SpringBootTest
@EmbeddedKafka(
        controlledShutdown = true,
        topics = {"criacao-reserva", "confirmacao-reserva", "checkin", "checkout", "criacao-reserva-dlq", "confirmacao-reserva-dlq", "checkin-dlq", "checkout-dlq"},
        partitions = 1
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestMailConfig.class)
@ActiveProfiles("test")
public class ReservaConsumerTest {

    private static final String TOPIC_CRIACAO = "criacao-reserva";
    private static final String TOPIC_CONFIRMACAO = "confirmacao-reserva";
    private static final String TOPIC_CHECKIN = "checkin";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ReservaConsumer consumer;

    @Autowired
    private KafkaTemplate<String, ReservaDTO> kafkaTemplate;

    private static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    static {
        kafkaContainer.start();
    }

    @Test
    @DisplayName("deve consumir uma mensagem com sucesso no tópico de criação")
    void deveConsumirAMensagemComSucessoNoTopicoDeCriacao() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, TOPIC_CRIACAO);

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        kafkaTemplate.send(TOPIC_CRIACAO, reservaDTO);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> record.value().equals(reservaDTO));
    }


    @Test
    @DisplayName("deve consumir uma mensagem com sucesso no tópico de confirmação")
    void deveConsumirAMensagemComSucessoNoTopicoDeConfirmacao() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, TOPIC_CONFIRMACAO);

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        kafkaTemplate.send(TOPIC_CONFIRMACAO, reservaDTO);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> record.value().equals(reservaDTO));
    }

    @Test
    @DisplayName("deve consumir uma mensagem com sucesso no tópico de check-in")
    void deveConsumirAMensagemComSucessoNoTopicoDeCheckin() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.embeddedKafkaBroker.consumeFromEmbeddedTopics(consumer, TOPIC_CHECKIN);

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        kafkaTemplate.send(TOPIC_CHECKIN, reservaDTO);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> record.value().equals(reservaDTO));
    }


    private <V> Consumer<String, V> createConsumer(Class<V> classType) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", this.embeddedKafkaBroker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put("bootstrap.servers", this.embeddedKafkaBroker.getBrokersAsString());

        DefaultKafkaConsumerFactory<String, V> consumerFactory = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(classType, false)
        );

        return consumerFactory.createConsumer();
    }
}

