package com.cristian.teste.reservas.hoteis.dto;

import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.descriptor.java.spi.BasicCollectionJavaType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {
    private Long id;
    private ReservaDTO reserva;
    private String emailDestinatario;
    private LocalDateTime enviadaEm;
    private String mensagem;
    private Boolean visualizada;
    private TipoNotificacao tipoNotificacao;
}

