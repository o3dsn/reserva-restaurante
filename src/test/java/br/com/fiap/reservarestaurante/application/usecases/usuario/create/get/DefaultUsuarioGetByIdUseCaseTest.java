package br.com.fiap.reservarestaurante.application.usecases.usuario.create.get;

import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.exceptions.UsuarioException;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.DefaultRestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get.DefaultUsuarioGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get.UsuarioGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get.UsuarioGetByIdUseCaseOutPut;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import br.com.fiap.reservarestaurante.utils.UsuarioHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultUsuarioGetByIdUseCaseTest {

    AutoCloseable openMocks;

    private RestauranteGetByIdUseCase restauranteGetByIdUseCase;
    private UsuarioGetByIdUseCase usuarioGetByIdUseCase;
    @Mock
    private RestauranteRepository restauranteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;


    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        restauranteGetByIdUseCase = new DefaultRestauranteGetByIdUseCase(restauranteRepository);
        usuarioGetByIdUseCase = new DefaultUsuarioGetByIdUseCase(usuarioRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirRecuperarUsuarioPorId() {
        var id = "0a3fc31d-ec07-43a7-a637-68f32172978b";
        var usuario = UsuarioHelper.gerar(id);

        when(usuarioRepository.buscarPorId(any(UsuarioId.class)))
                .thenReturn(Optional.of(usuario));

        var output = usuarioGetByIdUseCase.execute(id);

        assertThat(output)
                .extracting(UsuarioGetByIdUseCaseOutPut::id)
                .isEqualTo(usuario.getId());
    }

    @Test
    void deveGerarExcecao_QuandoRecuperarUsuarioPorId_QueNaoExiste() {
        var id = "0a3fc31d-ec07-43a7-a637-68f32172978b";
        var msgErro = "Usuario com ID 0a3fc31d-ec07-43a7-a637-68f32172978b não encontrado.";

        when(usuarioRepository.buscarPorId(any(UsuarioId.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioGetByIdUseCase.execute(id))
                .isInstanceOf(UsuarioException.class)
                .hasMessage(msgErro)
                .extracting("status")
                .isEqualTo(HttpStatus.NOT_FOUND);

        verify(usuarioRepository, times(1)).buscarPorId(any(UsuarioId.class));
    }
}
