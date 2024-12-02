package br.com.fiap.reservarestaurante.application.domain.usuario;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public record UsuarioId(String value) {


    public static UsuarioId from(String value) {
        try {
            return new UsuarioId(UUID.fromString(value).toString());
        } catch (IllegalArgumentException e) {
            throw new AvaliacaoException("Usuario com ID %s não é valido.".formatted(value), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public String toString() {
        return value;
    }


}
