package br.com.fiap.reservarestaurante.infrastructure.api;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fiap.reserva.model.AtualizaReservaDTO;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
class AvaliacaoControllerIT {

  @LocalServerPort protected int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  class Criar {

    private void atualizarReserva(String reservaId) {
      var body = new AtualizaReservaDTO().status(AtualizaReservaDTO.StatusEnum.FINALIZADA);
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .patch("/reservas/{id}", reservaId)
          .then()
          .statusCode(HttpStatus.OK.value());
    }

    @Test
    void devePermitirCriarAvaliacao() {
      var reservaId = "828a24a8-4e99-4c68-a476-3c9397bb4e97";
      var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();

      atualizarReserva(reservaId);

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("reservaId", reservaId)
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .body(body)
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.CREATED.value())
          .body(matchesJsonSchemaInClasspath("schemas/avaliacao.schema.json"));
    }

    @ParameterizedTest
    @CsvSource(
        value = {
          "Reserva ainda não finalizada,81d46912-62bf-4c38-b638-3f569478e369,afaa347c-b698-4e51-b71a-d861c5f480ba",
          "Usuario não pode fazer avaliação para essa reserva,d750f2ba-568b-4d39-98fa-c525736be003,afaa347c-b698-4e51-b71a-d861c5f480bc",
          "Tempo limite para realizar avaliação alcançado,be147699-ff04-4503-a48e-5d5740739272,afaa347c-b698-4e51-b71a-d861c5f480ba",
          "Avaliação já registrada para essa reserva,093bff48-6e42-4939-99d9-959f61b41fdd,afaa347c-b698-4e51-b71a-d861c5f480ba"
        })
    void deveGerarExcecao_QuandoCriarAvaliacao(String msg, String reservaId, String usuarioId) {
      var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();

      var erro =
          given()
              .contentType(MediaType.APPLICATION_JSON_VALUE)
              .queryParam("reservaId", reservaId)
              .queryParam("usuarioId", usuarioId)
              .body(body)
              .when()
              .post("/avaliacoes")
              .then()
              .statusCode(HttpStatus.CONFLICT.value())
              .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"))
              .extract()
              .response()
              .jsonPath()
              .get("erros.erro");

      assertEquals(msg, erro);
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_ComParametroReservaIdFaltando() {
      var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .body(body)
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioExceptionReservaId.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_ComParametroUsuarioIdFaltando() {
      var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("reservaId", "093bff48-6e42-4939-99d9-959f61b41fdd")
          .body(body)
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioExceptionUsuarioId.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_SemParametro() {
      var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioExceptionReservaId.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_SemDTO() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("reservaId", "81d46912-62bf-4c38-b638-3f569478e369")
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/erroDefault.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_ComDTOFaltandoAtributos() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("reservaId", "81d46912-62bf-4c38-b638-3f569478e369")
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .body("{}")
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/criaAvaliacaoDTOAllMissing.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarAvaliacao_ComPayloadXml() {
      var reservaId = "828a24a8-4e99-4c68-a476-3c9397bb4e97";
      var body = "<avaliacao><comentario>um comentario</comentario><nota>3.4</nota></avaliacao>";

      atualizarReserva(reservaId);

      given()
          .contentType(MediaType.APPLICATION_XML_VALUE)
          .queryParam("reservaId", reservaId)
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .body(body)
          .when()
          .post("/avaliacoes")
          .then()
          .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
          .body(matchesJsonSchemaInClasspath("schemas/erroDefault.schema.json"));
    }
  }

  @Nested
  class Atualizar {
    @Test
    void devePermitirAtualizarAvaliacao() {
      var body = AvaliacaoHelper.gerarAtualizaAvaliacaoDTO();
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .patch("/avaliacoes/{id}", "3d4abf43-22ce-4d83-ba4c-d8bafe71569f")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/avaliacao.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoAtualizarAvaliacao_ComStatusFalse() {
      var body = AvaliacaoHelper.gerarAtualizaAvaliacaoDTO();
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .patch("/avaliacoes/{id}", "d4322250-3fc0-49f2-99df-0ca87c40dc5a")
          .then()
          .statusCode(HttpStatus.CONFLICT.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoAtualizarAvaliacao_QuandoIdInvalido() {
      var body = AvaliacaoHelper.gerarAtualizaAvaliacaoDTO();
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .patch("/avaliacoes/{id}", "2258bc37-77fe-4607-90ce-370a567906b1")
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }

  @Nested
  class Deletar {
    @Test
    void devePermitirDeletarAvaliacaoComStatusTrue() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/avaliacoes/{id}", "73168510-2714-4cd9-a2e6-149b3fc862d6")
          .then()
          .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveGerarExcecao_QuandoDeletarAvaliacao_JaDeletada() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/avaliacoes/{id}", "d4322250-3fc0-49f2-99df-0ca87c40dc5a")
          .then()
          .statusCode(HttpStatus.CONFLICT.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoDeletarAvaliacao_NaoExiste() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/avaliacoes/{id}", "c36c7d02-9b83-49cf-b1d9-13d0e80aae9b")
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasAvaliacoes() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/avaliacoes")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/avaliacao.pagination.schema.json"));
    }

    @Test
    void devePermitirBuscarAvaliacaoPorId() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/avaliacoes/{id}", "73168510-2714-4cd9-a2e6-149b3fc862d6")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/avaliacao.schema.json"));
    }

    @Test
    void devePermitirBuscarAvaliacoesPorIdRestaurante() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/avaliacoes/restaurantes/{id}", "177d17ed-9b8b-480f-becf-bb57c896f0f6")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/avaliacao.pagination.schema.json"));
    }

    @Test
    void devePermitirBuscarNotaRestaurantePorIdRestaurante() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/avaliacoes/restaurantes/nota/{id}", "177d17ed-9b8b-480f-becf-bb57c896f0f6")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/notaRestaurante.schema.json"));
    }
  }
}
