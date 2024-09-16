package com.cristian.teste.reservas.hoteis.dto;

public record QuartoDTO(
    Long id,
    HotelDTO hotel,
    String tipoQuarto,
    Double precoPorNoite,
    Integer maxHospedes,
    Boolean disponivel
) {
}
