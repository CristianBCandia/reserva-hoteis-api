package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.converter.NotificacaoConverter;
import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;
import com.cristian.teste.reservas.hoteis.model.Notificacao;
import com.cristian.teste.reservas.hoteis.model.Reserva;
import com.cristian.teste.reservas.hoteis.producer.NotificacaoProducer;
import com.cristian.teste.reservas.hoteis.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoService {

    private final JavaMailSender mailSender;
    private final NotificacaoProducer notificacaoProducer;
    private final NotificacaoRepository notificacaoRepository;

    public void criarNotificacao(Reserva reserva, TipoNotificacao tipoNotificacao) {
        var notificacao = gerarNotificacao(reserva, tipoNotificacao);
        notificacaoRepository.save(notificacao);
        enviarNotificacaoParaFila(notificacao);
    }

    public void salvar(Notificacao notificacao) {
        notificacaoRepository.save(notificacao);
    }

    public void enviarNotificacaoParaFila(Notificacao notificacao) {
        notificacaoProducer.sendMessage(NotificacaoConverter.dto(notificacao));
    }

    public void enviarEmail(Notificacao notificacao) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(notificacao.getEmailDestinatario());
        mensagem.setSubject("Notificação de Reserva");
        mensagem.setText(notificacao.getMensagem());

        mailSender.send(mensagem);

        notificacao.setEnviadaEm(LocalDateTime.now());
        salvar(notificacao);

        log.info("Notificação enviada para: {}", notificacao.getEmailDestinatario());
        notificacao.setEnviadaEm(LocalDateTime.now());
        salvar(notificacao);
    }

    private Notificacao gerarNotificacao(Reserva reserva, TipoNotificacao tipoNotificacao) {
        return Notificacao.builder()
                .reserva(reserva)
                .emailDestinatario(reserva.getEmailHospede())
                .enviadaEm(null)
                .tipoNotificacao(tipoNotificacao)
                .visualizada(false)
                .mensagem(reserva.getNomeHospede()
                        + " sua reserva no hotel " + reserva.getHotel().getNome()
                        + " foi confirmada com sucesso")
                .build();
    }

//    private static NotificacaoDTO converterParaDto(Notificacao notificacao) {
//        return new NotificacaoDTO(notificacao.getId(),
//                notificacao.getReserva().getId(),
//                notificacao.getEmailDestinatario(),
//                notificacao.getEnviadaEm(),
//                notificacao.getMensagem(),
//                notificacao.isVisualizada(),
//                notificacao.getTipoNotificacao());
//    }
}
