package com.cristian.teste.reservas.hoteis.consumer;

import com.cristian.teste.reservas.hoteis.converter.NotificacaoConverter;
import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.exception.ConsumerException;
import com.cristian.teste.reservas.hoteis.service.NotificacaoService;
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
@Slf4j
@RequiredArgsConstructor
public class NotificacaoConsumer {

    private final NotificacaoService notificacaoService;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 3000),
            dltTopicSuffix = "-dlq",
            autoCreateTopics = "false",
            kafkaTemplate = "defaultRetryTopicKafkaTemplate"
    )
    @KafkaListener(topics = "${KAFKA_TOPIC_NOTIFICACAO:notificacao}", groupId = "${KAFKA_GROUP_NOTIFICACAO:notificacao-group}", containerFactory = "notificacaoListenerContainerFactory")
    public void listen(NotificacaoDTO notificacao) {
        log.info("Consumindo mensagem de notificação: {}", notificacao);
        try {
            notificacaoService.enviarEmail(NotificacaoConverter.entidade(notificacao));
        } catch (Exception e) {
            throw new ConsumerException("notificacao", e);
        }
    }

    @DltHandler
    public void notificacaoHandler(
            NotificacaoDTO reserva, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.error(MENSAGEM_ERRO_DLQ, topic, reserva);
    }
}