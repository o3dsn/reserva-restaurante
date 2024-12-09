package br.com.fiap.reservarestaurante.application.usecases.reserva.update;

import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultReservaUpdateUseCaseTest {
    AutoCloseable openMocks;
    private ReservaUpdateUseCase reservaUpdateUseCase;
    @Mock
    private ReservaRepository reservaRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        reservaUpdateUseCase = new DefaultReservaUpdateUseCase(reservaRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirAlterarReserva() {
        var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
        var input = ReservaHelper.gerarReservaUpdateUseCaseInput(id);
        var reserva = ReservaHelper.gerarReserva(id);

        when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.of(reserva));
        when(reservaRepository.atualizar(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        reservaUpdateUseCase.execute(input);

        verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
        verify(reservaRepository, times(1)).atualizar(any(Reserva.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarReserva_QueNaoExiste() {
        var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
        var input = ReservaHelper.gerarReservaUpdateUseCaseInput(id);
        var msgErro = "Reserva com ID %s não encontrado.".formatted(id);

        when(reservaRepository.buscarPorId(any(ReservaId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservaUpdateUseCase.execute(input))
                .isInstanceOf(ReservaException.class)
                .hasMessage(msgErro)
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
        verify(reservaRepository, never()).atualizar(any(Reserva.class));
    }

    @Test
    void deveGerarExcecao_QuandoAlterarReserva_JaExcluida() {
        var id = "ad23b448-3880-4fc4-ac24-d9aea45406ed";
        var input = ReservaHelper.gerarReservaUpdateUseCaseInput(id);
        var reserva = ReservaHelper.gerarReservaExcluida(id);
        var msgErro = "Reserva com ID %s já cancelada.".formatted(id);

        when(reservaRepository.buscarPorId(any(ReservaId.class)))
                .thenReturn(Optional.of(reserva));

        assertThatThrownBy(() -> reservaUpdateUseCase.execute(input))
                .isInstanceOf(ReservaException.class)
                .hasMessage(msgErro)
                .extracting("status")
                .isEqualTo(HttpStatus.CONFLICT);

        verify(reservaRepository, times(1)).buscarPorId(any(ReservaId.class));
        verify(reservaRepository, never()).atualizar(any(Reserva.class));
    }
}
