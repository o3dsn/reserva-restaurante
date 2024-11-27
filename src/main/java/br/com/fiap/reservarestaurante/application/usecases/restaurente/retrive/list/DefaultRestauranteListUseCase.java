package br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRestauranteListUseCase extends RestauranteListUseCase {

  private final RestauranteRepository restauranteRepository;

  @Override
  public Pagination<RestauranteListUseCaseOutput> execute(final RestauranteListUseCaseInput input) {
    return restauranteRepository
        .buscarTudo(input.page())
        .mapItems(RestauranteListUseCaseOutput::from);
  }
}
