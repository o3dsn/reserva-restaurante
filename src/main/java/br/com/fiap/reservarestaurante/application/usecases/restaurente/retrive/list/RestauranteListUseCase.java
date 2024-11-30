package br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.usecases.UseCase;

public abstract class RestauranteListUseCase
    extends UseCase<RestauranteListUseCaseInput, Pagination<RestauranteListUseCaseOutput>> {}
