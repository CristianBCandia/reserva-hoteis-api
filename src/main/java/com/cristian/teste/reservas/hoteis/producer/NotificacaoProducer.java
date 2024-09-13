package com.cristian.teste.reservas.hoteis.producer;

import com.cristian.teste.reservas.hoteis.utils.KafkaMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificacaoProducer {

    @Value("${spring.kafka.topics.notificacao}")
    private String topicoDeNotificacoes;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaMessageMapper kafkaMessageMapper;

    public void sendMessage(Object mensagem) {
        kafkaTemplate.send(topicoDeNotificacoes, kafkaMessageMapper.map(mensagem));
        log.info("Mensagem enviada para o tópico {} : {}", topicoDeNotificacoes, mensagem);
    }
}
