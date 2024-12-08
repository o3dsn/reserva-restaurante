package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import java.math.BigDecimal;

public record NotaRestauranteGetByIdUseCaseOutput(
    String restauranteId, Long avaliacoes, BigDecimal nota) {

  public static NotaRestauranteGetByIdUseCaseOutput from(final NotaRestaurante notaRestaurante) {
    return new NotaRestauranteGetByIdUseCaseOutput(
        notaRestaurante.restauranteId(), notaRestaurante.avaliacoes(), notaRestaurante.nota());
  }
}
