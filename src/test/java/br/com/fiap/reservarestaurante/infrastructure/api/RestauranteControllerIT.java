package br.com.fiap.reservarestaurante.infrastructure.api;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
class RestauranteControllerIT {

  @LocalServerPort private int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  class Criar {

    @Test
    void devePermitirCriarRestaurante() {
      var body = RestauranteHelper.gerarCriaRestauranteDTO();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .post("/restaurantes")
          .then()
          .statusCode(HttpStatus.CREATED.value())
          .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarRestaurante_SemDTO() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .post("/restaurantes")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/erroDefault.schema.json"));
    }
  }

  @Nested
  class Buscar {

    @Test
    void devePermitirBuscarRestaurantes() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/restaurantes")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/restaurante.pagination.schema.json"));
    }

    @Test
    void devePermitirBuscarRestaurantePorId() {
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f7";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/restaurantes/{id}", restauranteId)
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoBuscarRestaurantePorId_Inexistente() {
      var restauranteId = "id_inexistente";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/restaurantes/{id}", restauranteId)
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }

  @Nested
  class Atualizar {

    @Test
    void devePermitirAtualizarRestaurante() {
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f7";
      var body = RestauranteHelper.gerarAtualizaRestauranteDTO();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .put("/restaurantes/{id}", restauranteId)
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoAtualizarRestaurante_QuandoIdInvalido() {
      var restauranteId = "id_invalido";
      var body = RestauranteHelper.gerarAtualizaRestauranteDTO();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .put("/restaurantes/{id}", restauranteId)
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }

  @Nested
  class Deletar {

    @Test
    void devePermitirDeletarRestaurante() {
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f6";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/restaurantes/{id}", restauranteId)
          .then()
          .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveGerarExcecao_QuandoDeletarRestaurante_NaoExistir() {
      var restauranteId = "id_inexistente";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/restaurantes/{id}", restauranteId)
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }
}
