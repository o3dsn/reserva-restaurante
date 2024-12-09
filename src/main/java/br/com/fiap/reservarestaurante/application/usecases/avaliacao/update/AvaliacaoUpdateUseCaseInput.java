package br.com.fiap.reservarestaurante.application.usecases.avaliacao.update;

import java.math.BigDecimal;

public record AvaliacaoUpdateUseCaseInput(String id, String comentario, BigDecimal nota) {}
