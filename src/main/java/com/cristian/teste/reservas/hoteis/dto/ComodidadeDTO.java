package com.cristian.teste.reservas.hoteis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComodidadeDTO(Long id, String descricao, HotelDTO hotel) {
}
