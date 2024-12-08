package br.com.fiap.reservarestaurante.application.usecases.avaliacao.update;

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

class DefaultAvaliacaoUpdateUseCaseTest {

  AutoCloseable openMocks;
  private AvaliacaoUpdateUseCase avaliacaoUpdateUseCase;
  @Mock private AvaliacaoRepository avaliacaoRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    avaliacaoUpdateUseCase = new DefaultAvaliacaoUpdateUseCase(avaliacaoRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirAlterarAvaliacao() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var input = AvaliacaoHelper.gerarAvaliacaoUpdateUseCaseInput(id);
    var avaliacao = AvaliacaoHelper.gerarAvaliacao(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class)))
        .thenReturn(Optional.of(avaliacao));
    when(avaliacaoRepository.atualizar(any(Avaliacao.class))).thenAnswer(i -> i.getArgument(0));

    avaliacaoUpdateUseCase.execute(input);

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
    verify(avaliacaoRepository, times(1)).atualizar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoAlterarAvaliacao_QueNaoExiste() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var input = AvaliacaoHelper.gerarAvaliacaoUpdateUseCaseInput(id);
    var msgErro = "Avaliação com ID %s não encontrado.".formatted(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> avaliacaoUpdateUseCase.execute(input))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
    verify(avaliacaoRepository, never()).atualizar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoAlterarAvaliacao_JaExcluida() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var input = AvaliacaoHelper.gerarAvaliacaoUpdateUseCaseInput(id);
    var avaliacao = AvaliacaoHelper.gerarAvaliacaoExcluida(id);
    var msgErro = "Avaliação com ID %s não disponível para atualização.".formatted(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class)))
        .thenReturn(Optional.of(avaliacao));

    assertThatThrownBy(() -> avaliacaoUpdateUseCase.execute(input))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
    verify(avaliacaoRepository, never()).atualizar(any(Avaliacao.class));
  }
}
