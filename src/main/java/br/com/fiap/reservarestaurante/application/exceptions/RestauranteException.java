package br.com.fiap.reservarestaurante.application.exceptions;

public class RestauranteException extends RuntimeException {
    public RestauranteException(String mensagem) {
        super(mensagem);
    }
}
