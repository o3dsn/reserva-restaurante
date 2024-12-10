package br.com.fiap.reservarestaurante.application.usecases.restaurante.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.DefaultRestauranteCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultRestauranteCreateUseCaseTest {

  AutoCloseable openMocks;

  private RestauranteCreateUseCase restauranteCreateUseCase;

  @Mock private RestauranteRepository restauranteRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    restauranteCreateUseCase = new DefaultRestauranteCreateUseCase(restauranteRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirCriarRestaurante() {
    var input = RestauranteHelper.gerarRestauranteCreateUseCaseInput();
    var restaurante = RestauranteHelper.gerarRestaurante("0a3fc31d-ec07-43a7-a637-68f32172978b");

    when(restauranteRepository.criar(any(Restaurante.class))).thenReturn(restaurante);

    var output = restauranteCreateUseCase.execute(input);

    assertThat(output).isInstanceOf(RestauranteCreateUseCaseOutput.class).isNotNull();

    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::id)
        .isEqualTo(restaurante.getId());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::nome)
        .isEqualTo(restaurante.getNome());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::descricao)
        .isEqualTo(restaurante.getDescricao());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::endereco)
        .isEqualTo(restaurante.getEndereco());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::cidade)
        .isEqualTo(restaurante.getCidade());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::estado)
        .isEqualTo(restaurante.getEstado());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::bairro)
        .isEqualTo(restaurante.getBairro());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::tipoCozinhaId)
        .isEqualTo(restaurante.getTipoCozinhaId());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::faixaPreco)
        .isEqualTo(restaurante.getFaixaPreco());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::telefone)
        .isEqualTo(restaurante.getTelefone());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::email)
        .isEqualTo(restaurante.getEmail());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::avaliacaoMedia)
        .isEqualTo(restaurante.getAvaliacaoMedia());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::avaliacaoTotal)
        .isEqualTo(restaurante.getAvaliacaoTotal());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::horarioAbertura)
        .isEqualTo(restaurante.getHorarioAbertura());
    assertThat(output)
        .extracting(RestauranteCreateUseCaseOutput::horarioFechamento)
        .isEqualTo(restaurante.getHorarioFechamento());

    verify(restauranteRepository, times(1)).criar(any(Restaurante.class));
  }
}
