package com.cristian.teste.reservas.hoteis.controller;

import com.cristian.teste.reservas.hoteis.dto.ComodidadeDTO;
import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@WebMvcTest(HotelController.class)
@ActiveProfiles("test")
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Any setup logic can be placed here
    }

    @Test
    void testAdicionarHotel() throws Exception {
        HotelDTO hotel = new HotelDTO(
                1L, "Hotel Teste", "São Paulo", "Endereço Teste", 100.0, 10, 2,
                List.of(new ComodidadeDTO(1L, "Wi-Fi", null)), 4.5);

        when(hotelService.adicionarHotel(hotel)).thenReturn(hotel);

        mockMvc.perform(MockMvcRequestBuilders.post("/hoteis")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotel)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Hotel Teste"));

        verify(hotelService, times(1)).adicionarHotel(hotel);
    }

    @Test
    void testBuscarHotel() throws Exception {
        Long hotelId = 1L;
        HotelDTO hotel = new HotelDTO(
                1L, "Hotel Teste", "São Paulo", "Endereço Teste", 100.0, 10, 2,
                List.of(new ComodidadeDTO(1L, "Wi-Fi", null)), 4.5);

        when(hotelService.buscarHotel(hotelId)).thenReturn(hotel);

        mockMvc.perform(MockMvcRequestBuilders.get("/hoteis/{id}", hotelId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Hotel Teste"));

        verify(hotelService, times(1)).buscarHotel(hotelId);
    }

    @Test
    void testAtualizarHotel() throws Exception {
        Long hotelId = 1L;
        HotelDTO hotelAtualizado = new HotelDTO(
                1L, "Hotel Atualizado", "São Paulo", "Novo Endereço", 120.0, 12, 3,
                List.of(new ComodidadeDTO(2L, "Piscina", null)), 4.8);

        when(hotelService.atualizarHotel(hotelId, hotelAtualizado)).thenReturn(hotelAtualizado);

        mockMvc.perform(MockMvcRequestBuilders.put("/hoteis/{id}", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelAtualizado)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(hotelId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome").value("Hotel Atualizado"));

        verify(hotelService, times(1)).atualizarHotel(hotelId, hotelAtualizado);
    }

    @Test
    void testPesquisarHoteis() throws Exception {
        String cidade = "São Paulo";
        LocalDate dataCheckIn = LocalDate.now();
        LocalDate dataCheckOut = LocalDate.now().plusDays(1);
        int numeroQuartos = 2;
        int numeroHospedes = 3;
        List<HotelDTO> hoteis = List.of(new HotelDTO(
                1L, "Hotel Teste", cidade, "Endereço Teste", 100.0, 10, 2,
                List.of(new ComodidadeDTO(1L, "Wi-Fi", null)), 4.5));

        when(hotelService.pesquisarHoteis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes))
                .thenReturn(hoteis);

        mockMvc.perform(MockMvcRequestBuilders.get("/hoteis/pesquisar")
                        .param("cidade", cidade)
                        .param("dataCheckIn", dataCheckIn.toString())
                        .param("dataCheckOut", dataCheckOut.toString())
                        .param("numeroQuartos", String.valueOf(numeroQuartos))
                        .param("numeroHospedes", String.valueOf(numeroHospedes))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Hotel Teste"));

        verify(hotelService, times(1)).pesquisarHoteis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes);
    }

    @Test
    void testCompararHotel() throws Exception {
        Long hotelId = 1L;
        String criterio = "preco";
        List<HotelDTO> hoteis = List.of(new HotelDTO(
                1L, "Hotel Comparado", "São Paulo", "Endereço Teste", 90.0, 8, 2,
                List.of(new ComodidadeDTO(1L, "Estacionamento", null)), 4.0));

        when(hotelService.compararHotel(hotelId, criterio)).thenReturn(hoteis);

        mockMvc.perform(MockMvcRequestBuilders.get("/hoteis/comparar/{id}", hotelId)
                        .param("criterio", criterio)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome").value("Hotel Comparado"));

        verify(hotelService, times(1)).compararHotel(hotelId, criterio);
    }
}
