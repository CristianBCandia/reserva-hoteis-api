package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.converter.HotelConverter;
import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.exception.CriterioInvalidoException;
import com.cristian.teste.reservas.hoteis.exception.HotelNaoEncontradoException;
import com.cristian.teste.reservas.hoteis.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelService {

    private final HotelRepository hotelRepository;

    @Transactional
    public HotelDTO adicionarHotel(HotelDTO hotel) {
        return HotelConverter.dto(hotelRepository.save(HotelConverter.entidade(hotel)));
    }

    public HotelDTO buscarHotel(Long id) {
        return HotelConverter.dto(hotelRepository.findById(id).orElseThrow(() -> {
            log.error("ERRO - Hotel não encontrado para o id={}", id);
            return new HotelNaoEncontradoException("Hotel não encontrado para o id " + id);
        }));
    }

    @Transactional
    public HotelDTO atualizarHotel(Long id, HotelDTO hotelAtualizado) {
        buscarHotel(id);
        return HotelConverter.dto(hotelRepository.save(HotelConverter.entidade(hotelAtualizado, id)));
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> pesquisarHoteis(String cidade, LocalDate dataCheckIn, LocalDate dataCheckOut, int numeroQuartos, int numeroHospedes) {
        return HotelConverter.dtoList(hotelRepository.buscaHoteisDisponiveis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes));
    }

    public List<HotelDTO> compararHotel(Long id, String criterio) {
        var hotelComparar = buscarHotel(id);

        if("AVALIACAO".equals(criterio)) {
            return HotelConverter.dtoList(hotelRepository.findHoteisByCidadeAndNumeroDeQuartosOrderByAvaliacao(
                    hotelComparar.cidade(),
                    hotelComparar.numeroDeQuartos()));
        } else if("PRECO".equals(criterio)) {
            return HotelConverter.dtoList(hotelRepository.findHoteisByCidadeAndNumeroDeQuartosOrderByPrecoPorNoite(
                    hotelComparar.cidade(),
                    hotelComparar.numeroDeQuartos()));
        } else {
            throw new CriterioInvalidoException("Critério de comparação inválido. Valores válidos: AVALIACAO e PRECO.");
        }
    }
}