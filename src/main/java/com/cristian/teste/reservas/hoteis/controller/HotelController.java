package com.cristian.teste.reservas.hoteis.controller;
import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.exception.HotelNaoEncontradoException;
import com.cristian.teste.reservas.hoteis.model.Hotel;
import com.cristian.teste.reservas.hoteis.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hoteis")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    @CacheEvict(value = "hotelDetalhes", key = "#hotel.id")
    public Hotel adicionarHotel(@RequestBody Hotel hotel) {
        return hotelService.adicionarHotel(hotel);
    }

    @GetMapping("/{id}")
    @Cacheable(value = "hotelDetalhes", key = "#id")
    public Hotel buscarHotel(@PathVariable Long id) {
        return hotelService.buscarHotel(id)
                .orElseThrow(() -> new HotelNaoEncontradoException("Hotel não encontrado para o id " + id));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "hotelDetalhes", key = "#id")
    public Hotel atualizarHotel(@PathVariable Long id, @RequestBody Hotel hotelAtualizado) {
        return hotelService.atualizarHotel(id, hotelAtualizado)
                .orElseThrow(() -> new HotelNaoEncontradoException("Hotel não encontrado para o id " + id));
    }

    @GetMapping("/pesquisar")
    @Cacheable(value = "hoteisPorCidade", key = "#cidade")
    public List<HotelDTO> pesquisarHoteis(
            @RequestParam String cidade,
            @RequestParam(required = false) LocalDate dataCheckIn,
            @RequestParam(required = false) LocalDate dataCheckOut,
            @RequestParam(required = false, defaultValue = "1") int numeroQuartos,
            @RequestParam(required = false, defaultValue = "1") int numeroHospedes) {
        return hotelService.pesquisarHoteis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes);
    }

    @GetMapping("/comparar/{id}")
    @Cacheable(value = "compararHoteis", key = "{#id, #criterio}")
    public List<HotelDTO> compararHotel(
            @PathVariable("id") Long id,
            @RequestParam("criterio") String criterio) {
        return hotelService.compararHotel(id, criterio);
    }
}

