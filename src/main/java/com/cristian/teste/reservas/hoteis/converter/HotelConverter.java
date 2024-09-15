package com.cristian.teste.reservas.hoteis.converter;

import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.model.Hotel;

import java.util.List;
import java.util.stream.Collectors;

public class HotelConverter {

    public static HotelDTO dto(Hotel hotel) {
        if(hotel == null) return null;
        return  new HotelDTO(
                hotel.getId(),
                hotel.getNome(),
                hotel.getCidade(),
                hotel.getEndereco(),
                hotel.getPrecoPorNoite(),
                hotel.getNumeroDeQuartos(),
                hotel.getNumeroDeHospedes(),
                hotel.getComodidades().stream().map(ComodidadeConverter::dto).collect(Collectors.toList()),
                hotel.getAvaliacao()
        );
    }

    public static Hotel entidade(HotelDTO hotel) {
        if(hotel == null) return null;
        return Hotel.builder()
                .id(hotel.id())
                .nome(hotel.nome())
                .numeroDeHospedes(hotel.numeroDeQuartos())
                .numeroDeQuartos(hotel.numeroDeQuartos())
                .cidade(hotel.cidade())
                .comodidades(hotel.comodidades().stream().map(ComodidadeConverter::entidade).collect(Collectors.toList()))
                .build();
    }

    public static Hotel entidade(HotelDTO hotel, Long id) {
        if(hotel == null) return null;
        return Hotel.builder()
                .id(id)
                .nome(hotel.nome())
                .cidade(hotel.cidade())
                .numeroDeHospedes(hotel.numeroDeQuartos())
                .numeroDeQuartos(hotel.numeroDeQuartos())
                .comodidades(hotel.comodidades().stream().map(ComodidadeConverter::entidade).collect(Collectors.toList()))
                .build();
    }

    public static List<HotelDTO> dtoList(List<Hotel> hoteis) {
        return hoteis.stream().map(HotelConverter::dto).collect(Collectors.toList());
    }
}
