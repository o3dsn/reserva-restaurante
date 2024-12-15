package br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultReservaListUseCaseTest {

  AutoCloseable openMocks;

  private ReservaListUseCase reservaListUseCase;

  @Mock private ReservaRepository reservaRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    reservaListUseCase = new DefaultReservaListUseCase(reservaRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarTodasReservas() {
    var input = ReservaHelper.gerarReservaListUseCaseInput();

    var reserva1 = ReservaHelper.gerarReserva("3e1d00ed-ece2-4311-a4df-d0af2012f548");
    var reserva2 = ReservaHelper.gerarReserva("7657063c-eca6-4f33-a9cc-b012db6de0b3");
    var pagination =
        new Pagination<>(
            input.page().currentPage(), input.page().perPage(), 2L, List.of(reserva1, reserva2));

    when(reservaRepository.buscarTudo(any(Page.class))).thenReturn(pagination);

    var output = reservaListUseCase.execute(input);

    assertThat(output).isNotNull().isInstanceOf(Pagination.class);

    Pagination<ReservaListUseCaseOutput> outputPagination =
        new Pagination<>(
            input.page().currentPage(),
            input.page().perPage(),
            2L,
            List.of(
                ReservaListUseCaseOutput.from(reserva1), ReservaListUseCaseOutput.from(reserva2)));
    assertThat(output).extracting(Pagination::currentPage).isEqualTo(pagination.currentPage());
    assertThat(output).extracting(Pagination::perPage).isEqualTo(pagination.perPage());
    assertThat(output).extracting(Pagination::total).isEqualTo(pagination.total());
    assertThat(output).extracting(Pagination::items).isEqualTo(outputPagination.items());

    verify(reservaRepository, times(1)).buscarTudo(input.page());
  }

  @Test
  void devePermitirRecuperarTodasReserva_QuandoNaoTiverNadaAListar() {
    var input = ReservaHelper.gerarReservaListUseCaseInput();
    Pagination<Reserva> pagination =
        new Pagination<>(input.page().currentPage(), input.page().perPage(), 0L, List.of());

    when(reservaRepository.buscarTudo(any(Page.class))).thenReturn(pagination);

    var output = reservaListUseCase.execute(input);

    assertThat(output).isNotNull().isInstanceOf(Pagination.class);

    assertThat(output).extracting(Pagination::currentPage).isEqualTo(pagination.currentPage());
    assertThat(output).extracting(Pagination::perPage).isEqualTo(pagination.perPage());
    assertThat(output).extracting(Pagination::total).isEqualTo(pagination.total());
    assertThat(output).extracting(Pagination::items).isEqualTo(pagination.items());

    verify(reservaRepository, times(1)).buscarTudo(input.page());
  }
}
