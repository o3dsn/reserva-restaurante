package br.com.fiap.reservarestaurante.application.usecases.avaliacao.delete;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class DefaultAvaliacaoDeleteUseCaseTest {

  AutoCloseable openMocks;
  private AvaliacaoDeleteUseCase avaliacaoDeleteUseCase;
  @Mock private AvaliacaoRepository avaliacaoRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    avaliacaoDeleteUseCase = new DefaultAvaliacaoDeleteUseCase(avaliacaoRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirExcluirAvaliacao() {
    var avaliacao = AvaliacaoHelper.gerarAvaliacao("ad23b448-3880-4fc4-ac24-d9aea45406ed");

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class)))
        .thenReturn(Optional.of(avaliacao));
    when(avaliacaoRepository.atualizar(any(Avaliacao.class))).thenAnswer(i -> i.getArgument(0));

    avaliacaoDeleteUseCase.execute(avaliacao.getId().value());

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
    verify(avaliacaoRepository, times(1)).atualizar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoExcluirAvaliacao_QueNaoExiste() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var msgErro = "Avaliação com ID %s não encontrado.".formatted(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> avaliacaoDeleteUseCase.execute(id))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
    verify(avaliacaoRepository, never()).atualizar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoExcluirAvaliacao_JaExcluida() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var msgErro = "Avaliação com ID %s já deletada.".formatted(id);
    var avaliacao = AvaliacaoHelper.gerarAvaliacaoExcluida(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class)))
        .thenReturn(Optional.of(avaliacao));

    assertThatThrownBy(() -> avaliacaoDeleteUseCase.execute(id))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
    verify(avaliacaoRepository, never()).atualizar(any(Avaliacao.class));
  }
}
