package com.cristian.teste.reservas.hoteis.consumer;

import com.cristian.teste.reservas.hoteis.converter.ReservaConverter;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaConsumer {

    private final ReservaService reservaService;

    @KafkaListener(topics = "${KAFKA_TOPIC_CRIACAO_RESERVA:criacao-reserva}", groupId = "${KAFKA_GROUP_CRIACAO_RESERVA:criacao-reserva-group}", containerFactory = "reservaListenerContainerFactory")
    public void criacao(ReservaDTO reserva) {
        reservaService.criarReserva(ReservaConverter.entidade(reserva));
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_CONFIRMACAO_RESERVA:confirmacao-reserva}", groupId = "${KAFKA_GROUP_CONFIRMACAO_RESERVA:confirmacao-reserva-group}", containerFactory = "reservaListenerContainerFactory")
    public void confirmacao(ReservaDTO reserva) {
        reservaService.confirmarReserva(ReservaConverter.entidade(reserva));
    }

    @KafkaListener(topics = "${KAFKA_TOPIC_CHECKIN:checkin}", groupId = "${KAFKA_GROUP_CHECKIN:checkin-group}", containerFactory = "reservaListenerContainerFactory")
    public void checkin(ReservaDTO reserva) {
        reservaService.checkIn(ReservaConverter.entidade(reserva));
    }

    @KafkaListener(topics = "${KAFKA_GROUP_CHECKOUT:checkout}", groupId = "${KAFKA_GROUP_CHECKOUT:checkout-group}", containerFactory = "reservaListenerContainerFactory")
    public void checkout(ReservaDTO reserva) {
        reservaService.checkOut(ReservaConverter.entidade(reserva));
    }
}
