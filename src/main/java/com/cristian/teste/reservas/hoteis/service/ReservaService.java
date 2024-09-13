package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.converter.ReservaConverter;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;
import com.cristian.teste.reservas.hoteis.exception.ReservaIndisponivelException;
import com.cristian.teste.reservas.hoteis.model.Reserva;
import com.cristian.teste.reservas.hoteis.producer.ReservaProducer;
import com.cristian.teste.reservas.hoteis.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.cristian.teste.reservas.hoteis.enums.TipoOperacaoReserva.*;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final ReservaProducer reservaProducer;
    private final NotificacaoService notificacaoService;

    public void processaCriacaoReserva(ReservaDTO reserva) {
        verificaSeReservaEstaDisponivel(reserva);
        reservaProducer.sendMessage(reserva, CRIACAO);
    }

    public void processaConfirmacaoReserva(Long id) {
        reservaProducer.sendMessage(buscaReservaPorId(id), CONFIRMACAO);
    }

    public void processaCheckIn(Long id){
        reservaProducer.sendMessage(buscaReservaPorId(id), CHECKIN);
    }

    public void processaCheckOut(Long id) {
        reservaProducer.sendMessage(buscaReservaPorId(id), CHECKOUT);
    }

    public Reserva criarReserva(Reserva reserva) {
        reserva.setStatus(StatusReserva.PENDENTE);
        reservaRepository.save(reserva);
        notificacaoService.criarNotificacao(reserva, TipoNotificacao.CRIACAO_RESERVA);
        return reserva;
    }

    @Transactional
    public void confirmarReserva(Reserva reserva) {
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reservaRepository.save(reserva);
        notificacaoService.criarNotificacao(reserva, TipoNotificacao.CONFIRMACAO_RESERVA);
    }

    @Transactional
    public void checkIn(Reserva reserva) {
        if (reserva.getStatus() == StatusReserva.CONFIRMADA &&
                reserva.getDataCheckIn().isBefore(LocalDate.now())) {
            reserva.setStatus(StatusReserva.CHECKED_IN);
            reservaRepository.save(reserva);
            notificacaoService.criarNotificacao(reserva, TipoNotificacao.CHECK_IN);
        }
    }

    @Transactional
    public void checkOut(Reserva reserva) {
        if (reserva.getStatus() == StatusReserva.CHECKED_IN &&
                reserva.getDataCheckOut().isBefore(LocalDate.now())) {
            reserva.setStatus(StatusReserva.CHECKED_OUT);
            reservaRepository.save(reserva);
            notificacaoService.criarNotificacao(reserva, TipoNotificacao.CHECK_OUT);
        }
    }

    public ReservaDTO buscaReservaPorId(Long id) {
        logger.info("Buscando reserva na base de dados");
        return ReservaConverter.dto(reservaRepository.findById(id)
                .orElseThrow(()-> {
                    logger.info("Reserva de id {}, não encontrada!", id);
                    return new RuntimeException("Reserva não encontrada!");
                }));
    }

    public void verificaSeReservaEstaDisponivel(ReservaDTO reserva) {
        if(reservaRepository.buscaPorReservasNoMesmoHotelQuartoEPeriodo(
                reserva.getHotel().id(),
                reserva.getQuarto().id(),
                reserva.getDataCheckIn(),
                reserva.getDataCheckout()).stream().findAny().isPresent())
                throw new ReservaIndisponivelException("Reserva não disponível para o período escolhido");
    }
}