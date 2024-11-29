package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import java.math.BigDecimal;
import java.time.Instant;

public record AvaliacaoListUseCaseOutput(
    AvaliacaoId id,
    String reservaId,
    String usuarioId,
    Instant criacao,
    Instant alteracao,
    Instant exclusao,
    boolean ativo,
    String comentario,
    BigDecimal nota) {

  public static AvaliacaoListUseCaseOutput from(final Avaliacao avaliacao) {
    return new AvaliacaoListUseCaseOutput(
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
