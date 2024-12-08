package br.com.fiap.reservarestaurante.application.usecases.avaliacao.create;

import java.math.BigDecimal;

public record AvaliacaoCreateUseCaseInput(
    String reservaId, String usuarioId, String comentario, BigDecimal nota) {}
