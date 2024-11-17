package br.com.fiap.reservarestaurante.application.domain.avaliacao;

import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public record AvaliacaoId(String value) {

    public static AvaliacaoId from(String value) {
        try {
            return new AvaliacaoId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException e) {
            throw new AvaliacaoException("Avaliação com ID %s não é valido.".formatted(value), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String toString() {
        return value;
    }

}
