package com.cristian.teste.reservas.hoteis.producer;

import com.cristian.teste.reservas.hoteis.enums.TipoOperacaoReserva;
import com.cristian.teste.reservas.hoteis.exception.TipoOperacaoInvalidaException;
import com.cristian.teste.reservas.hoteis.utils.KafkaMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.cristian.teste.reservas.hoteis.enums.TipoOperacaoReserva.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservaProducer {

    @Value("${spring.kafka.topics.checkin}")
    private String topicoDeCheckin;

    @Value("${spring.kafka.topics.checkout}")
    private String topicoDeCheckout;

    @Value("${spring.kafka.topics.confirmacao-reserva}")
    private String topicoDeConfirmacao;

    @Value("${spring.kafka.topics.criacao-reserva}")
    private String topicoDeCriacao;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaMessageMapper kafkaMessageMapper;

    public void sendMessage(Object mensagem, TipoOperacaoReserva tipoOperacao) {
        String topico;

        if (CHECKIN.equals(tipoOperacao)) {
            topico = topicoDeCheckin;
        } else if (CHECKOUT.equals(tipoOperacao)) {
            topico = topicoDeCheckout;
        } else if(CONFIRMACAO.equals(tipoOperacao)) {
            topico = topicoDeConfirmacao;
        }  else if(CRIACAO.equals(tipoOperacao)) {
            topico = topicoDeCriacao;
        } else {
            throw new TipoOperacaoInvalidaException("Tipo de operação inválido");
        }

        kafkaTemplate.send(topico, kafkaMessageMapper.map(mensagem));
        log.info("Mensagem enviada para o tópico {} : {}", topico, mensagem);
    }
}
