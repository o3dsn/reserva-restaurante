package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;

public record UsuarioCreateUseCaseOutput(String id, String nome, String email) {

    public static UsuarioCreateUseCaseOutput from(final Usuario usuario) {
        return new UsuarioCreateUseCaseOutput(
                usuario.getId().toString(),
                usuario.getNome(),
                usuario.getEmail());
    }

}
