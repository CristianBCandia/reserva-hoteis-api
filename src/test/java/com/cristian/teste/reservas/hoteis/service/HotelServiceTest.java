package com.cristian.teste.reservas.hoteis.service;

import com.cristian.teste.reservas.hoteis.converter.HotelConverter;
import com.cristian.teste.reservas.hoteis.dto.HotelDTO;
import com.cristian.teste.reservas.hoteis.exception.CriterioInvalidoException;
import com.cristian.teste.reservas.hoteis.exception.HotelNaoEncontradoException;
import com.cristian.teste.reservas.hoteis.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.cristian.teste.reservas.hoteis.utils.TestUtils.criarHotelDTO;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void deveBuscarHotelPorId() {
        Long id = 1L;
        HotelDTO hotelDTO = criarHotelDTO(id);
        when(hotelRepository.findById(id)).thenReturn(Optional.of(HotelConverter.entidade(hotelDTO)));

        HotelDTO resultado = hotelService.buscarHotel(id);

        assertEquals(hotelDTO, resultado);
        verify(hotelRepository, times(1)).findById(id);
    }

    @Test
    void deveAtualizarHotel() {
        Long id = 1L;
        HotelDTO hotelDTO = criarHotelDTO(id);
        HotelDTO hotelDTOAtualizado = criarHotelDTO(id, "Hotel Teste Atualizado");
        when(hotelRepository.findById(id)).thenReturn(Optional.of(HotelConverter.entidade(hotelDTOAtualizado)));
        when(hotelRepository.save(any())).thenReturn(HotelConverter.entidade(hotelDTOAtualizado));

        HotelDTO resultado = hotelService.atualizarHotel(id, hotelDTOAtualizado);

        assertEquals(hotelDTOAtualizado, resultado);
        verify(hotelRepository, times(1)).findById(id);
        verify(hotelRepository, times(1)).save(any());
    }

    @Test
    void deveLancarExcecaoAoAtualizarHotelNaoEncontrado() {
        Long id = 1L;
        HotelDTO hotelDTO = criarHotelDTO(id);
        when(hotelRepository.findById(id)).thenReturn(Optional.empty());

        HotelNaoEncontradoException exception = assertThrows(HotelNaoEncontradoException.class, () -> {
            hotelService.atualizarHotel(id, hotelDTO);
        });

        assertEquals("Hotel não encontrado para o id " + id, exception.getMessage());
        verify(hotelRepository, times(1)).findById(id);
        verify(hotelRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoHotelNaoEncontrado() {
        Long id = 1L;
        when(hotelRepository.findById(id)).thenReturn(Optional.empty());

        HotelNaoEncontradoException exception = assertThrows(HotelNaoEncontradoException.class, () -> {
            hotelService.buscarHotel(id);
        });

        assertEquals("Hotel não encontrado para o id " + id, exception.getMessage());
        verify(hotelRepository, times(1)).findById(id);
    }

    @Test
    void devePesquisarHoteis() {
        String cidade = "Cidade Teste";
        LocalDate dataCheckIn = LocalDate.now();
        LocalDate dataCheckOut = LocalDate.now().plusDays(1);
        int numeroQuartos = 2;
        int numeroHospedes = 2;

        HotelDTO hotelDTO = criarHotelDTO(1L);
        when(hotelRepository.buscaHoteisDisponiveis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes))
                .thenReturn(List.of(HotelConverter.entidade(hotelDTO)));

        List<HotelDTO> resultado = hotelService.pesquisarHoteis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes);

        assertEquals(1, resultado.size());
        assertEquals(hotelDTO, resultado.get(0));
        verify(hotelRepository, times(1)).buscaHoteisDisponiveis(cidade, dataCheckIn, dataCheckOut, numeroQuartos, numeroHospedes);
    }

    @Test
    void deveCompararHoteisPorAvaliacao() {
        Long id = 1L;
        HotelDTO hotelComparar = criarHotelDTO(id);
        when(hotelRepository.findById(id)).thenReturn(Optional.of(HotelConverter.entidade(hotelComparar)));
        when(hotelRepository.findHoteisByCidadeAndNumeroDeQuartosOrderByAvaliacao(
                hotelComparar.cidade(), hotelComparar.numeroDeQuartos()))
                .thenReturn(List.of(HotelConverter.entidade(hotelComparar)));

        List<HotelDTO> resultado = hotelService.compararHotel(id, "AVALIACAO");

        assertEquals(1, resultado.size());
        assertEquals(hotelComparar, resultado.get(0));
        verify(hotelRepository, times(1)).findById(id);
        verify(hotelRepository, times(1)).findHoteisByCidadeAndNumeroDeQuartosOrderByAvaliacao(
                hotelComparar.cidade(), hotelComparar.numeroDeQuartos());
    }

    @Test
    void deveCompararHoteisPorPreco() {
        Long id = 1L;
        HotelDTO hotelComparar = criarHotelDTO(id);
        when(hotelRepository.findById(id)).thenReturn(Optional.of(HotelConverter.entidade(hotelComparar)));
        when(hotelRepository.findHoteisByCidadeAndNumeroDeQuartosOrderByPrecoPorNoite(
                hotelComparar.cidade(), hotelComparar.numeroDeQuartos()))
                .thenReturn(List.of(HotelConverter.entidade(hotelComparar)));

        List<HotelDTO> resultado = hotelService.compararHotel(id, "PRECO");

        assertEquals(1, resultado.size());
        assertEquals(hotelComparar, resultado.get(0));
        verify(hotelRepository, times(1)).findById(id);
        verify(hotelRepository, times(1)).findHoteisByCidadeAndNumeroDeQuartosOrderByPrecoPorNoite(
                hotelComparar.cidade(), hotelComparar.numeroDeQuartos());
    }

    @Test
    void deveLancarExcecaoQuandoCriterioDeComparacaoInvalido() {
        Long id = 1L;
        when(hotelRepository.findById(id)).thenReturn(Optional.of(HotelConverter.entidade(criarHotelDTO(id))));

        CriterioInvalidoException exception = assertThrows(CriterioInvalidoException.class, () -> {
            hotelService.compararHotel(id, "INVALIDO");
        });

        assertEquals("Critério de comparação inválido. Valores válidos: AVALIACAO e PRECO.", exception.getMessage());
        verify(hotelRepository, times(1)).findById(id);
        verify(hotelRepository, never()).findHoteisByCidadeAndNumeroDeQuartosOrderByAvaliacao(anyString(), anyInt());
        verify(hotelRepository, never()).findHoteisByCidadeAndNumeroDeQuartosOrderByPrecoPorNoite(anyString(), anyInt());
    }
}

