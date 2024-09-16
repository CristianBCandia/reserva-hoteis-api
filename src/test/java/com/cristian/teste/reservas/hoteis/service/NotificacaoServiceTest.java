package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.converter.NotificacaoConverter;
import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;
import com.cristian.teste.reservas.hoteis.model.Hotel;
import com.cristian.teste.reservas.hoteis.model.Notificacao;
import com.cristian.teste.reservas.hoteis.model.Quarto;
import com.cristian.teste.reservas.hoteis.model.Reserva;
import com.cristian.teste.reservas.hoteis.producer.NotificacaoProducer;
import com.cristian.teste.reservas.hoteis.repository.NotificacaoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificacaoServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private NotificacaoProducer notificacaoProducer;

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    public NotificacaoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarNotificacao() {
        Reserva reserva = criarReserva();
        TipoNotificacao tipoNotificacao = TipoNotificacao.CONFIRMACAO_RESERVA;

        NotificacaoRepository notificacaoRepository = mock(NotificacaoRepository.class);
        NotificacaoProducer notificacaoProducer = mock(NotificacaoProducer.class);
        JavaMailSender mailSender = mock(JavaMailSender.class);

        NotificacaoService notificacaoService = new NotificacaoService(mailSender, notificacaoProducer, notificacaoRepository);

        ArgumentCaptor<Notificacao> notificacaoCaptor = ArgumentCaptor.forClass(Notificacao.class);

        notificacaoService.criarNotificacao(reserva, tipoNotificacao);

        verify(notificacaoRepository).save(notificacaoCaptor.capture());
        Notificacao notificacaoSalva = notificacaoCaptor.getValue();

        assertEquals("hospede@example.com", notificacaoSalva.getEmailDestinatario());
        assertEquals("Hospede Teste sua reserva no hotel Hotel Teste foi confirmada com sucesso", notificacaoSalva.getMensagem());
        assertNull(notificacaoSalva.getEnviadaEm());
        assertFalse(notificacaoSalva.isVisualizada());
        assertEquals(tipoNotificacao, notificacaoSalva.getTipoNotificacao());
        assertEquals(reserva, notificacaoSalva.getReserva());
    }

    @Test
    void deveSalvarNotificacao() {
        Notificacao notificacao = criarNotificacao(criarReserva(), TipoNotificacao.CONFIRMACAO_RESERVA);

        notificacaoService.salvar(notificacao);

        verify(notificacaoRepository, times(1)).save(notificacao);
    }

    @Test
    void deveEnviarNotificacaoParaFila() {
        Notificacao notificacao = criarNotificacao(criarReserva(), TipoNotificacao.CONFIRMACAO_RESERVA);

        notificacaoService.enviarNotificacaoParaFila(notificacao);

        verify(notificacaoProducer, times(1)).sendMessage(NotificacaoConverter.dto(notificacao));
    }

    @Test
    void deveEnviarEmail() {
        Notificacao notificacao = criarNotificacao(criarReserva(), TipoNotificacao.CONFIRMACAO_RESERVA);
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(notificacao.getEmailDestinatario());
        mensagem.setSubject("Notificação de Reserva");
        mensagem.setText(notificacao.getMensagem());

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        notificacaoService.enviarEmail(notificacao);

        verify(mailSender, times(1)).send(mensagem);
        verify(notificacaoRepository, times(2)).save(notificacao);
        assertNotNull(notificacao.getEnviadaEm());
    }

    private Reserva criarReserva() {
        Hotel hotel = new Hotel();
        hotel.setNome("Hotel Teste");
        hotel.setId(1L);
        Quarto quarto = new Quarto();
        quarto.setId(1L);
        quarto.setId(1L);

        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setHotel(hotel);
        reserva.setQuarto(quarto);
        reserva.setNomeHospede("Hospede Teste");
        reserva.setEmailHospede("hospede@example.com");
        reserva.setTelefoneHospede("123456789");
        reserva.setDataCheckIn(LocalDate.now());
        reserva.setDataCheckOut(LocalDate.now().plusDays(2));
        reserva.setNumeroHospedes(2);
        reserva.setStatus(StatusReserva.CONFIRMADA);
        return reserva;
    }

    private Notificacao criarNotificacao(Reserva reserva, TipoNotificacao tipoNotificacao) {
        return Notificacao.builder()
                .reserva(reserva)
                .emailDestinatario(reserva.getEmailHospede())
                .enviadaEm(null)
                .tipoNotificacao(tipoNotificacao)
                .visualizada(false)
                .mensagem(reserva.getNomeHospede()
                        + ", sua reserva no hotel " + reserva.getHotel().getNome()
                        + " foi confirmada com sucesso.")
                .build();
    }
}
