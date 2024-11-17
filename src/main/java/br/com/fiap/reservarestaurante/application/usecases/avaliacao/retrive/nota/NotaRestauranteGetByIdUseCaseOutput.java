package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;

public record NotaRestauranteGetByIdUseCaseOutput(
        String restauranteId,
        Long avaliacoes,
        double nota) {

    public static NotaRestauranteGetByIdUseCaseOutput from(final NotaRestaurante notaRestaurante) {
        return new NotaRestauranteGetByIdUseCaseOutput(notaRestaurante.restauranteId(), notaRestaurante.avaliacoes(), notaRestaurante.nota());
    }

}
