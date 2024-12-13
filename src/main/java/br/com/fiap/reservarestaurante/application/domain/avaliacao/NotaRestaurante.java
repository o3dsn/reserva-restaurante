package br.com.fiap.reservarestaurante.application.domain.avaliacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record NotaRestaurante(String restauranteId, Long avaliacoes, BigDecimal nota) {

  public NotaRestaurante(String restauranteId, Long avaliacoes, BigDecimal nota) {
    this.restauranteId = restauranteId;
    this.avaliacoes = avaliacoes;
    this.nota = nota.setScale(1, RoundingMode.DOWN);
  }
}
