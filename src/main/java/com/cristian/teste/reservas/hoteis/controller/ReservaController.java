package com.cristian.teste.reservas.hoteis.controller;

import com.cristian.teste.reservas.hoteis.dto.ReservaDTO;
import com.cristian.teste.reservas.hoteis.service.ReservaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
@Slf4j
@RequiredArgsConstructor
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    private final ReservaService reservaService;

    @GetMapping("/{id}")
    public ReservaDTO buscarReservaPorId(@PathVariable Long id) {
        return reservaService.buscaReservaPorId(id);
    }

    @PostMapping
    public void criarReserva(@RequestBody ReservaDTO reserva) {
        logger.info("Início do processamento de criação de reserva para o hospede {} no hotel {}",
                reserva.getNomeHospede(), reserva.getHotel().nome());
        reservaService.processaCriacaoReserva(reserva);
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmarReserva(@PathVariable Long id) {
        logger.info("Início do processamento de confirmação de reserva para reserva id {}", id);
        reservaService.processaConfirmacaoReserva(id);
    }

    @PatchMapping("/{id}/checkin")
    public void checkIn(@PathVariable Long id) {
        logger.info("Início do processamento de checkin para reserva id {}", id);
        reservaService.processaCheckIn(id);
    }

    @PatchMapping("/{id}/checkout")
    public void checkOut(@PathVariable Long id) {
        logger.info("Início do processamento de checkout para reserva id {}", id);
        reservaService.processaCheckOut(id);
    }
}

