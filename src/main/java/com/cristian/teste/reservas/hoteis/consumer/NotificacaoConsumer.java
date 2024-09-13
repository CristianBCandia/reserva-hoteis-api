package com.cristian.teste.reservas.hoteis.consumer;

import com.cristian.teste.reservas.hoteis.converter.NotificacaoConverter;
import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.model.Notificacao;
import com.cristian.teste.reservas.hoteis.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class NotificacaoConsumer {

    private final NotificacaoService notificacaoService;

    @KafkaListener(topics = "${KAFKA_TOPIC_NOTIFICACAO:notificacao}", groupId = "${KAFKA_GROUP_NOTIFICACAO:notificacao-group}", containerFactory = "notificacaoListenerContainerFactory")
    public void listen(NotificacaoDTO notificacao) {
        log.info("Consumindo mensagem de notificação: {}", notificacao);
        notificacaoService.enviarEmail(NotificacaoConverter.entidade(notificacao));
    }
}