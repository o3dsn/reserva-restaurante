package br.com.fiap.reservarestaurante.application.usecases.reserva.create;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DefaultReservaCreateUseCaseTest {
    AutoCloseable openMocks;
    private ReservaCreateUseCase reservaCreateUseCase;
    @Mock
    private ReservaRepository reservaRepository;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        reservaCreateUseCase = new DefaultReservaCreateUseCase(reservaRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCriarReserva() {
        var input = ReservaHelper.gerarReservaCreateUseCaseInput();
        var reserva = ReservaHelper.gerarReserva("0a3fc31d-ec07-43a7-a637-68f32172978b");

        when(reservaRepository.criar(any(Reserva.class))).thenReturn(reserva);

        var output = reservaCreateUseCase.execute(input);

        assertThat(output).isInstanceOf(ReservaCreateUseCaseOutput.class).isNotNull();
        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(reserva.getId());
        assertThat(output.restauranteId()).isEqualTo(reserva.getRestauranteId());
        assertThat(output.usuarioId()).isEqualTo(reserva.getUsuarioId());
        assertThat(output.comentario()).isEqualTo(reserva.getComentario());
        assertThat(output.status()).isEqualTo(reserva.getStatus());

        verify(reservaRepository, times(1)).criar(any(Reserva.class));
    }
}
