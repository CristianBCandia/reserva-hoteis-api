package com.cristian.teste.reservas.hoteis.dto;

import com.cristian.teste.reservas.hoteis.enums.StatusReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Long id;
    private HotelDTO hotel;
    private QuartoDTO quarto;
    private String nomeHospede;
    private String emailHospede;
    private String telefoneHospede;
    private Integer numeroHospedes;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckout;
    private StatusReserva status;
}

