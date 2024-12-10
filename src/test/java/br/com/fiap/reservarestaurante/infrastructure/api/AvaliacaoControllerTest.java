package br.com.fiap.reservarestaurante.infrastructure.api;

import static br.com.fiap.reservarestaurante.utils.JsonUtil.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.delete.AvaliacaoDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get.AvaliacaoGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get.AvaliacaoGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota.NotaRestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota.NotaRestauranteGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCaseOutput;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class AvaliacaoControllerTest {

  @RegisterExtension
  LogTrackerStub logTracker =
      LogTrackerStub.create()
          .recordForLevel(LogTracker.LogLevel.INFO)
          .recordForType(AvaliacaoController.class);

  private MockMvc mockMvc;
  private AutoCloseable openMocks;

  @Mock private AvaliacaoCreateUseCase avaliacaoCreateUseCase;
  @Mock private AvaliacaoListUseCase avaliacaoListUseCase;
  @Mock private AvaliacaoGetByIdUseCase avaliacaoGetByIdUseCase;
  @Mock private AvaliacaoListByIdRestauranteUseCase avaliacaoListByIdRestauranteUseCase;
  @Mock private AvaliacaoUpdateUseCase avaliacaoUpdateUseCase;
  @Mock private AvaliacaoDeleteUseCase avaliacaoDeleteUseCase;
  @Mock private NotaRestauranteGetByIdUseCase notaRestauranteGetByIdUseCase;

  @BeforeEach
  void setup() {
    this.openMocks = MockitoAnnotations.openMocks(this);
    AvaliacaoController avaliacaoController =
        new AvaliacaoController(
            avaliacaoCreateUseCase,
            avaliacaoListUseCase,
            avaliacaoGetByIdUseCase,
            avaliacaoListByIdRestauranteUseCase,
            avaliacaoUpdateUseCase,
            avaliacaoDeleteUseCase,
            notaRestauranteGetByIdUseCase);
    mockMvc =
        MockMvcBuilders.standaloneSetup(avaliacaoController)
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
    void devePermitirCriarAvaliacao() throws Exception {
      var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();
      var avaliacao = AvaliacaoHelper.gerarAvaliacao("3a69f250-12ba-4030-a458-706748749126");

      final var useCaseOutput = AvaliacaoCreateUseCaseOutput.from(avaliacao);

      when(avaliacaoCreateUseCase.execute(any(AvaliacaoCreateUseCaseInput.class)))
          .thenReturn(useCaseOutput);

      MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
      param.add("reservaId", avaliacao.getReservaId());
      param.add("usuarioId", avaliacao.getUsuarioId());

      mockMvc
          .perform(
              post("/avaliacoes")
                  .contentType(MediaType.APPLICATION_JSON)
                  .params(param)
                  .content(toJson(body)))
          .andExpect(status().isCreated());

      verify(avaliacaoCreateUseCase, times(1)).execute(any(AvaliacaoCreateUseCaseInput.class));
    }

    @ParameterizedTest
    @ValueSource(
        strings = {
          "Reserva ainda não finalizada",
          "Usuario não pode fazer avaliação para essa reserva",
          "Tempo limite para realizar avaliação alcançado",
          "Avaliação já registrada para essa reserva"
        })
    void deveGerarExcecao_QuandoCriarAvaliacao(String msg) throws Exception {
      var body = toJson(AvaliacaoHelper.gerarCriaAvaliacaoDTONova());

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("reservaId", AvaliacaoHelper.RESERVA_ID);
      params.add("usuarioId", AvaliacaoHelper.USUARIO_ID);

      when(avaliacaoCreateUseCase.execute(any(AvaliacaoCreateUseCaseInput.class)))
          .thenThrow(new AvaliacaoException(msg, HttpStatus.CONFLICT));

      mockMvc
          .perform(
              post("/avaliacoes")
                  .contentType(MediaType.APPLICATION_JSON)
                  .params(params)
                  .content(body))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.erros.erro").value(msg));

      verify(avaliacaoCreateUseCase, times(1)).execute(any(AvaliacaoCreateUseCaseInput.class));
    }

    @ParameterizedTest
    @CsvSource(
        value = {
          "usuarioId,reservaId," + AvaliacaoHelper.USUARIO_ID,
          "reservaId,usuarioId," + AvaliacaoHelper.RESERVA_ID
        })
    void deveGerarExcecao_QuandoCriarAvaliacao_ComParametroFaltando(
        String parametro, String parametroFaltante, String valor) throws Exception {
      var body = toJson(AvaliacaoHelper.gerarCriaAvaliacaoDTONova());

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add(parametro, valor);

      mockMvc
          .perform(
              post("/avaliacoes")
                  .contentType(MediaType.APPLICATION_JSON)
                  .params(params)
                  .content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(
              jsonPath("$.erros." + parametroFaltante)
                  .value(
                      "Required request parameter '%s' for method parameter type String is not present"
                          .formatted(parametroFaltante)));

      verify(avaliacaoCreateUseCase, never()).execute(any(AvaliacaoCreateUseCaseInput.class));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_SemParametro() throws Exception {
      var body = toJson(AvaliacaoHelper.gerarCriaAvaliacaoDTONova());

      mockMvc
          .perform(post("/avaliacoes").contentType(MediaType.APPLICATION_JSON).content(body))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
          .andExpect(
              jsonPath("$.erros.reservaId")
                  .value(
                      "Required request parameter 'reservaId' for method parameter type String is not present"));

      verify(avaliacaoCreateUseCase, never()).execute(any(AvaliacaoCreateUseCaseInput.class));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_SemDTO() throws Exception {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("reservaId", AvaliacaoHelper.RESERVA_ID);
      params.add("usuarioId", AvaliacaoHelper.USUARIO_ID);

      mockMvc
          .perform(post("/avaliacoes").contentType(MediaType.APPLICATION_JSON).params(params))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value("400"))
          .andExpect(jsonPath("$.detail").value("Failed to read request"))
          .andExpect(jsonPath("$.instance").value("/avaliacoes"));

      verify(avaliacaoCreateUseCase, never()).execute(any(AvaliacaoCreateUseCaseInput.class));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_ComDTOFaltandoAtributos() throws Exception {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("reservaId", AvaliacaoHelper.RESERVA_ID);
      params.add("usuarioId", AvaliacaoHelper.USUARIO_ID);

      mockMvc
          .perform(
              post("/avaliacoes")
                  .contentType(MediaType.APPLICATION_JSON)
                  .params(params)
                  .content("{}"))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.comentario").value("must not be null"))
          .andExpect(jsonPath("$.nota").value("must not be null"));

      verify(avaliacaoCreateUseCase, never()).execute(any(AvaliacaoCreateUseCaseInput.class));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_ComPayloadXml() throws Exception {
      var content = "<avaliacao><comentario>um comentario</comentario><nota>3.4</nota></avaliacao>";
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("reservaId", AvaliacaoHelper.RESERVA_ID);
      params.add("usuarioId", AvaliacaoHelper.USUARIO_ID);

      mockMvc
          .perform(
              post("/avaliacoes")
                  .contentType(MediaType.APPLICATION_XML)
                  .params(params)
                  .content(content))
          .andExpect(status().isUnsupportedMediaType())
          .andExpect(jsonPath("$.detail").value("Content-Type 'application/xml' is not supported."))
          .andExpect(jsonPath("$.instance").value("/avaliacoes"));

      verify(avaliacaoCreateUseCase, never()).execute(any(AvaliacaoCreateUseCaseInput.class));
    }
  }

  @Nested
  class Atualizar {
    @Test
    void devePermitirAtualizarAvaliacaoComStatusTrue() throws Exception {
      var id = "fd89b551-6f30-436f-b361-69cde1dcc713";

      var body = AvaliacaoHelper.gerarAtualizaAvaliacaoDTO();
      var avaliacao = AvaliacaoHelper.gerarAvaliacaoAlterada(id);

      final var useCaseOutput = AvaliacaoUpdateUseCaseOutput.from(avaliacao);

      when(avaliacaoUpdateUseCase.execute(any(AvaliacaoUpdateUseCaseInput.class)))
          .thenReturn(useCaseOutput);

      mockMvc
          .perform(
              patch("/avaliacoes/{id}", id)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(toJson(body)))
          .andExpect(status().isOk());

      verify(avaliacaoUpdateUseCase, times(1)).execute(any(AvaliacaoUpdateUseCaseInput.class));
    }

    @ParameterizedTest
    @CsvSource(
        value = {
          "Avaliação com ID %s não disponível para atualização.,CONFLICT",
          "Avaliação com ID %s não disponível para atualização.,NOT_FOUND"
        })
    void deveGerarExcecao_QuandoAtualizarAvaliacao(String msg, String httpStatus) throws Exception {
      var id = "fd89b551-6f30-436f-b361-69cde1dcc713";
      var body = AvaliacaoHelper.gerarAtualizaAvaliacaoDTO();
      var msgErro = msg.formatted(id);

      when(avaliacaoUpdateUseCase.execute(any(AvaliacaoUpdateUseCaseInput.class)))
          .thenThrow(new AvaliacaoException(msgErro, HttpStatus.valueOf(httpStatus)));

      mockMvc
          .perform(
              patch("/avaliacoes/{id}", id)
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(toJson(body)))
          .andExpect(jsonPath("$.status").value(httpStatus))
          .andExpect(jsonPath("$.erros.erro").value(msgErro));

      verify(avaliacaoUpdateUseCase, times(1)).execute(any(AvaliacaoUpdateUseCaseInput.class));
    }
  }

  @Nested
  class Deletar {
    @Test
    void devePermitirDeletarAvaliacaoComStatusTrue() throws Exception {
      var id = "fd89b551-6f30-436f-b361-69cde1dcc713";

      mockMvc
          .perform(delete("/avaliacoes/{id}", id).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNoContent());

      verify(avaliacaoDeleteUseCase, times(1)).execute(any(String.class));
    }

    @ParameterizedTest
    @CsvSource(
        value = {
          "Avaliação com ID %s já deletada.,CONFLICT",
          "Avaliação com ID %s não encontrado.,NOT_FOUND"
        })
    void deveGerarExcecao_QuandoDeletarAvaliacao(String msg, String httpStatus) throws Exception {
      var id = "fd89b551-6f30-436f-b361-69cde1dcc713";
      var msgErro = msg.formatted(id);

      doThrow(new AvaliacaoException(msgErro, HttpStatus.valueOf(httpStatus)))
          .when(avaliacaoDeleteUseCase)
          .execute(any(String.class));

      mockMvc
          .perform(delete("/avaliacoes/{id}", id).contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.status").value(httpStatus))
          .andExpect(jsonPath("$.erros.erro").value(msgErro));

      verify(avaliacaoDeleteUseCase, times(1)).execute(any(String.class));
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasAvaliacoes() throws Exception {
      var output =
          AvaliacaoListUseCaseOutput.from(
              AvaliacaoHelper.gerarAvaliacao("c8fa8769-33c5-42f0-9466-421880efdee2"));
      var pagination = new Pagination<>(0, 10, 1, List.of(output));

      when(avaliacaoListUseCase.execute(any(AvaliacaoListUseCaseInput.class)))
          .thenReturn(pagination);

      MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
      param.add("pagina", "0");
      param.add("porPagina", "10");
      param.add("ativo", "true");

      mockMvc
          .perform(get("/avaliacoes").contentType(MediaType.APPLICATION_JSON).params(param))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.pagina").value(pagination.currentPage()))
          .andExpect(jsonPath("$.porPagina").value(pagination.perPage()))
          .andExpect(jsonPath("$.total").value(pagination.total()))
          .andExpect(jsonPath("$.items[0].id").value(output.id().value()))
          .andExpect(jsonPath("$.items[0].reservaId").value(output.reservaId()))
          .andExpect(jsonPath("$.items[0].usuarioId").value(output.usuarioId()))
          .andExpect(jsonPath("$.items[0].criacao").value(output.criacao().getEpochSecond()))
          .andExpect(jsonPath("$.items[0].alteracao").value(output.alteracao()))
          .andExpect(jsonPath("$.items[0].exclusao").value(output.exclusao()))
          .andExpect(jsonPath("$.items[0].ativo").value(output.ativo()))
          .andExpect(jsonPath("$.items[0].comentario").value(output.comentario()))
          .andExpect(jsonPath("$.items[0].nota").value(output.nota()));

      verify(avaliacaoListUseCase, times(1)).execute(any(AvaliacaoListUseCaseInput.class));
    }

    @Test
    void devePermitirBuscarTodasAvaliacoes_SemAvaliacao() throws Exception {
      Pagination<AvaliacaoListUseCaseOutput> pagination = new Pagination<>(0, 10, 1, List.of());

      when(avaliacaoListUseCase.execute(any(AvaliacaoListUseCaseInput.class)))
          .thenReturn(pagination);

      mockMvc
          .perform(get("/avaliacoes").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.pagina").value(pagination.currentPage()))
          .andExpect(jsonPath("$.porPagina").value(pagination.perPage()))
          .andExpect(jsonPath("$.total").value(pagination.total()))
          .andExpect(jsonPath("$.items").isEmpty());

      verify(avaliacaoListUseCase, times(1)).execute(any(AvaliacaoListUseCaseInput.class));
    }

    @Test
    void devePermitirBuscarAvaliacaoPorId() throws Exception {
      var output =
          AvaliacaoGetByIdUseCaseOutput.from(
              AvaliacaoHelper.gerarAvaliacao("ffc20765-c3bb-4b4d-8a4f-c4629f1a5fa1"));

      when(avaliacaoGetByIdUseCase.execute(any(String.class))).thenReturn(output);

      mockMvc
          .perform(
              get("/avaliacoes/{id}", output.id().value()).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(output.id().value()))
          .andExpect(jsonPath("$.reservaId").value(output.reservaId()))
          .andExpect(jsonPath("$.usuarioId").value(output.usuarioId()))
          .andExpect(jsonPath("$.criacao").value(output.criacao().getEpochSecond()))
          .andExpect(jsonPath("$.alteracao").value(output.alteracao()))
          .andExpect(jsonPath("$.exclusao").value(output.exclusao()))
          .andExpect(jsonPath("$.ativo").value(output.ativo()))
          .andExpect(jsonPath("$.comentario").value(output.comentario()))
          .andExpect(jsonPath("$.nota").value(output.nota()));

      verify(avaliacaoGetByIdUseCase, times(1)).execute(any(String.class));
    }

    @Test
    void devePermitirBuscarAvaliacoesPorIdRestaurante() throws Exception {
      var id = "62a5ae8e-4d86-4d26-926e-cc80f2b0816e";
      var output =
          AvaliacaoListByIdRestauranteUseCaseOutput.from(
              AvaliacaoHelper.gerarAvaliacao("bca45593-60d4-4353-8572-8cc59d4a6d3d"));
      var pagination = new Pagination<>(0, 10, 1, List.of(output));

      when(avaliacaoListByIdRestauranteUseCase.execute(
              any(AvaliacaoListByIdRestauranteUseCaseInput.class)))
          .thenReturn(pagination);

      MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
      param.add("pagina", "0");
      param.add("porPagina", "10");

      mockMvc
          .perform(
              get("/avaliacoes/restaurantes/{id}", id)
                  .contentType(MediaType.APPLICATION_JSON)
                  .params(param))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.pagina").value(pagination.currentPage()))
          .andExpect(jsonPath("$.porPagina").value(pagination.perPage()))
          .andExpect(jsonPath("$.total").value(pagination.total()))
          .andExpect(jsonPath("$.items[0].id").value(output.id().value()))
          .andExpect(jsonPath("$.items[0].reservaId").value(output.reservaId()))
          .andExpect(jsonPath("$.items[0].usuarioId").value(output.usuarioId()))
          .andExpect(jsonPath("$.items[0].criacao").value(output.criacao().getEpochSecond()))
          .andExpect(jsonPath("$.items[0].alteracao").value(output.alteracao()))
          .andExpect(jsonPath("$.items[0].exclusao").value(output.exclusao()))
          .andExpect(jsonPath("$.items[0].ativo").value(output.ativo()))
          .andExpect(jsonPath("$.items[0].comentario").value(output.comentario()))
          .andExpect(jsonPath("$.items[0].nota").value(output.nota()));

      verify(avaliacaoListByIdRestauranteUseCase, times(1))
          .execute(any(AvaliacaoListByIdRestauranteUseCaseInput.class));
    }

    @Test
    void devePermitirBuscarNotaRestaurantePorIdRestaurante() throws Exception {
      var output = NotaRestauranteGetByIdUseCaseOutput.from(AvaliacaoHelper.gerarNotaRestaurante());

      when(notaRestauranteGetByIdUseCase.execute(any(String.class))).thenReturn(output);

      mockMvc
          .perform(
              get("/avaliacoes/restaurantes/nota/{id}", AvaliacaoHelper.RESTAURANTE_ID)
                  .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.restauranteId").value(AvaliacaoHelper.RESTAURANTE_ID))
          .andExpect(jsonPath("$.avaliacoes").value(output.avaliacoes()))
          .andExpect(jsonPath("$.nota").value(output.nota()));

      verify(notaRestauranteGetByIdUseCase, times(1)).execute(any(String.class));
    }
  }
}
