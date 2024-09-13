package com.cristian.teste.reservas.hoteis.converter;

import com.cristian.teste.reservas.hoteis.dto.NotificacaoDTO;
import com.cristian.teste.reservas.hoteis.model.Notificacao;


public class NotificacaoConverter {

    public static Notificacao entidade(NotificacaoDTO dto) {
        return Notificacao.builder()
                .id(dto.getId())
                .reserva(ReservaConverter.entidade(dto.getReserva()))
                .tipoNotificacao(dto.getTipoNotificacao())
                .visualizada(dto.getVisualizada())
                .enviadaEm(dto.getEnviadaEm() != null ? dto.getEnviadaEm() : null)
                .mensagem(dto.getMensagem())
                .emailDestinatario(dto.getEmailDestinatario())
                .build();
    }

    public static NotificacaoDTO dto(Notificacao entidade) {
        return new NotificacaoDTO(
                entidade.getId(),
                ReservaConverter.dto(entidade.getReserva()),
                entidade.getEmailDestinatario(),
                entidade.getEnviadaEm() != null ? entidade.getEnviadaEm() : null,
                entidade.getMensagem(),
                entidade.isVisualizada(),
                entidade.getTipoNotificacao()
        );
    }
}
