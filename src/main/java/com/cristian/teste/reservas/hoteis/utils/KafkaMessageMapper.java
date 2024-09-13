package com.cristian.teste.reservas.hoteis.utils;

import com.cristian.teste.reservas.hoteis.exception.KafkaMessageMapperException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageMapper {

    private final ObjectMapper mapper;

    public String map(Object mensagem) {
        try {
            return mapper.writeValueAsString(mensagem);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter mensagem para o kafka: {}", mensagem);
            throw new KafkaMessageMapperException("Erro ao converter mensagem para o kafka");
        }
    }
}
