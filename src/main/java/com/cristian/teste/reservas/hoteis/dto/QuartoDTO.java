package com.cristian.teste.reservas.hoteis.dto;

public record QuartoDTO(
    Long id,
    HotelDTO hotel,
    String tipoQuarto,
    double precoPorNoite,
    int maxHospedes,
    boolean disponivel
) {
}
