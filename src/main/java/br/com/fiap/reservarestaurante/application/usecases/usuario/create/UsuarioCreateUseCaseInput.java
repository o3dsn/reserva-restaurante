package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;

public record UsuarioCreateUseCaseInput(
        String id,
        String nome,
        String email,
        String senha) {
}
