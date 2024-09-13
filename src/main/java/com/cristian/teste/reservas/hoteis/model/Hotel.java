package com.cristian.teste.reservas.hoteis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotel",
        indexes = {
                @Index(name = "idx_estado_cidade", columnList = "estado, cidade"),
                @Index(name = "idx_preco_por_noite", columnList = "preco_por_noite")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false)
    private String telefone;

    @Column(name = "numero_de_quartos", nullable = false)
    private int numeroDeQuartos;

    @Column(name = "numero_de_hospedes", nullable = false)
    private int numeroDeHospedes;

    @Column(name = "preco_por_noite", nullable = false)
    private double precoPorNoite;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comodidade> comodidades = new ArrayList<>();

    @Column(nullable = false)
    private double avaliacao;
}

