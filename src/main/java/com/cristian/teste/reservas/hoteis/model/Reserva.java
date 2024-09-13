package com.cristian.teste.reservas.hoteis.model;

import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quarto_id", referencedColumnName = "id")
    private Quarto quarto;

    @Column(name = "nome_hospede")
    private String nomeHospede;

    @Column(name = "email_hospede")
    private String emailHospede;

    @Column(name = "telefone_hospede")
    private String telefoneHospede;

    @Column(name = "data_check_in")
    private LocalDate dataCheckIn;

    @Column(name = "data_check_out")
    private LocalDate dataCheckOut;

    @Column(name = "numero_hospedes")
    private Integer numeroHospedes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusReserva status;
}
