package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;

public record UsuarioCreateUseCaseOutput(UsuarioId id, String nome, String email) {

    public static UsuarioCreateUseCaseOutput from(final Usuario usuario) {

        return new UsuarioCreateUseCaseOutput(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail());
    }

}
