package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

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

class DefaultNotaRestauranteGetByIdUseCaseTest {

  AutoCloseable openMocks;
  private NotaRestauranteGetByIdUseCase notaRestauranteGetByIdUseCase;
  @Mock private AvaliacaoRepository avaliacaoRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    notaRestauranteGetByIdUseCase = new DefaultNotaRestauranteGetByIdUseCase(avaliacaoRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarNotaRestauranteGetById() {
    var notaRestaurante = AvaliacaoHelper.gerarNotaRestaurante();

    when(avaliacaoRepository.buscarNotaRestaurante(any(String.class)))
        .thenReturn(Optional.of(notaRestaurante));

    var output = notaRestauranteGetByIdUseCase.execute(AvaliacaoHelper.RESTAURANTE_ID);

    assertThat(output).isNotNull().isInstanceOf(NotaRestauranteGetByIdUseCaseOutput.class);

    assertThat(output)
        .extracting(NotaRestauranteGetByIdUseCaseOutput::restauranteId)
        .isEqualTo(notaRestaurante.restauranteId());
    assertThat(output)
        .extracting(NotaRestauranteGetByIdUseCaseOutput::avaliacoes)
        .isEqualTo(notaRestaurante.avaliacoes());
    assertThat(output)
        .extracting(NotaRestauranteGetByIdUseCaseOutput::nota)
        .isEqualTo(notaRestaurante.nota());

    verify(avaliacaoRepository, times(1)).buscarNotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID);
  }

  @Test
  void deveGerarExcecao_QuandoRecuperarNotaRestauranteGetById_QueNaoExiste() {
    var msgErro =
        "Nota para o restaurante com ID %s não encontrado."
            .formatted(AvaliacaoHelper.RESTAURANTE_ID);

    when(avaliacaoRepository.buscarNotaRestaurante(any(String.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> notaRestauranteGetByIdUseCase.execute(AvaliacaoHelper.RESTAURANTE_ID))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(avaliacaoRepository, times(1)).buscarNotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID);
  }
}
