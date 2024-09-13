package com.cristian.teste.reservas.hoteis.converter;

import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.model.Reserva;

public class ReservaConverter {

    public static ReservaDTO dto(Reserva reserva) {
        if(reserva == null) return  null;
        return new ReservaDTO(
                reserva.getId(),
                HotelConverter.dto(reserva.getHotel()),
                QuartoConverter.dto(reserva.getQuarto()),
                reserva.getNomeHospede(),
                reserva.getEmailHospede(),
                reserva.getTelefoneHospede(),
                reserva.getNumeroHospedes(),
                reserva.getDataCheckIn(),
                reserva.getDataCheckOut(),
                reserva.getStatus()
        );
    }

    public static Reserva entidade(ReservaDTO reserva) {
        return new Reserva(
                reserva.getId(),
                HotelConverter.entidade(reserva.getHotel()),
                QuartoConverter.entidade(reserva.getQuarto()),
                reserva.getNomeHospede(),
                reserva.getEmailHospede(),
                reserva.getTelefoneHospede(),
                reserva.getDataCheckIn(),
                reserva.getDataCheckout(),
                reserva.getNumeroHospedes(),
                reserva.getStatus()
        );

    }
}