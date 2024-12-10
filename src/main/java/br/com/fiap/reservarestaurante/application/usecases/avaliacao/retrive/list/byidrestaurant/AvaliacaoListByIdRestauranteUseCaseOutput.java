package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import java.math.BigDecimal;
import java.time.Instant;

public record AvaliacaoListByIdRestauranteUseCaseOutput(
    AvaliacaoId id,
    String reservaId,
    String usuarioId,
    Instant criacao,
    Instant alteracao,
    Instant exclusao,
    boolean ativo,
    String comentario,
    BigDecimal nota) {

  public static AvaliacaoListByIdRestauranteUseCaseOutput from(final Avaliacao avaliacao) {
    return new AvaliacaoListByIdRestauranteUseCaseOutput(
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
