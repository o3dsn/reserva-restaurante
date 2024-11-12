package br.com.fiap.reservarestaurante.application.exceptions;

public class ReservaException extends RuntimeException {
    public ReservaException(String mensagem) {
        super(mensagem);
    }
}
