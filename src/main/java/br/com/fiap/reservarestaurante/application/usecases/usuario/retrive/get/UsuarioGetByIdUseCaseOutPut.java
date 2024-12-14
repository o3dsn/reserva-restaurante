package br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;

public record UsuarioGetByIdUseCaseOutPut(
        UsuarioId id,
        String nome,
        String email) {

    public static UsuarioGetByIdUseCaseOutPut from(final Usuario usuario) {
        return new UsuarioGetByIdUseCaseOutPut(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail());
    }

}
