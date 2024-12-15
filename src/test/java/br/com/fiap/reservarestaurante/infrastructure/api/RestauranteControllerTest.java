package br.com.fiap.reservarestaurante.infrastructure.api;

import static br.com.fiap.reservarestaurante.utils.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.delete.RestauranteDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class RestauranteControllerTest {

  @RegisterExtension
  LogTrackerStub logTracker =
      LogTrackerStub.create()
          .recordForLevel(LogTracker.LogLevel.INFO)
          .recordForType(RestauranteController.class);

  private MockMvc mockMvc;
  private AutoCloseable openMocks;

  @Mock private RestauranteGetByIdUseCase restauranteGetByIdUseCase;
  @Mock private RestauranteCreateUseCase restauranteCreateUseCase;
  @Mock private RestauranteUpdateUseCase restauranteUpdateUseCase;
  @Mock private RestauranteDeleteUseCase restauranteDeleteUseCase;
  @Mock private RestauranteListUseCase restauranteListUseCase;

  @BeforeEach
  void setup() {
    this.openMocks = MockitoAnnotations.openMocks(this);
    RestauranteController restauranteController =
        new RestauranteController(
            restauranteGetByIdUseCase,
            restauranteListUseCase,
            restauranteCreateUseCase,
            restauranteUpdateUseCase,
            restauranteDeleteUseCase);

    mockMvc =
        MockMvcBuilders.standaloneSetup(restauranteController)
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
    void devePermitirCriarRestaurante() throws Exception {
      var body = RestauranteHelper.gerarCriaRestauranteDTO();
      var restaurante = RestauranteHelper.gerarRestaurante("3a69f250-12ba-4030-a458-706748749126");

      final var useCaseOutput = RestauranteCreateUseCaseOutput.from(restaurante);

      when(restauranteCreateUseCase.execute(any(RestauranteCreateUseCaseInput.class)))
          .thenReturn(useCaseOutput);

      mockMvc
          .perform(
              post("/restaurantes").contentType(MediaType.APPLICATION_JSON).content(toJson(body)))
          .andExpect(status().isCreated());

      verify(restauranteCreateUseCase, times(1)).execute(any(RestauranteCreateUseCaseInput.class));
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodosRestaurantes() throws Exception {
      var output =
          RestauranteListUseCaseOutput.from(
              RestauranteHelper.gerarRestaurante("c8fa8769-33c5-42f0-9466-421880efdee2"));

      var pagination = new Pagination<>(0, 10, 1, List.of(output));

      when(restauranteListUseCase.execute(any(RestauranteListUseCaseInput.class)))
          .thenReturn(pagination);

      MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
      param.add("pagina", "0");
      param.add("porPagina", "10");

      mockMvc
          .perform(get("/restaurantes").contentType(MediaType.APPLICATION_JSON).params(param))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.pagina").value(pagination.currentPage()))
          .andExpect(jsonPath("$.porPagina").value(pagination.perPage()))
          .andExpect(jsonPath("$.total").value(pagination.total()))
          .andExpect(jsonPath("$.itens[0].id").value(output.id().value()))
          .andExpect(jsonPath("$.itens[0].nome").value(output.nome()))
          .andExpect(jsonPath("$.itens[0].descricao").value(output.descricao()))
          .andExpect(jsonPath("$.itens[0].endereco").value(output.endereco()))
          .andExpect(jsonPath("$.itens[0].cidade").value(output.cidade()))
          .andExpect(jsonPath("$.itens[0].estado").value(output.estado()))
          .andExpect(jsonPath("$.itens[0].bairro").value(output.bairro()))
          .andExpect(jsonPath("$.itens[0].tipoCozinhaId").value(output.tipoCozinhaId()))
          .andExpect(jsonPath("$.itens[0].faixaPreco").value(output.faixaPreco()))
          .andExpect(jsonPath("$.itens[0].telefone").value(output.telefone()))
          .andExpect(jsonPath("$.itens[0].email").value(output.email()))
          .andExpect(jsonPath("$.itens[0].horarioAbertura").value(output.horarioAbertura()))
          .andExpect(jsonPath("$.itens[0].horarioFechamento").value(output.horarioFechamento()));

      verify(restauranteListUseCase, times(1)).execute(any(RestauranteListUseCaseInput.class));
    }
  }

  @Test
  void devePermitirBuscarRestaurantePorId() throws Exception {
    var restauranteId = "3a69f250-12ba-4030-a458-706748749126";
    var restaurante = RestauranteHelper.gerarRestaurante(restauranteId);

    when(restauranteGetByIdUseCase.execute(restauranteId))
        .thenReturn(RestauranteGetByIdUseCaseOutput.from(restaurante));

    mockMvc
        .perform(get("/restaurantes/{id}", restauranteId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(restauranteId));

    verify(restauranteGetByIdUseCase, times(1)).execute(restauranteId);
  }

  @Test
  void deveRetornar404_QuandoRestauranteNaoExistir() throws Exception {
    var restauranteId = "id_invalido";

    when(restauranteGetByIdUseCase.execute(restauranteId))
        .thenThrow(new RestauranteException(restauranteId, HttpStatus.NOT_FOUND));

    mockMvc
        .perform(get("/restaurantes/{id}", restauranteId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(restauranteGetByIdUseCase, times(1)).execute(restauranteId);
  }

  @Nested
  class Deletar {
    @Test
    void devePermitirDeletarRestaurante() throws Exception {
      var id = "3a69f250-12ba-4030-a458-706748749126";

      mockMvc
          .perform(delete("/restaurantes/{id}", id).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());

      verify(restauranteDeleteUseCase, times(1)).execute(any(String.class));
    }

    @ParameterizedTest
    @CsvSource(
        value = {
          "Restaurante com ID %s já deletado.,CONFLICT",
          "Restaurante com ID %s não encontrado.,NOT_FOUND"
        })
    void deveGerarExcecao_QuandoDeletarRestaurante(String msg, String httpStatus) throws Exception {
      var id = "fd89b551-6f30-436f-b361-69cde1dcc713";
      var msgErro = msg.formatted(id);

      doThrow(new RestauranteException(msgErro, HttpStatus.valueOf(httpStatus)))
          .when(restauranteDeleteUseCase)
          .execute(any(String.class));

      mockMvc
          .perform(delete("/restaurantes/{id}", id).contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.status").value(httpStatus))
          .andExpect(jsonPath("$.erros.erro").value(msgErro));

      verify(restauranteDeleteUseCase, times(1)).execute(any(String.class));
    }
  }

  @Nested
  class Atualizar {

    @Test
    void devePermitirAtualizarRestauranteComStatusTrue() throws Exception {
      var id = "3a69f250-12ba-4030-a458-706748749126";

      var body = RestauranteHelper.gerarAtualizaRestauranteDTO();
      var restaurante = RestauranteHelper.gerarRestauranteAlterado(id);

      final var useCaseOutput = RestauranteUpdateUseCaseOutput.from(restaurante);

      when(restauranteUpdateUseCase.execute(any(RestauranteUpdateUseCaseInput.class)))
          .thenReturn(useCaseOutput);

      mockMvc
          .perform(
              put("/restaurantes/{id}", id)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(toJson(body)))
          .andExpect(status().isOk());

      verify(restauranteUpdateUseCase, times(1)).execute(any(RestauranteUpdateUseCaseInput.class));
    }

    @ParameterizedTest
    @CsvSource(
        value = {
          "Restaurante com ID %s não disponível para atualização.,CONFLICT",
          "Restaurante com ID %s não disponível para atualização.,NOT_FOUND"
        })
    void deveGerarExcecao_QuandoAtualizarRestaurante(String msg, String httpStatus)
        throws Exception {
      var id = "fd89b551-6f30-436f-b361-69cde1dcc713";
      var body = RestauranteHelper.gerarAtualizaRestauranteDTO();
      var msgErro = msg.formatted(id);

      when(restauranteUpdateUseCase.execute(any(RestauranteUpdateUseCaseInput.class)))
          .thenThrow(new RestauranteException(msgErro, HttpStatus.valueOf(httpStatus)));

      mockMvc
          .perform(
              put("/restaurantes/{id}", id)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(toJson(body)))
          .andExpect(jsonPath("$.status").value(httpStatus))
          .andExpect(jsonPath("$.erros.erro").value(msgErro));

      verify(restauranteUpdateUseCase, times(1)).execute(any(RestauranteUpdateUseCaseInput.class));
    }
  }
}
