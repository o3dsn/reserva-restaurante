package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.delete.ReservaDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list.ReservaListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list.ReservaListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list.ReservaListUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.update.ReservaUpdateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.update.ReservaUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.update.ReservaUpdateUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static br.com.fiap.reservarestaurante.utils.JsonUtil.toJson;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservaControllerTest {
    @RegisterExtension
    LogTrackerStub logTracker =
            LogTrackerStub.create()
                    .recordForLevel(LogTracker.LogLevel.INFO)
                    .recordForType(ReservaController.class);

    private MockMvc mockMvc;
    private AutoCloseable openMocks;

    @Mock
    private ReservaCreateUseCase reservaCreateUseCase;
    @Mock
    private ReservaDeleteUseCase reservaDeleteUseCase;
    @Mock
    private ReservaListUseCase reservaListUseCase;
    @Mock
    private ReservaGetByIdUseCase reservaGetByIdUseCase;
    @Mock
    private ReservaUpdateUseCase reservaUpdateUseCase;

    @BeforeEach
    void setup() {
        this.openMocks = MockitoAnnotations.openMocks(this);

        ReservaController reservaController =
                new ReservaController(
                        reservaCreateUseCase,
                        reservaDeleteUseCase,
                        reservaListUseCase,
                        reservaGetByIdUseCase,
                        reservaUpdateUseCase
                );
        mockMvc =
                MockMvcBuilders.standaloneSetup(reservaController)
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
        void devePermitirCriarReserva() throws Exception {
            var body = ReservaHelper.gerarCriaReservaDTO();
            var reserva = ReservaHelper.gerarReserva("3a69f250-12ba-4030-a458-706748749126");

            final var useCaseOutput = ReservaCreateUseCaseOutput.from(reserva);

            when(reservaCreateUseCase.execute(any(ReservaCreateUseCaseInput.class)))
                    .thenReturn(useCaseOutput);

            MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
            param.add("restauranteId", reserva.getRestauranteId());
            param.add("usuarioId", reserva.getUsuarioId());

            mockMvc
                    .perform(
                            post("/reservas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .params(param)
                                    .content(toJson(body)))
                    .andExpect(status().isCreated());

            verify(reservaCreateUseCase, times(1)).execute(any(ReservaCreateUseCaseInput.class));
        }
    }

    @Nested
    class Buscar {
        @Test
        void devePermitirBuscarTodasReserva() throws Exception {
            var output = ReservaListUseCaseOutput.from(ReservaHelper.gerarReserva("c8fa8769-33c5-42f0-9466-421880efdee2"));
            var pagination = new Pagination<>(0, 10, 1, List.of(output));

            when(reservaListUseCase.execute(any(ReservaListUseCaseInput.class))).thenReturn(pagination);

            MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
            param.add("pagina", "0");
            param.add("porPagina", "10");
            param.add("ativo", "true");

            mockMvc
                    .perform(get("/reservas").contentType(MediaType.APPLICATION_JSON).params(param))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pagina").value(pagination.currentPage()))
                    .andExpect(jsonPath("$.porPagina").value(pagination.perPage()))
                    .andExpect(jsonPath("$.total").value(pagination.total()))
                    .andExpect(jsonPath("$.items[0].id").value(output.id().value()))
                    .andExpect(jsonPath("$.items[0].restauranteId").value(output.restauranteId()))
                    .andExpect(jsonPath("$.items[0].usuarioId").value(output.usuarioId()))
                    .andExpect(jsonPath("$.items[0].dataHorarioReserva").value(output.dataHorarioReserva().getEpochSecond()))
                    .andExpect(jsonPath("$.items[0].criacao").value(output.criacao().getEpochSecond()))
                    .andExpect(jsonPath("$.items[0].alteracao").value(output.alteracao()))
                    .andExpect(jsonPath("$.items[0].comentario").value(output.comentario()));

            verify(reservaListUseCase, times(1)).execute(any(ReservaListUseCaseInput.class));
        }

        @Test
        void devePermitirBuscarReservaPorId() throws Exception {
            var output =
                    ReservaGetByIdUseCaseOutput.from(
                            ReservaHelper.gerarReserva("093bff48-6e42-4939-99d9-959f61b41fdd"));

            when(reservaGetByIdUseCase.execute(any(String.class))).thenReturn(output);

            mockMvc
                    .perform(
                            get("/reservas/{id}", output.id().value()).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(output.id().value()))
                    .andExpect(jsonPath("$.restauranteId").value(output.restauranteId()))
                    .andExpect(jsonPath("$.usuarioId").value(output.usuarioId()))
                    .andExpect(jsonPath("$.dataHorarioReserva").value(output.dataHorarioReserva().getEpochSecond()))
                    .andExpect(jsonPath("$.criacao").value(output.criacao().getEpochSecond()))
                    .andExpect(jsonPath("$.alteracao").value(output.alteracao()))
                    .andExpect(jsonPath("$.comentario").value(output.comentario()));

            verify(reservaGetByIdUseCase, times(1)).execute(any(String.class));
        }


    }

    @Nested
    class Atualizar {
        @Test
        void devePermitirAtualizarReserva() throws Exception {
            var id = "bb0bd99f-9ae0-4901-b484-8438fb72308d";

            var body = ReservaHelper.gerarAtualizaReservaDTO();
            var reserva = ReservaHelper.gerarReserva(id);

            final var useCaseOutput = ReservaUpdateUseCaseOutput.from(reserva);

            when(reservaUpdateUseCase.execute(any(ReservaUpdateUseCaseInput.class)))
                    .thenReturn(useCaseOutput);

            mockMvc
                    .perform(
                            patch("/reservas/{id}", id)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(toJson(body)))
                    .andExpect(status().isOk());

            verify(reservaUpdateUseCase, times(1)).execute(any(ReservaUpdateUseCaseInput.class));
        }
    }

    @Nested
    class Deletar {
        @Test
        void devePermitirDeletarReservaComStatusTrue() throws Exception {
            var id = "d750f2ba-568b-4d39-98fa-c525736be003";

            mockMvc
                    .perform(delete("/reservas/{id}", id).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(reservaDeleteUseCase, times(1)).execute(any(String.class));
        }
    }
}

