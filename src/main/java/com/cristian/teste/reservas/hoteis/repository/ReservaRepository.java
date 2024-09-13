package com.cristian.teste.reservas.hoteis.repository;

import com.cristian.teste.reservas.hoteis.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("SELECT r FROM Reserva r WHERE r.hotel.id = :hotelId " +
            "AND r.quarto.id = :quartoId " +
            "AND ((r.dataCheckIn <= :dataCheckOut AND r.dataCheckOut >= :dataCheckIn) " +
            "OR (r.dataCheckIn BETWEEN :dataCheckIn AND :dataCheckOut " +
            "OR r.dataCheckOut BETWEEN :dataCheckIn AND :dataCheckOut))")
    List<Reserva> buscaPorReservasNoMesmoHotelQuartoEPeriodo(@Param("hotelId") Long hotelId,
                                              @Param("quartoId") Long quartoId,
                                              @Param("dataCheckIn") LocalDate dataCheckIn,
                                              @Param("dataCheckOut") LocalDate dataCheckOut);
}
