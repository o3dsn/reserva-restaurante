package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.utils.UsuarioHelper;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@Transactional
@RequiredArgsConstructor
class UsuarioControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Nested
    class Criar {

        @Test
        void devePermitirCriarUsuario() {
            var body = UsuarioHelper.gerarUsuarioDTO();

            var response = given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .when()
                    .post("/usuario/novo")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract().response();

            Assertions.assertEquals("Joao do Bairro", response.jsonPath().getString("nome"));
            Assertions.assertEquals("joao@exemplo.com", response.jsonPath().getString("email"));

        }

        @Test
        void deveGerarExcecao_QuandoCriarUsuario_SemDTO() {
            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/usuario/novo")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(matchesJsonSchemaInClasspath("schemas/erroDefault.schema.json"));
        }
    }

    @Nested
    class Buscar {

        @Test
        void devePermitirBuscarUsuarioPorId() {
            var restauranteId = "afaa347c-b698-4e51-b71a-d861c5f480ba";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuario/{id}", restauranteId)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(matchesJsonSchemaInClasspath("schemas/usuario.schema.json"));
        }

        @Test
        void deveGerarExcecao_QuandoBuscarUsuarioPorId_Inexistente() {
            var restauranteId = "2cc9c534-9900-44b7-a0d2-551d38d82953";

            given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuario/{id}", restauranteId)
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("erros.erro", is("Usuario com ID 2cc9c534-9900-44b7-a0d2-551d38d82953 não encontrado."));
        }
    }
}
