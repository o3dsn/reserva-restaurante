package br.com.fiap.reservarestaurante.application.usecases.restaurante.retrive.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.DefaultRestauranteListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultRestauranteListUseCaseTest {

  AutoCloseable openMocks;

  private RestauranteListUseCase restauranteListUseCase;

  @Mock private RestauranteRepository restauranteRepository;

  @BeforeEach
  public void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    restauranteListUseCase = new DefaultRestauranteListUseCase(restauranteRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarTodosRestaurantes() {
    var input = RestauranteHelper.gerarRestauranteListUseCaseInput();

    var restaurante1 = RestauranteHelper.gerarRestaurante("3e1d00ed-ece2-4311-a4df-d0af2012f548");
    var restaurante2 = RestauranteHelper.gerarRestaurante("7657063c-eca6-4f33-a9cc-b012db6de0b3");
    var pagination =
        new Pagination<>(
            input.page().currentPage(),
            input.page().perPage(),
            2L,
            List.of(restaurante1, restaurante2));

    when(restauranteRepository.buscarTudo(any(Page.class))).thenReturn(pagination);

    var output = restauranteListUseCase.execute(input);

    assertThat(output).isNotNull().isInstanceOf(Pagination.class);

    Pagination<RestauranteListUseCaseOutput> outputPagination =
        new Pagination<>(
            input.page().currentPage(),
            input.page().perPage(),
            2L,
            List.of(
                RestauranteListUseCaseOutput.from(restaurante1),
                RestauranteListUseCaseOutput.from(restaurante2)));

    assertThat(output).extracting(Pagination::currentPage).isEqualTo(pagination.currentPage());
    assertThat(output).extracting(Pagination::perPage).isEqualTo(pagination.perPage());
    assertThat(output).extracting(Pagination::total).isEqualTo(pagination.total());
    assertThat(output).extracting(Pagination::items).isEqualTo(outputPagination.items());

    verify(restauranteRepository, times(1)).buscarTudo(input.page());
  }

  @Test
  void devePermitirRecuperarTodosRestaurantes_QuandoNaoTiverNadaAListar() {
    var input = RestauranteHelper.gerarRestauranteListUseCaseInput();
    Pagination<Restaurante> pagination =
        new Pagination<>(input.page().currentPage(), input.page().perPage(), 0L, List.of());

    when(restauranteRepository.buscarTudo(any(Page.class))).thenReturn(pagination);

    var output = restauranteListUseCase.execute(input);

    assertThat(output).isNotNull().isInstanceOf(Pagination.class);

    assertThat(output).extracting(Pagination::currentPage).isEqualTo(pagination.currentPage());
    assertThat(output).extracting(Pagination::perPage).isEqualTo(pagination.perPage());
    assertThat(output).extracting(Pagination::total).isEqualTo(pagination.total());
    assertThat(output).extracting(Pagination::items).isEqualTo(pagination.items());

    verify(restauranteRepository, times(1)).buscarTudo(input.page());
  }
}
