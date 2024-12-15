package br.com.fiap.reservarestaurante.infrastructure.api;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
public class ReservaControllerIT {
  @LocalServerPort private int port;

  @BeforeEach
  void setup() {
    RestAssured.port = port;
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Nested
  class Criar {

    @Test
    void devePermitirReserva() {
      var body = ReservaHelper.gerarCriaReservaDTO();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("restauranteId", "177d17ed-9b8b-480f-becf-bb57c896f0f7")
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .body(body)
          .when()
          .post("/reservas")
          .then()
          .statusCode(HttpStatus.CREATED.value())
          .body(matchesJsonSchemaInClasspath("schemas/reserva.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoCriarReserva_SemDTO() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .queryParam("restauranteId", "177d17ed-9b8b-480f-becf-bb57c896f0f7")
          .queryParam("usuarioId", "afaa347c-b698-4e51-b71a-d861c5f480ba")
          .when()
          .post("/reservas")
          .then()
          .statusCode(HttpStatus.BAD_REQUEST.value())
          .body(matchesJsonSchemaInClasspath("schemas/erroDefault.schema.json"));
    }
  }

  @Nested
  class Buscar {

    @Test
    void devePermitirBuscarReservas() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/reservas")
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/reserva.pagination.schema.json"));
    }

    @Test
    void devePermitirBuscarReservaPorId() {
      var reservaId = "d750f2ba-568b-4d39-98fa-c525736be003";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/reservas/{id}", reservaId)
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/reserva.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoBuscarReservaPorId_Inexistente() {
      var reservaId = "id_inexistente";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .get("/reservas/{id}", reservaId)
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }

  @Nested
  class Atualizar {

    @Test
    void devePermitirAtualizarReserva() {
      var reservaId = "828a24a8-4e99-4c68-a476-3c9397bb4e97";
      var body = ReservaHelper.gerarAtualizaReservaDTO();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .patch("/reservas/{id}", reservaId)
          .then()
          .statusCode(HttpStatus.OK.value())
          .body(matchesJsonSchemaInClasspath("schemas/reserva.schema.json"));
    }

    @Test
    void deveGerarExcecao_QuandoAtualizarReserva_QuandoIdInvalido() {
      var reservaId = "id_invalido";
      var body = ReservaHelper.gerarAtualizaReservaDTO();

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .body(body)
          .when()
          .patch("/reservas/{id}", reservaId)
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }

  @Nested
  class Deletar {

    @Test
    void devePermitirDeletarReserva() {
      var reservaId = "81d46912-62bf-4c38-b638-3f569478e369";

      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/reservas/{id}", reservaId)
          .then()
          .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deveGerarExcecao_QuandoDeletarReserva_NaoExistir() {
      given()
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .when()
          .delete("/reservas/{id}", "c36c7d02-9b83-49cf-b1d9-13d0e80aae9b")
          .then()
          .statusCode(HttpStatus.NOT_FOUND.value())
          .body(matchesJsonSchemaInClasspath("schemas/dominioException.schema.json"));
    }
  }
}
