package com.cristian.teste.reservas.hoteis.consumer;

import com.cristian.teste.reservas.hoteis.converter.ReservaConverter;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.exception.ConsumerException;
import com.cristian.teste.reservas.hoteis.service.ReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import static com.cristian.teste.reservas.hoteis.constants.LogConstants.MENSAGEM_ERRO_DLQ;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaConsumer {

    private final ReservaService reservaService;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 3000),
            dltTopicSuffix = "-dlq",
            autoCreateTopics = "false",
            kafkaTemplate = "retryableTopicKafkaTemplate"
    )
    @KafkaListener(topics = "${KAFKA_TOPIC_CRIACAO_RESERVA:criacao-reserva}", groupId = "${KAFKA_GROUP_CRIACAO_RESERVA:criacao-reserva-group}", containerFactory = "containerFactory")
    public void criacao(ReservaDTO reserva) {
        try {
            reservaService.criarReserva(ReservaConverter.entidade(reserva));
        } catch (Exception e) {
            throw new ConsumerException("criacao-reserva", e);
        }
    }

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(delay = 3000),
            dltTopicSuffix = "-dlq",
            kafkaTemplate = "retryableTopicKafkaTemplate"
    )
    @KafkaListener(topics = "${KAFKA_TOPIC_CONFIRMACAO_RESERVA:confirmacao-reserva}", groupId = "${KAFKA_GROUP_CONFIRMACAO_RESERVA:confirmacao-reserva-group}", containerFactory = "containerFactory")
    public void confirmacao(ReservaDTO reserva) {
        try {
            reservaService.confirmarReserva(ReservaConverter.entidade(reserva));
        } catch (Exception e) {
            throw new ConsumerException("confirmacao-reserva", e);
        }
    }

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 3000),
            dltTopicSuffix = "-dlq",
            kafkaTemplate = "retryableTopicKafkaTemplate"
    )
    @KafkaListener(topics = "${KAFKA_TOPIC_CHECKIN:checkin}", groupId = "${KAFKA_GROUP_CHECKIN:checkin-group}", containerFactory = "containerFactory")
    public void checkin(ReservaDTO reserva) {
        try {
            reservaService.checkIn(ReservaConverter.entidade(reserva));
        } catch (Exception e) {
            throw new ConsumerException("checkin", e);
        }
    }

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 3000),
            dltTopicSuffix = "-dlq",
            kafkaTemplate = "retryableTopicKafkaTemplate"
    )
    @KafkaListener(topics = "${KAFKA_TOPIC_CHECKOUT:checkout}", groupId = "${KAFKA_GROUP_CHECKOUT:checkout-group}", containerFactory = "containerFactory")
    public void checkout(ReservaDTO reserva) {
        try {
            reservaService.checkOut(ReservaConverter.entidade(reserva));
        } catch (Exception e) {
            throw new ConsumerException("checkout", e);
        }
    }

    @DltHandler
    public void reservaHandler(
            ReservaDTO reserva, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error(MENSAGEM_ERRO_DLQ, topic, reserva);
    }
}
