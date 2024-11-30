package br.com.fiap.reservarestaurante.application.domain.reserva;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public record ReservaId(String value) {
    public static ReservaId from(String value) {
        try {
            return new ReservaId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException e) {
            throw new AvaliacaoException("Reserva com ID %s não é valido.".formatted(value), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}