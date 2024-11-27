package br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get;

import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultRestauranteGetByIdUseCase extends RestauranteGetByIdUseCase {

  private final RestauranteRepository repository;

  @Override
  public RestauranteGetByIdUseCaseOutput execute(String id) {
    RestauranteId restauranteId = new RestauranteId(id);
    return repository
        .buscarPorId(restauranteId)
        .map(RestauranteGetByIdUseCaseOutput::from)
        .orElseThrow(
            () -> new RestauranteException("Restaurante não encontrado", HttpStatus.NOT_FOUND));
  }
}
