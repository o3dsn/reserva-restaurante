package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

public record UsuarioCreateUseCaseInput(
        String usuarioId,
        String nome,
        String email,
        String senha) {
}
