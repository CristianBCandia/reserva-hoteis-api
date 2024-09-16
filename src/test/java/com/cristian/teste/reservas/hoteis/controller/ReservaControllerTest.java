package com.cristian.teste.reservas.hoteis.controller;

import com.cristian.teste.reservas.hoteis.dto.ComodidadeDTO;
import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.dto.QuartoDTO;
import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.enums.StatusReserva;
import com.cristian.teste.reservas.hoteis.service.ReservaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(ReservaController.class)
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testBuscarReservaPorId() throws Exception {
        Long reservaId = 1L;
        HotelDTO hotel = new HotelDTO(
                1L, "Hotel Teste", "São Paulo", "Endereço Teste", 100.0, 10, 2,
                List.of(new ComodidadeDTO(1L, "Wi-Fi", null)), 4.5);
        QuartoDTO quarto = new QuartoDTO(
                1L, hotel, "Suite", 150.0, 2, true);
        ReservaDTO reserva = new ReservaDTO(
                reservaId, hotel, quarto, "João", "joao@example.com", "123456789",
                2, LocalDate.now(), LocalDate.now().plusDays(2), StatusReserva.PENDENTE);

        when(reservaService.buscaReservaPorId(reservaId)).thenReturn(reserva);

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/{id}", reservaId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(reservaId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nomeHospede").value("João"));

        verify(reservaService, times(1)).buscaReservaPorId(reservaId);
    }

    @Test
    void testCriarReserva() throws Exception {
        HotelDTO hotel = new HotelDTO(
                1L, "Hotel Teste", "São Paulo", "Endereço Teste", 100.0, 10, 2,
                List.of(new ComodidadeDTO(1L, "Wi-Fi", null)), 4.5);
        QuartoDTO quarto = new QuartoDTO(
                1L, hotel, "Suite", 150.0, 2, true);
        ReservaDTO reserva = new ReservaDTO(
                null, hotel, quarto, "João", "joao@example.com", "123456789",
                2, LocalDate.now(), LocalDate.now().plusDays(2), StatusReserva.PENDENTE);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(reservaService, times(1)).processaCriacaoReserva(reserva);
    }

    @Test
    void testConfirmarReserva() throws Exception {
        Long reservaId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch("/reservas/{id}/confirmar", reservaId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(reservaService, times(1)).processaConfirmacaoReserva(reservaId);
    }

    @Test
    void testCheckIn() throws Exception {
        Long reservaId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch("/reservas/{id}/checkin", reservaId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(reservaService, times(1)).processaCheckIn(reservaId);
    }

    @Test
    void testCheckOut() throws Exception {
        Long reservaId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.patch("/reservas/{id}/checkout", reservaId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(reservaService, times(1)).processaCheckOut(reservaId);
    }
}