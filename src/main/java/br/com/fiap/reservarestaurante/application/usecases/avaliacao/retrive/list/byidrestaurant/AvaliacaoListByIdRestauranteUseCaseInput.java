package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;

public record AvaliacaoListByIdRestauranteUseCaseInput(Page page, String restauranteId) {}
