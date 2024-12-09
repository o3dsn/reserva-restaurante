package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

class DefaultAvaliacaoGetByIdUseCaseTest {

  AutoCloseable openMocks;
  private AvaliacaoGetByIdUseCase avaliacaoGetByIdUseCase;
  @Mock private AvaliacaoRepository avaliacaoRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    avaliacaoGetByIdUseCase = new DefaultAvaliacaoGetByIdUseCase(avaliacaoRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarAvaliacaoPorId() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var avaliacao = AvaliacaoHelper.gerarAvaliacao(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class)))
        .thenReturn(Optional.of(avaliacao));

    var output = avaliacaoGetByIdUseCase.execute(id);

    assertThat(output).extracting(AvaliacaoGetByIdUseCaseOutput::id).isEqualTo(avaliacao.getId());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::reservaId)
        .isEqualTo(avaliacao.getReservaId());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::usuarioId)
        .isEqualTo(avaliacao.getUsuarioId());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::criacao)
        .isEqualTo(avaliacao.getCriacao());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::alteracao)
        .isEqualTo(avaliacao.getAlteracao());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::exclusao)
        .isEqualTo(avaliacao.getExclusao());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::ativo)
        .isEqualTo(avaliacao.isAtivo());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::comentario)
        .isEqualTo(avaliacao.getComentario());
    assertThat(output)
        .extracting(AvaliacaoGetByIdUseCaseOutput::nota)
        .isEqualTo(avaliacao.getNota());

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
  }

  @Test
  void deveGerarExcecao_QuandoRecuperarAvaliacaoPorId_QueNaoExiste() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var msgErro = "Avaliação com ID %s não encontrado.".formatted(id);

    when(avaliacaoRepository.buscarPorId(any(AvaliacaoId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> avaliacaoGetByIdUseCase.execute(id))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(avaliacaoRepository, times(1)).buscarPorId(any(AvaliacaoId.class));
  }
}
