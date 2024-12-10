package br.com.fiap.reservarestaurante.application.usecases.restaurante.delete;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.delete.DefaultRestauranteDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.delete.RestauranteDeleteUseCase;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultRestauranteDeleteUseCaseTest {

  AutoCloseable openMocks;
  private RestauranteDeleteUseCase restauranteDeleteUseCase;

  @Mock private RestauranteRepository restauranteRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    restauranteDeleteUseCase = new DefaultRestauranteDeleteUseCase(restauranteRepository);
  }

  @Test
  void devePermitirExcluirRestaurante() {
    var restaurante = RestauranteHelper.gerarRestaurante("ad23b448-3880-4fc4-ac24-d9aea45406ed");

    when(restauranteRepository.buscarPorId(any(RestauranteId.class)))
        .thenReturn(Optional.of(restaurante));

    restauranteDeleteUseCase.execute(restaurante.getId().value());

    verify(restauranteRepository, times(1)).buscarPorId(any(RestauranteId.class));
  }
}
