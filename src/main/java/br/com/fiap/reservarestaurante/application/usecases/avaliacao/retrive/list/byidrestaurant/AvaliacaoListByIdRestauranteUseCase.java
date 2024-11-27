package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.usecases.UseCase;

public abstract class AvaliacaoListByIdRestauranteUseCase
    extends UseCase<
        AvaliacaoListByIdRestauranteUseCaseInput,
        Pagination<AvaliacaoListByIdRestauranteUseCaseOutput>> {}
