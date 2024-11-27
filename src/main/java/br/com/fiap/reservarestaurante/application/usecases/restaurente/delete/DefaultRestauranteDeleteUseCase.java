package br.com.fiap.reservarestaurante.application.usecases.restaurente.delete;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultRestauranteDeleteUseCase extends RestauranteDeleteUseCase {

  private final RestauranteRepository restauranteRepository;

  @Override
  public void execute(String id) {
    RestauranteId restauranteId = new RestauranteId(id);
    Restaurante restaurante =
        restauranteRepository
            .buscarPorId(restauranteId)
            .orElseThrow(
                () -> new RestauranteException("Restaurante não encontrado", HttpStatus.NOT_FOUND));

    restauranteRepository.deletarRestaurante(restaurante);
  }
}
