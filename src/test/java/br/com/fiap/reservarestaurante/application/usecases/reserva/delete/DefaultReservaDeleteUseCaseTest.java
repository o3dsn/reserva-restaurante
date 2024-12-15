package br.com.fiap.reservarestaurante.application.usecases.reserva.delete;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

public class DefaultReservaDeleteUseCaseTest {
  AutoCloseable openMocks;
  private ReservaDeleteUseCase reservaDeleteUseCase;
  @Mock private ReservaRepository reservaRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    reservaDeleteUseCase = new DefaultReservaDeleteUseCase(reservaRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirExcluirReserva() {
    var avaliacao = ReservaHelper.gerarReserva("ad23b448-3880-4fc4-ac24-d9aea45406ed");

    when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.of(avaliacao));
    when(reservaRepository.atualizar(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

    reservaDeleteUseCase.execute(avaliacao.getId().value());

    verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
    verify(reservaRepository, times(1)).atualizar(any(Reserva.class));
  }

  @Test
  void deveGerarExcecao_QuandoExcluirReserva_QueNaoExiste() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var msgErro = "Reserva com ID %s não encontrado.".formatted(id);

    when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> reservaDeleteUseCase.execute(id))
        .isInstanceOf(ReservaException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
    verify(reservaRepository, never()).atualizar(any(Reserva.class));
  }

  @Test
  void deveGerarExcecao_QuandoExcluirReserva_JaExcluida() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var msgErro = "Reserva com ID %s já cancelada.".formatted(id);
    var reserva = ReservaHelper.gerarReservaExcluida(id);

    when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.of(reserva));

    assertThatThrownBy(() -> reservaDeleteUseCase.execute(id))
        .isInstanceOf(AvaliacaoException.class) // Altere aqui para AvaliacaoException
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.CONFLICT);

    verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
    verify(reservaRepository, never()).atualizar(any(Reserva.class));
  }
}
