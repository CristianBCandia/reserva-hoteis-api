package com.cristian.teste.reservas.hoteis.utils;

import com.cristian.teste.reservas.hoteis.dto.*;
import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.cristian.teste.reservas.hoteis.enums.TipoNotificacao;
import com.cristian.teste.reservas.hoteis.model.Hotel;
import com.cristian.teste.reservas.hoteis.model.Quarto;
import com.cristian.teste.reservas.hoteis.model.Reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    private TestUtils() {}

    private static HotelDTO hotelDTO = new HotelDTO(
            1L,
            "Hotel 1",
            "Cidade Exemplo",
            "Rua Exemplo, 123",
            150.00,
            20,
            50,
            List.of(new ComodidadeDTO(1L, "Wi-Fi", null)),
            4.5
    );

    private static QuartoDTO quartoDTO = new QuartoDTO(
            1L,
            hotelDTO,
            "Suite",
            200.00,
            2,
            true
    );

    private static ReservaDTO reservaDTO = new ReservaDTO(
            1L,
            hotelDTO,
            quartoDTO,
            "João da Silva",
            "joao.silva@example.com",
            "+55 11 98765-4321",
            2,
            LocalDate.of(2024, 9, 15),
            LocalDate.of(2024, 9, 20),
            StatusReserva.CONFIRMADA
    );


    public static NotificacaoDTO notificacaoDTOMock() {
        return new NotificacaoDTO(
                1L,
                reservaDTO,
                "joao.silva@example.com",
                LocalDateTime.now(),
                "Sua reserva foi confirmada!",
                false,
                TipoNotificacao.CONFIRMACAO_RESERVA
        );
    }

    public static ReservaDTO reservaDTOMock() {
        HotelDTO hotelDTO = new HotelDTO(
                1L,
                "Hotel Exemplo",
                "Cidade Exemplo",
                "Endereço Exemplo",
                200.0,
                10,
                20,
                Arrays.asList(
                        new ComodidadeDTO(1L, "Wi-Fi", null),
                        new ComodidadeDTO(2L, "Piscina", null)
                ),
                4.5
        );

        QuartoDTO quartoDTO = new QuartoDTO(
                1L,
                hotelDTO,
                "Deluxe",
                150.0,
                2,
                true
        );

        return new ReservaDTO(
                1L,
                hotelDTO,
                quartoDTO,
                "João da Silva",
                "joao.silva@example.com",
                "123456789",
                2,
                LocalDate.of(2024, 9, 1),
                LocalDate.of(2024, 9, 10),
                StatusReserva.PENDENTE
        );
    }

    public static Reserva reservaMock() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setNome("Hotel Teste");

        Quarto quarto = new Quarto();
        quarto.setId(1L);

        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setHotel(hotel);
        reserva.setQuarto(quarto);
        reserva.setNomeHospede("Hospede Teste");
        reserva.setEmailHospede("hospede@example.com");
        reserva.setTelefoneHospede("123456789");
        reserva.setDataCheckIn(LocalDate.now().plusDays(1));
        reserva.setDataCheckOut(LocalDate.now().plusDays(2));
        reserva.setNumeroHospedes(2);
        reserva.setStatus(StatusReserva.PENDENTE);

        return reserva;
    }

    public static List<Reserva> reservaMockList() {
        return List.of(reservaMock());
    }

    public static HotelDTO criarHotelDTO(Long id) {
        return new HotelDTO(id, "Hotel Teste", "Cidade Teste", "Rua Teste", 150.0, 10, 4, List.of(), 4.7);
    }

    public static HotelDTO criarHotelDTO(Long id, String nome) {
        return new HotelDTO(id, nome, "Cidade Teste", "Rua Teste", 150.0, 10, 4, List.of(), 4.7);
    }

    public static QuartoDTO getQuartoDTO() {
        return reservaDTOMock().getQuarto();
    }
}
