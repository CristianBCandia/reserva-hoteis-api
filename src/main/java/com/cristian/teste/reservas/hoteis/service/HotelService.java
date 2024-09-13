package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.converter.HotelConverter;
import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.exception.CriterioInvalidoException;
import com.cristian.teste.reservas.hoteis.model.Hotel;
import com.cristian.teste.reservas.hoteis.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    @Transactional
    public Hotel adicionarHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Optional<Hotel> buscarHotel(Long id) {
        return hotelRepository.findById(id);
    }

    @Transactional
    public Optional<Hotel> atualizarHotel(Long id, Hotel hotelAtualizado) {
        return hotelRepository.findById(id).map(existingHotel -> {
            existingHotel.setNome(hotelAtualizado.getNome());
            existingHotel.setEndereco(hotelAtualizado.getEndereco());
            existingHotel.setCidade(hotelAtualizado.getCidade());
            existingHotel.setEstado(hotelAtualizado.getEstado());
            existingHotel.setPais(hotelAtualizado.getPais());
            existingHotel.setTelefone(hotelAtualizado.getTelefone());
            existingHotel.setNumeroDeQuartos(hotelAtualizado.getNumeroDeQuartos());
            existingHotel.setPrecoPorNoite(hotelAtualizado.getPrecoPorNoite());
            existingHotel.setComodidades(hotelAtualizado.getComodidades());
            existingHotel.setAvaliacao(hotelAtualizado.getAvaliacao());
            return hotelRepository.save(existingHotel);
        });
    }

    @Transactional(readOnly = true)
    public List<HotelDTO> pesquisarHoteis(String cidade, LocalDate dataCheckIn, LocalDate dataCheckOut, int numeroQuartos, int numeroHospedes) {
        return HotelConverter.dtoList(hotelRepository.buscaHoteisDisponiveis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes));
    }

    public List<HotelDTO> compararHotel(Long id, String criterio) {
        var hotelComparar = hotelRepository.findById(id).orElseThrow();
        if("AVALIACAO".equals(criterio)) {
            return HotelConverter.dtoList(hotelRepository.findHoteisByCidadeAndNumeroDeQuartosOrderByAvaliacao(
                    hotelComparar.getCidade(),
                    hotelComparar.getNumeroDeQuartos()));

        } else if("PRECO".equals(criterio)) {
            return HotelConverter.dtoList(hotelRepository.findHoteisByCidadeAndNumeroDeQuartosOrderByPrecoPorNoite(
                    hotelComparar.getCidade(),
                    hotelComparar.getNumeroDeQuartos()));
        } else {
            throw new CriterioInvalidoException("Critério de comparação inválido. Valores válidos: AVALIACAO e PRECO.");
        }
    }
}