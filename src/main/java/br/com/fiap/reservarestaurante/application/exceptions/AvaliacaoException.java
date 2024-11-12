package br.com.fiap.reservarestaurante.application.exceptions;

public class AvaliacaoException extends RuntimeException {
    public AvaliacaoException(String mensagem) {
        super(mensagem);
    }
}
