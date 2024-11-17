package br.com.fiap.reservarestaurante.application.usecases.avaliacao.create;

public record AvaliacaoCreateUseCaseInput(String reservaId, String usuarioId, String comentario, float nota) {}
