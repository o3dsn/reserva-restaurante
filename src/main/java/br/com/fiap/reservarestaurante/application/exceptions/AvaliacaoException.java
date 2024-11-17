package br.com.fiap.reservarestaurante.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AvaliacaoException extends RuntimeException {
    private final HttpStatus status;
    public AvaliacaoException(String mensagem, HttpStatus status) {
        super(mensagem);
        this.status = status;
    }
}
