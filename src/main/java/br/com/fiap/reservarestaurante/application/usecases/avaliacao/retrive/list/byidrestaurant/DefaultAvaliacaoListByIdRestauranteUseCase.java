package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultAvaliacaoListByIdRestauranteUseCase
    extends AvaliacaoListByIdRestauranteUseCase {

  private final AvaliacaoRepository avaliacaoRepository;

  @Override
  public Pagination<AvaliacaoListByIdRestauranteUseCaseOutput> execute(
      final AvaliacaoListByIdRestauranteUseCaseInput input) {
    return avaliacaoRepository
        .buscarPorIdRestaurante(input.page(), input.restauranteId())
        .mapItems(AvaliacaoListByIdRestauranteUseCaseOutput::from);
  }
}
