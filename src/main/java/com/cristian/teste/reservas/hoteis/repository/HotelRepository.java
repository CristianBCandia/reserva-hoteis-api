package com.cristian.teste.reservas.hoteis.repository;

import com.cristian.teste.reservas.hoteis.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("SELECT h FROM Hotel h WHERE h.cidade = :cidade AND h.numeroDeQuartos >= :numeroDeQuartos ORDER BY h.precoPorNoite ASC")
    List<Hotel> findHoteisByCidadeAndNumeroDeQuartosOrderByPrecoPorNoite(
            @Param("cidade") String cidade,
            @Param("numeroDeQuartos") int numeroDeQuartos);

    @Query("SELECT h FROM Hotel h WHERE h.cidade = :cidade AND h.numeroDeQuartos >= :numeroDeQuartos ORDER BY h.avaliacao DESC")
    List<Hotel> findHoteisByCidadeAndNumeroDeQuartosOrderByAvaliacao(
            @Param("cidade") String cidade,
            @Param("numeroDeQuartos") int numeroDeQuartos);

    @Query("SELECT h FROM Hotel h " +
            "WHERE h.cidade = :cidade " +
            "AND h.numeroDeQuartos >= :numeroDeQuartos " +
            "AND h.numeroDeHospedes >= :numeroDeHospedes " +
            "AND NOT EXISTS (" +
            "    SELECT r FROM Reserva r " +
            "    WHERE r.hotel = h " +
            "    AND (r.dataCheckIn < :dataCheckOut AND r.dataCheckOut > :dataCheckIn)" +
            ")")
    List<Hotel> buscaHoteisDisponiveis(
            @Param("cidade") String cidade,
            @Param("dataCheckIn") LocalDate dataCheckIn,
            @Param("dataCheckOut") LocalDate dataCheckOut,
            @Param("numeroDeQuartos") int numeroDeQuartos,
            @Param("numeroDeHospedes") int numeroDeHospedes);

}