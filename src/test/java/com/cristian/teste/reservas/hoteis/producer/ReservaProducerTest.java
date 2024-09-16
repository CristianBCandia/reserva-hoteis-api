package com.cristian.teste.reservas.hoteis.producer;


import com.cristian.teste.reservas.hoteis.config.TestMailConfig;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.exception.TipoOperacaoInvalidaException;
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

import static com.cristian.teste.reservas.hoteis.enums.TipoOperacaoReserva.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@EmbeddedKafka(
        topics = {"checkin", "checkout", "confirmacao-reserva", "criacao-reserva"},
        partitions = 1
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Import(TestMailConfig.class)
@ActiveProfiles("test")
public class ReservaProducerTest {

    @Autowired
    private EmbeddedKafkaBroker broker;

    @Autowired
    private ReservaProducer producer;

    private static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.2.1"));

    static {
        kafkaContainer.start();  // Inicia o container antes de executar os testes
    }

    @Test
    @DisplayName("deve enviar uma mensagem para o tópico de checkin")
    void shouldSendMessageToCheckinTopic() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.broker.consumeFromEmbeddedTopics(consumer, "checkin");

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        producer.sendMessage(reservaDTO, CHECKIN);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> {
                    ReservaDTO value = record.value();
                    return value.getId().equals(reservaDTO.getId())
                            && value.getNomeHospede().equals(reservaDTO.getNomeHospede())
                            && value.getEmailHospede().equals(reservaDTO.getEmailHospede())
                            && value.getTelefoneHospede().equals(reservaDTO.getTelefoneHospede())
                            && value.getNumeroHospedes().equals(reservaDTO.getNumeroHospedes())
                            && value.getDataCheckIn().equals(reservaDTO.getDataCheckIn())
                            && value.getDataCheckout().equals(reservaDTO.getDataCheckout())
                            && value.getStatus().equals(reservaDTO.getStatus());
                });
    }

    @Test
    @DisplayName("deve enviar uma mensagem para o tópico de checkout")
    void shouldSendMessageToCheckoutTopic() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.broker.consumeFromEmbeddedTopics(consumer, "checkout");

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        producer.sendMessage(reservaDTO, CHECKOUT);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> {
                    ReservaDTO value = record.value();
                    return value.getId().equals(reservaDTO.getId())
                            && value.getNomeHospede().equals(reservaDTO.getNomeHospede())
                            && value.getEmailHospede().equals(reservaDTO.getEmailHospede())
                            && value.getTelefoneHospede().equals(reservaDTO.getTelefoneHospede())
                            && value.getNumeroHospedes().equals(reservaDTO.getNumeroHospedes())
                            && value.getDataCheckIn().equals(reservaDTO.getDataCheckIn())
                            && value.getDataCheckout().equals(reservaDTO.getDataCheckout())
                            && value.getStatus().equals(reservaDTO.getStatus());
                });
    }

    @Test
    @DisplayName("deve enviar uma mensagem para o tópico de confirmação")
    void shouldSendMessageToConfirmacaoTopic() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.broker.consumeFromEmbeddedTopics(consumer, "confirmacao-reserva");

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        producer.sendMessage(reservaDTO, CONFIRMACAO);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> {
                    ReservaDTO value = record.value();
                    return value.getId().equals(reservaDTO.getId())
                            && value.getNomeHospede().equals(reservaDTO.getNomeHospede())
                            && value.getEmailHospede().equals(reservaDTO.getEmailHospede())
                            && value.getTelefoneHospede().equals(reservaDTO.getTelefoneHospede())
                            && value.getNumeroHospedes().equals(reservaDTO.getNumeroHospedes())
                            && value.getDataCheckIn().equals(reservaDTO.getDataCheckIn())
                            && value.getDataCheckout().equals(reservaDTO.getDataCheckout())
                            && value.getStatus().equals(reservaDTO.getStatus());
                });
    }

    @Test
    @DisplayName("deve enviar uma mensagem para o tópico de criação")
    void shouldSendMessageToCriacaoTopic() {
        Consumer<String, ReservaDTO> consumer = createConsumer(ReservaDTO.class);
        this.broker.consumeFromEmbeddedTopics(consumer, "criacao-reserva");

        ReservaDTO reservaDTO = TestUtils.reservaDTOMock();
        producer.sendMessage(reservaDTO, CRIACAO);

        ConsumerRecords<String, ReservaDTO> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records).hasSize(1)
                .allMatch(record -> {
                    ReservaDTO value = record.value();
                    return value.getId().equals(reservaDTO.getId())
                            && value.getNomeHospede().equals(reservaDTO.getNomeHospede())
                            && value.getEmailHospede().equals(reservaDTO.getEmailHospede())
                            && value.getTelefoneHospede().equals(reservaDTO.getTelefoneHospede())
                            && value.getNumeroHospedes().equals(reservaDTO.getNumeroHospedes())
                            && value.getDataCheckIn().equals(reservaDTO.getDataCheckIn())
                            && value.getDataCheckout().equals(reservaDTO.getDataCheckout())
                            && value.getStatus().equals(reservaDTO.getStatus());
                });
    }

    @Test
    @DisplayName("deve lançar uma exceção para tipo de operação inválido")
    void shouldThrowExceptionForInvalidOperationType() {
        assertThatThrownBy(() -> producer.sendMessage(TestUtils.reservaDTOMock(), null))
                .isInstanceOf(TipoOperacaoInvalidaException.class)
                .hasMessage("Tipo de operação inválido");
    }

    private <V> Consumer<String, V> createConsumer(Class<V> classType) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", this.broker);
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


