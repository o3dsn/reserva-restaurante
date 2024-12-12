package br.com.fiap.reservarestaurante.application.domain.usuario;

import br.com.fiap.reservarestaurante.application.exceptions.UsuarioException;

import java.util.UUID;

public record UsuarioId(String value) {

    public static UsuarioId from(final String value) {
        try {
            return new UsuarioId(java.util.UUID.fromString(value).toString());
        } catch (IllegalArgumentException e) {
            throw new UsuarioException("Usuario com ID %s não é valido.".formatted(value), org.springframework.http.HttpStatus.BAD_REQUEST);
        }
    }

    public static UsuarioId generate() {
        return new UsuarioId(UUID.randomUUID().toString());
    }


    @Override
    public String toString() {
        return value;
    }
}
