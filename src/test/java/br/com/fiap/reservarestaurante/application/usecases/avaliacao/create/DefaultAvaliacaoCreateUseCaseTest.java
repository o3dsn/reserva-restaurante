package br.com.fiap.reservarestaurante.application.usecases.avaliacao.create;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;

import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

class DefaultAvaliacaoCreateUseCaseTest {

  AutoCloseable openMocks;
  private AvaliacaoCreateUseCase avaliacaoCreateUseCase;
  @Mock private AvaliacaoRepository avaliacaoRepository;
  @Mock private ReservaGetByIdUseCase reservaGetByIdUseCase;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    avaliacaoCreateUseCase =
        new DefaultAvaliacaoCreateUseCase(avaliacaoRepository, reservaGetByIdUseCase);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  private ReservaGetByIdUseCaseOutput gerarReservaFinalizadaComUsuarioDiferente() {
    return new ReservaGetByIdUseCaseOutput(
            ReservaId.from(AvaliacaoHelper.RESERVA_ID),
            AvaliacaoHelper.RESTAURANTE_ID,
            "b03269bb-e8e8-4168-9a39-01f1d7a6076a",
            ReservaDTO.StatusEnum.FINALIZADA,
            null,
            Instant.parse("2024-12-02T10:15:30.00Z"),
            Instant.parse("2024-12-01T06:10:30.100Z"),
            Instant.parse("2024-12-01T06:10:30.100Z"));
  }

  @Test
  void devePermitirCriarAvaliacao() {
    var input = AvaliacaoHelper.gerarAvaliacaoCreateUseCaseInput();
    var avaliacao = AvaliacaoHelper.gerarAvaliacao("0a3fc31d-ec07-43a7-a637-68f32172978b");
    var reserva = ReservaHelper.gerarReservaFinalizada();

    when(reservaGetByIdUseCase.execute(any(String.class))).thenReturn(reserva);
    when(avaliacaoRepository.buscarPorIdReserva(any(String.class))).thenReturn(Optional.empty());
    when(avaliacaoRepository.criar(any(Avaliacao.class))).thenReturn(avaliacao);

    var output = avaliacaoCreateUseCase.execute(input);

    assertThat(output).isInstanceOf(AvaliacaoCreateUseCaseOutput.class).isNotNull();

    assertThat(output).extracting(AvaliacaoCreateUseCaseOutput::id).isEqualTo(avaliacao.getId());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::reservaId)
        .isEqualTo(avaliacao.getReservaId());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::usuarioId)
        .isEqualTo(avaliacao.getUsuarioId());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::criacao)
        .isEqualTo(avaliacao.getCriacao());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::alteracao)
        .isEqualTo(avaliacao.getAlteracao());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::exclusao)
        .isEqualTo(avaliacao.getExclusao());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::ativo)
        .isEqualTo(avaliacao.isAtivo());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::comentario)
        .isEqualTo(avaliacao.getComentario());
    assertThat(output)
        .extracting(AvaliacaoCreateUseCaseOutput::nota)
        .isEqualTo(avaliacao.getNota());

    verify(reservaGetByIdUseCase, times(1)).execute(any(String.class));
    verify(avaliacaoRepository, times(1)).buscarPorIdReserva(any(String.class));
    verify(avaliacaoRepository, times(1)).criar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoCriarAvaliacao_ComReservaNaoFinalizada() {
    var input = AvaliacaoHelper.gerarAvaliacaoCreateUseCaseInput();
    var reserva = ReservaHelper.gerarReservaConfirmada();

    when(reservaGetByIdUseCase.execute(any(String.class))).thenReturn(reserva);

    assertThatThrownBy(() -> avaliacaoCreateUseCase.execute(input))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage("Reserva ainda não finalizada")
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(reservaGetByIdUseCase, times(1)).execute(any(String.class));
    verify(avaliacaoRepository, never()).buscarPorIdReserva(any(String.class));
    verify(avaliacaoRepository, never()).criar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoCriarAvaliacao_ComUsuarioDiferenteDaReserva() {
    var input = AvaliacaoHelper.gerarAvaliacaoCreateUseCaseInput();
    var reserva = gerarReservaFinalizadaComUsuarioDiferente();

    when(reservaGetByIdUseCase.execute(any(String.class))).thenReturn(reserva);

    assertThatThrownBy(() -> avaliacaoCreateUseCase.execute(input))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage("Usuario não pode fazer avaliação para essa reserva")
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(reservaGetByIdUseCase, times(1)).execute(any(String.class));
    verify(avaliacaoRepository, never()).buscarPorIdReserva(any(String.class));
    verify(avaliacaoRepository, never()).criar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoCriarAvaliacao_ComTempoExcedido() {
    var input = AvaliacaoHelper.gerarAvaliacaoCreateUseCaseInput();
    var reserva = ReservaHelper.gerarReservaFinalizadaInvalida();

    when(reservaGetByIdUseCase.execute(any(String.class))).thenReturn(reserva);

    assertThatThrownBy(() -> avaliacaoCreateUseCase.execute(input))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage("Tempo limite para realizar avaliação alcançado")
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(reservaGetByIdUseCase, times(1)).execute(any(String.class));
    verify(avaliacaoRepository, times(1)).buscarPorIdReserva(any(String.class));
    verify(avaliacaoRepository, never()).criar(any(Avaliacao.class));
  }

  @Test
  void deveGerarExcecao_QuandoCriarAvaliacao_ComAvaliacaoJaRegistrada() {
    var input = AvaliacaoHelper.gerarAvaliacaoCreateUseCaseInput();
    var avaliacao = AvaliacaoHelper.gerarAvaliacao("0a3fc31d-ec07-43a7-a637-68f32172978b");
    var reserva = ReservaHelper.gerarReservaFinalizada();

    when(reservaGetByIdUseCase.execute(any(String.class))).thenReturn(reserva);
    when(avaliacaoRepository.buscarPorIdReserva(any(String.class)))
        .thenReturn(Optional.of(avaliacao));

    assertThatThrownBy(() -> avaliacaoCreateUseCase.execute(input))
        .isInstanceOf(AvaliacaoException.class)
        .hasMessage("Avaliação já registrada para essa reserva")
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(reservaGetByIdUseCase, times(1)).execute(any(String.class));
    verify(avaliacaoRepository, times(1)).buscarPorIdReserva(any(String.class));
    verify(avaliacaoRepository, never()).criar(any(Avaliacao.class));
  }
}
