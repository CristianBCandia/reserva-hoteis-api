package com.cristian.teste.reservas.hoteis.converter;

import com.cristian.teste.reservas.hoteis.dto.QuartoDTO;
import com.cristian.teste.reservas.hoteis.model.Quarto;

public class QuartoConverter {

    public static QuartoDTO dto(Quarto quarto) {
        return new QuartoDTO(
                quarto.getId(),
                HotelConverter.dto(quarto.getHotel()),
                quarto.getTipoQuarto(),
                quarto.getPrecoPorNoite() != null ? quarto.getPrecoPorNoite() : null,
                quarto.getMaxHospedes() != null ? quarto.getMaxHospedes() : null,
                quarto.getDisponivel()
        );
    }

    public static Quarto entidade(QuartoDTO quarto) {
        if(quarto == null) return null;
        return new Quarto(
                quarto.id(),
                HotelConverter.entidade(quarto.hotel()),
                quarto.tipoQuarto(),
                quarto.precoPorNoite(),
                quarto.maxHospedes(),
                quarto.disponivel()
        );
    }
}
