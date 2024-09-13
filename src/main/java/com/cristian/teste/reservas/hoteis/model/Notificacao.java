package com.cristian.teste.reservas.hoteis.model;

import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notificacao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @OneToOne
        private Reserva reserva;

        @Column(name = "email_destinatario", nullable = false)
        private String emailDestinatario;

        @Column(name = "enviada_em")
        private LocalDateTime enviadaEm;

        @Lob
        @Column(name = "mensagem", nullable = false)
        private String mensagem;

        @Column(name = "visualizada", nullable = false)
        private boolean visualizada;

        @Enumerated(EnumType.STRING)
        @Column(name = "tipo_notificacao", nullable = false)
        private TipoNotificacao tipoNotificacao;
}