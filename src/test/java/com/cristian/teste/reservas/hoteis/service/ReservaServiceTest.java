package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;
import com.cristian.teste.reservas.hoteis.enums.TipoOperacaoReserva;
import com.cristian.teste.reservas.hoteis.exception.ReservaIndisponivelException;
import com.cristian.teste.reservas.hoteis.exception.TipoOperacaoInvalidaException;
import com.cristian.teste.reservas.hoteis.model.Reserva;
import com.cristian.teste.reservas.hoteis.producer.ReservaProducer;
import com.cristian.teste.reservas.hoteis.repository.ReservaRepository;
import com.cristian.teste.reservas.hoteis.utils.TestUtils;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.cristian.teste.reservas.hoteis.utils.TestUtils.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaProducer reservaProducer;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private ReservaService reservaService;


    @Test
    void deveProcessarCriacaoReserva() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setHotel(criarHotelDTO(1L));
        reservaDTO.setQuarto(getQuartoDTO());
        reservaDTO.setDataCheckIn(LocalDate.now().plusDays(1));
        reservaDTO.setDataCheckout(LocalDate.now().plusDays(2));

        when(reservaRepository.buscaPorReservasNoMesmoHotelQuartoEPeriodo(
                reservaDTO.getHotel().id(),
                reservaDTO.getQuarto().id(),
                reservaDTO.getDataCheckIn(),
                reservaDTO.getDataCheckout()))
                .thenReturn(emptyList()); // Nenhuma reserva encontrada

        reservaService.processaCriacaoReserva(reservaDTO);

        verify(reservaProducer, times(1)).sendMessage(reservaDTO, TipoOperacaoReserva.CRIACAO);
    }

    @Test
    void deveLancarExcecaoQuandoReservaNaoDisponivel() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setHotel(criarHotelDTO(1L));
        reservaDTO.setQuarto(getQuartoDTO());
        reservaDTO.setDataCheckIn(LocalDate.now().plusDays(1));
        reservaDTO.setDataCheckout(LocalDate.now().plusDays(2));

        when(reservaRepository.buscaPorReservasNoMesmoHotelQuartoEPeriodo(
                reservaDTO.getHotel().id(),
                reservaDTO.getQuarto().id(),
                reservaDTO.getDataCheckIn(),
                reservaDTO.getDataCheckout()))
                .thenReturn(List.of(new Reserva())); // Reserva existente

        ReservaIndisponivelException thrown = assertThrows(
                ReservaIndisponivelException.class,
                () -> reservaService.processaCriacaoReserva(reservaDTO),
                "Reserva não disponível para o período escolhido"
        );

        assertEquals("Reserva não disponível para o período escolhido", thrown.getMessage());
    }

    @Test
    void deveProcessarConfirmacaoReserva() {
        Long reservaId = 1L;
        Reserva reserva = reservaMock();
        reserva.setDataCheckIn(LocalDate.now().plusDays(1));

        when(reservaRepository.findById(any())).thenReturn(Optional.of(reserva));

        reservaService.processaConfirmacaoReserva(reservaId);

        verify(reservaProducer, times(1)).sendMessage(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoDataCheckInNula() {
        Long reservaId = 1L;
        var reserva = TestUtils.reservaMock();
        reserva.setDataCheckIn(null);

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        TipoOperacaoInvalidaException thrown = assertThrows(
                TipoOperacaoInvalidaException.class,
                () -> reservaService.processaConfirmacaoReserva(reservaId),
                "Erro: Data checkin é obrigatório para confirmar a reserva."
        );

        assertEquals("Erro: Data checkin é obrigatório para confirmar a reserva.", thrown.getMessage());
    }

    @Test
    void deveProcessarCheckIn() {
        var reserva = TestUtils.reservaMock();
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva.setDataCheckIn(LocalDate.now().minusDays(1));

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        reservaService.processaCheckIn(1L);

        verify(reservaProducer, times(1)).sendMessage(any(), any());
    }

    @Test
    void deveProcessarCheckOut() {
        Long reservaId = 1L;
        Reserva reserva = reservaMock();
        reserva.setStatus(StatusReserva.CHECKED_IN);
        reserva.setDataCheckOut(LocalDate.now().minusDays(1));

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));

        reservaService.processaCheckOut(reservaId);

        verify(reservaProducer, times(1)).sendMessage(any(), any());
    }

    @Test
    void deveCriarReserva() {
        Reserva reserva = reservaMock();
        reserva.setStatus(StatusReserva.PENDENTE);

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        Reserva result = reservaService.criarReserva(reserva);

        verify(reservaRepository, times(1)).save(reserva);
        verify(notificacaoService, times(1)).criarNotificacao(reserva, TipoNotificacao.CRIACAO_RESERVA);
        assertEquals(StatusReserva.PENDENTE, result.getStatus());
    }

    @Test
    @Transactional
    void deveConfirmarReserva() {
        Reserva reserva = reservaMock();
        reserva.setStatus(StatusReserva.PENDENTE);

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        reservaService.confirmarReserva(reserva);

        verify(reservaRepository, times(1)).save(reserva);
        verify(notificacaoService, times(1)).criarNotificacao(reserva, TipoNotificacao.CONFIRMACAO_RESERVA);
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
    }

    @Test
    @Transactional
    void deveCheckInReserva() {
        Reserva reserva = reservaMock();;
        reserva.setStatus(StatusReserva.CONFIRMADA);
        reserva.setDataCheckIn(LocalDate.now().minusDays(1));

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        reservaService.checkIn(reserva);

        verify(reservaRepository, times(1)).save(reserva);
        verify(notificacaoService, times(1)).criarNotificacao(reserva, TipoNotificacao.CHECK_IN);
        assertEquals(StatusReserva.CHECKED_IN, reserva.getStatus());
    }

    @Test
    @Transactional
    void deveCheckOutReserva() {
        Reserva reserva = reservaMock();
        reserva.setStatus(StatusReserva.CHECKED_IN);
        reserva.setDataCheckOut(LocalDate.now().minusDays(1));

        when(reservaRepository.save(reserva)).thenReturn(reserva);

        reservaService.checkOut(reserva);

        verify(reservaRepository, times(1)).save(reserva);
        verify(notificacaoService, times(1)).criarNotificacao(any(), any());
        assertEquals(StatusReserva.CHECKED_OUT, reserva.getStatus());
    }

    @Test
    void deveBuscarReservaPorId() {
        Reserva reserva = reservaMock();
        reserva.setId(1L);
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        ReservaDTO result = reservaService.buscaReservaPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void deveLancarExcecaoQuandoReservaNaoEncontrada() {
        Long reservaId = 1L;

        when(reservaRepository.findById(reservaId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.buscaReservaPorId(reservaId),
                "Reserva não encontrada!"
        );

        assertEquals("Reserva não encontrada!", thrown.getMessage());
    }
}


