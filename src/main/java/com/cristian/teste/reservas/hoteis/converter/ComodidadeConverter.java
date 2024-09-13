package com.cristian.teste.reservas.hoteis.converter;

import com.cristian.teste.reservas.hoteis.dto.ComodidadeDTO;
import com.cristian.teste.reservas.hoteis.model.Comodidade;

public class ComodidadeConverter {

    public static ComodidadeDTO dto(Comodidade comodidade) {
        return new ComodidadeDTO(
                comodidade.getId(),
                comodidade.getDescricao(),
                null
        );
    }

    public static Comodidade entidade(ComodidadeDTO dto) {
        return new Comodidade(
                dto.id(),
                dto.descricao(),
                HotelConverter.entidade(dto.hotel())
        );
    }
}
