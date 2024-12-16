package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import br.com.fiap.reservarestaurante.utils.UsuarioHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultUsuarioCreateUseCaseTest {

    AutoCloseable openMocks;

    private UsuarioCreateUseCase usuarioCreateUseCase;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;


    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        usuarioCreateUseCase = new DefaultUsuarioCreateUseCase(usuarioRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirCriarUsuario() {
        var input = UsuarioHelper.gerarRestauranteCreateUseCaseInput();
        var usuario = UsuarioHelper.gerar("0a3fc31d-ec07-43a7-a637-68f32172978b");

        when(usuarioRepository.criar(any(Usuario.class))).thenReturn(usuario);

        var output = usuarioCreateUseCase.execute(input);

        assertThat(output).isInstanceOf(UsuarioCreateUseCaseOutput.class).isNotNull();

        assertThat(output)
                .extracting(UsuarioCreateUseCaseOutput::id)
                .isEqualTo(usuario.getId());

        verify(usuarioRepository, times(1)).criar(any(Usuario.class));
    }

}
