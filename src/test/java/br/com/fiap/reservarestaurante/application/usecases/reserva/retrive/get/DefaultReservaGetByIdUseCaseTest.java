package br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
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

public class DefaultReservaGetByIdUseCaseTest {
  AutoCloseable openMocks;
  private ReservaGetByIdUseCase reservaGetByIdUseCase;
  @Mock private ReservaRepository reservaRepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    reservaGetByIdUseCase = new DefaultReservaGetByIdUseCase(reservaRepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void devePermitirRecuperarReservaPorId() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var reserva = ReservaHelper.gerarReserva(id);

    when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.of(reserva));

    var output = reservaGetByIdUseCase.execute(id);

    assertThat(output).extracting(ReservaGetByIdUseCaseOutput::id).isEqualTo(reserva.getId());
    assertThat(output.id()).isEqualTo(reserva.getId());
    assertThat(output.restauranteId()).isEqualTo(reserva.getRestauranteId());
    assertThat(output.usuarioId()).isEqualTo(reserva.getUsuarioId());
    assertThat(output.comentario()).isEqualTo(reserva.getComentario());
    assertThat(output.status()).isEqualTo(reserva.getStatus());

    verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
  }

  @Test
  void deveGerarExcecao_QuandoRecuperarReservaPorId_QueNaoExiste() {
    var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
    var msgErro = "Reserva com ID %s não encontrado.".formatted(id);

    when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.empty());

    assertThatThrownBy(() -> reservaGetByIdUseCase.execute(id))
        .isInstanceOf(ReservaException.class)
        .hasMessage(msgErro)
        .extracting("status")
        .isEqualTo(HttpStatus.NOT_FOUND);

    verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
  }
}
