package br.com.fiap.reservarestaurante.application.usecases.restaurante.retrive.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.DefaultRestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class DefaultRestauranteGetByIdUseCaseTest {

  AutoCloseable openMocks;

  private RestauranteGetByIdUseCase restauranteGetByIdUseCase;

  @Mock private RestauranteRepository restauranteRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    restauranteGetByIdUseCase = new DefaultRestauranteGetByIdUseCase(restauranteRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarRestaurantePorId() {
    var id = "0a3fc31d-ec07-43a7-a637-68f32172978b";
    var restaurante = RestauranteHelper.gerarRestaurante(id);

    when(restauranteRepository.buscarPorId(any(RestauranteId.class)))
        .thenReturn(Optional.of(restaurante));

    var output = restauranteGetByIdUseCase.execute(id);

    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::id)
        .isEqualTo(restaurante.getId());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::nome)
        .isEqualTo(restaurante.getNome());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::descricao)
        .isEqualTo(restaurante.getDescricao());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::endereco)
        .isEqualTo(restaurante.getEndereco());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::cidade)
        .isEqualTo(restaurante.getCidade());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::estado)
        .isEqualTo(restaurante.getEstado());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::bairro)
        .isEqualTo(restaurante.getBairro());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::tipoCozinhaId)
        .isEqualTo(restaurante.getTipoCozinhaId());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::faixaPreco)
        .isEqualTo(restaurante.getFaixaPreco());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::telefone)
        .isEqualTo(restaurante.getTelefone());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::email)
        .isEqualTo(restaurante.getEmail());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::avaliacaoMedia)
        .isEqualTo(restaurante.getAvaliacaoMedia());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::avaliacaoTotal)
        .isEqualTo(restaurante.getAvaliacaoTotal());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::horarioAbertura)
        .isEqualTo(restaurante.getHorarioAbertura());
    assertThat(output)
        .extracting(RestauranteGetByIdUseCaseOutput::horarioFechamento)
        .isEqualTo(restaurante.getHorarioFechamento());

    verify(restauranteRepository, times(1)).buscarPorId(any(RestauranteId.class));
  }

  @Test
  void deveGerarExcecao_QuandoRecuperarRestaurantePorId_QueNaoExiste() {
    var id = "0a3fc31d-ec07-43a7-a637-68f32172978b";
    var msgErro = "Restaurante não encontrado";

    when(restauranteRepository.buscarPorId(any(RestauranteId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> restauranteGetByIdUseCase.execute(id))
        .isInstanceOf(RestauranteException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(restauranteRepository, times(1)).buscarPorId(any(RestauranteId.class));
  }
}
