package com.cristian.teste.reservas.hoteis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public record HotelDTO (
        Long id,
        String nome,
        String cidade,
        String endereco,
        double precoPorNoite,
        int numeroDeQuartos,
        int numeroDeHospedes,
        List<ComodidadeDTO> comodidades,
        double avaliacao
){}
