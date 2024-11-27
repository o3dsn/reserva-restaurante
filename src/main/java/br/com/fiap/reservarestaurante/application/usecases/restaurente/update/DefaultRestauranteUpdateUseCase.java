package br.com.fiap.reservarestaurante.application.usecases.restaurente.update;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultRestauranteUpdateUseCase extends RestauranteUpdateUseCase {

  private final RestauranteRepository repository;

  @Override
  public RestauranteUpdateUseCaseOutput execute(RestauranteUpdateUseCaseInput input) {
    RestauranteId restauranteId = new RestauranteId(input.id());
    Restaurante restaurante =
        repository
            .buscarPorId(restauranteId)
            .orElseThrow(
                () -> new RestauranteException("Restaurante não encontrado", HttpStatus.NOT_FOUND));

    return RestauranteUpdateUseCaseOutput.from(repository.atualizar(restaurante));
  }
}
