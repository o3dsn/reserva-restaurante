package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DefaultAvaliacaoListUseCaseTest {

  AutoCloseable openMocks;
  private AvaliacaoListUseCase avaliacaoListUseCase;
  @Mock private AvaliacaoRepository avaliacaoRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    avaliacaoListUseCase = new DefaultAvaliacaoListUseCase(avaliacaoRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarTodasAvaliacoes() {
    var input = AvaliacaoHelper.gerarAvaliacaoListUseCaseInput();

    var avaliacao1 = AvaliacaoHelper.gerarAvaliacao("3e1d00ed-ece2-4311-a4df-d0af2012f548");
    var avaliacao2 = AvaliacaoHelper.gerarAvaliacao("7657063c-eca6-4f33-a9cc-b012db6de0b3");
    var pagination =
        new Pagination<>(
            input.page().currentPage(),
            input.page().perPage(),
            2L,
            List.of(avaliacao1, avaliacao2));

    when(avaliacaoRepository.buscarTudo(any(Page.class), anyBoolean())).thenReturn(pagination);

    var output = avaliacaoListUseCase.execute(input);

    assertThat(output).isNotNull().isInstanceOf(Pagination.class);

    Pagination<AvaliacaoListUseCaseOutput> outputPagination = new Pagination<>(
            input.page().currentPage(),
            input.page().perPage(),
            2L,
            List.of(AvaliacaoListUseCaseOutput.from(avaliacao1), AvaliacaoListUseCaseOutput.from(avaliacao2)));
    assertThat(output).extracting(Pagination::currentPage).isEqualTo(pagination.currentPage());
    assertThat(output).extracting(Pagination::perPage).isEqualTo(pagination.perPage());
    assertThat(output).extracting(Pagination::total).isEqualTo(pagination.total());
    assertThat(output).extracting(Pagination::items).isEqualTo(outputPagination.items());

    verify(avaliacaoRepository, times(1)).buscarTudo(input.page(), true);
  }

  @Test
  void devePermitirRecuperarTodasAvaliacoes_QuandoNaoTiverNadaAListar() {
    var input = AvaliacaoHelper.gerarAvaliacaoListUseCaseInput();
    Pagination<Avaliacao> pagination =
        new Pagination<>(input.page().currentPage(), input.page().perPage(), 0L, List.of());

    when(avaliacaoRepository.buscarTudo(any(Page.class), anyBoolean())).thenReturn(pagination);

    var output = avaliacaoListUseCase.execute(input);

    assertThat(output).isNotNull().isInstanceOf(Pagination.class);

    assertThat(output).extracting(Pagination::currentPage).isEqualTo(pagination.currentPage());
    assertThat(output).extracting(Pagination::perPage).isEqualTo(pagination.perPage());
    assertThat(output).extracting(Pagination::total).isEqualTo(pagination.total());
    assertThat(output).extracting(Pagination::items).isEqualTo(pagination.items());

    verify(avaliacaoRepository, times(1)).buscarTudo(input.page(), true);
  }
}
