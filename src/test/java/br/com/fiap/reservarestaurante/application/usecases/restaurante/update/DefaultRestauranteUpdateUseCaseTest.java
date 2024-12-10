package br.com.fiap.reservarestaurante.application.usecases.restaurante.update;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.DefaultRestauranteUpdateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCase;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class DefaultRestauranteUpdateUseCaseTest {

  AutoCloseable openMocks;
  private RestauranteUpdateUseCase restauranteUpdateUseCase;
  @Mock private RestauranteRepository restauranteRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    restauranteUpdateUseCase = new DefaultRestauranteUpdateUseCase(restauranteRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirAlterarRestaurante() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var input = RestauranteHelper.gerarRestauranteUpdateUseCaseInput(id);
    var restaurante = RestauranteHelper.gerarRestaurante(id);

    when(restauranteRepository.buscarPorId(any(RestauranteId.class)))
        .thenReturn(Optional.of(restaurante));
    when(restauranteRepository.atualizar(any(Restaurante.class))).thenAnswer(i -> i.getArgument(0));

    restauranteUpdateUseCase.execute(input);

    verify(restauranteRepository, times(1)).buscarPorId(any(RestauranteId.class));
    verify(restauranteRepository, times(1)).atualizar(any(Restaurante.class));
  }

  @Test
  void deveGerarExcecao_QuandoAlterarRestaurante_QueNaoExiste() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var input = RestauranteHelper.gerarRestauranteUpdateUseCaseInput(id);
    var msgErro = "Restaurante não encontrado";

    when(restauranteRepository.buscarPorId(any(RestauranteId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> restauranteUpdateUseCase.execute(input))
        .isInstanceOf(RestauranteException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(restauranteRepository, times(1)).buscarPorId(any(RestauranteId.class));
    verify(restauranteRepository, never()).atualizar(any(Restaurante.class));
  }
}
