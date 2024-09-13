package com.cristian.teste.reservas.hoteis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", referencedColumnName = "id", nullable = false)
    private Reserva reserva;  // Referência à reserva

    @Column(name = "metodo_pagamento", nullable = false)
    private String metodoPagamento;  // Ex: Cartão de Crédito, Cartão de Débito, PayPal

    @Column(name = "valor", nullable = false)
    private double valor;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Column(name = "pagamento_confirmado", nullable = false)
    private boolean pagamentoConfirmado;
}
