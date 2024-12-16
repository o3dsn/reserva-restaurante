package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get.UsuarioGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get.UsuarioGetByIdUseCaseOutPut;
import br.com.fiap.reservarestaurante.utils.UsuarioHelper;
import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static br.com.fiap.reservarestaurante.utils.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UsuarioControllerTest {

    @RegisterExtension
    LogTrackerStub logTracker =
            LogTrackerStub.create()
                    .recordForLevel(LogTracker.LogLevel.INFO)
                    .recordForType(UsuarioController.class);

    private MockMvc mockMvc;
    private AutoCloseable openMocks;

    @Mock
    private UsuarioCreateUseCase usuarioCreateUseCase;
    @Mock
    private UsuarioGetByIdUseCase usuarioGetByIdUseCase;

    @BeforeEach
    void setup() {
        this.openMocks = MockitoAnnotations.openMocks(this);
        UsuarioController usuarioController =
                new UsuarioController(
                        usuarioCreateUseCase,
                        usuarioGetByIdUseCase);

        mockMvc =
                MockMvcBuilders.standaloneSetup(usuarioController)
                        .setControllerAdvice(new RestResponseEntityExceptionHandler())
                        .addFilter(
                                (req, resp, chain) -> {
                                    resp.setCharacterEncoding("UTF-8");
                                    chain.doFilter(req, resp);
                                },
                                "/*")
                        .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        this.openMocks.close();
    }

    @Nested
    class Criar {
        @Test
        void devePermitirCriarUsuario() throws Exception {
            var body = UsuarioHelper.gerarUsuarioDTO();
            var usuario = UsuarioHelper.gerar("3a69f250-12ba-4030-a458-706748749126");

            final var useCaseOutput = UsuarioCreateUseCaseOutput.from(usuario);

            when(usuarioCreateUseCase.execute(any(UsuarioCreateUseCaseInput.class)))
                    .thenReturn(useCaseOutput);

            mockMvc
                    .perform(
                            post("/usuario/novo").contentType(MediaType.APPLICATION_JSON).content(toJson(body)))
                    .andExpect(status().isCreated());

            verify(usuarioCreateUseCase, times(1)).execute(any(UsuarioCreateUseCaseInput.class));
        }
    }

    @Test
    void devePermitirBuscarUsuarioPorId() throws Exception {
        var usuarioId = "3a69f250-12ba-4030-a458-706748749126";
        var usuario = UsuarioHelper.gerar(usuarioId);

        when(usuarioGetByIdUseCase.execute(usuarioId))
                .thenReturn(UsuarioGetByIdUseCaseOutPut.from(usuario));

        mockMvc
                .perform(get("/usuario/{id}", usuarioId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioId));

        verify(usuarioGetByIdUseCase, times(1)).execute(usuarioId);
    }
}
