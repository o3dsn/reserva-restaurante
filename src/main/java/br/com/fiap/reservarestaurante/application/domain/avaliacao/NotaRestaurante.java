package br.com.fiap.reservarestaurante.application.domain.avaliacao;

import br.com.fiap.reservarestaurante.application.utils.NumberUtils;

public record NotaRestaurante(String restauranteId, Long avaliacoes, double nota) {

    public NotaRestaurante(String restauranteId, Long avaliacoes, double nota) {
        this.restauranteId = restauranteId;
        this.avaliacoes = avaliacoes;
        this.nota = NumberUtils.roundToOneDecimalPlace(nota);
    }

}
