package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.usecases.UseCase;

public abstract class AvaliacaoListUseCase
    extends UseCase<AvaliacaoListUseCaseInput, Pagination<AvaliacaoListUseCaseOutput>> {}
