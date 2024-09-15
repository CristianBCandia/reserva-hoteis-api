package com.cristian.teste.reservas.hoteis.exception;

public class ConsumerException extends RuntimeException {
    public ConsumerException(String topico, Throwable causa) {
        super("Erro ao consumir mensagem do tópico " + topico, causa);
    }
}