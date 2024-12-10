package br.com.fiap.reservarestaurante.application.usecases.avaliacao.create;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import java.math.BigDecimal;
import java.time.Instant;

public record AvaliacaoCreateUseCaseOutput(
    AvaliacaoId id,
    String reservaId,
    String usuarioId,
    Instant criacao,
    Instant alteracao,
    Instant exclusao,
    boolean ativo,
    String comentario,
    BigDecimal nota) {

  public static AvaliacaoCreateUseCaseOutput from(final Avaliacao avaliacao) {
    return new AvaliacaoCreateUseCaseOutput(
        avaliacao.getId(),
        avaliacao.getReservaId(),
        avaliacao.getUsuarioId(),
        avaliacao.getCriacao(),
        avaliacao.getAlteracao(),
        avaliacao.getExclusao(),
        avaliacao.isAtivo(),
        avaliacao.getComentario(),
        avaliacao.getNota());
  }
}
