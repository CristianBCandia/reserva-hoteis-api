package com.cristian.teste.reservas.hoteis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quarto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false)
    private Hotel hotel;

    @Column(name = "tipo_quarto", nullable = false)
    private String tipoQuarto; // Ex: Solteiro, Duplo, Suíte

    @Column(name = "preco_por_noite", nullable = false)
    private Double precoPorNoite;

    @Column(name = "max_hospedes", nullable = false)
    private Integer maxHospedes;

    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel;
}