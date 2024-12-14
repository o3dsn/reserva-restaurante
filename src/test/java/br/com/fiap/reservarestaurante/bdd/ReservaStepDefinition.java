package br.com.fiap.reservarestaurante.bdd;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.UsuarioJPARepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@NoArgsConstructor
@AllArgsConstructor
@AutoConfigureTestDatabase
public class ReservaStepDefinition extends StepDefsDefault {
    @Autowired
    private UsuarioJPARepository usuarioRepository;

    private UsuarioJPAEntity usuario;
    private String restauranteId;
    private Response response;
    private ReservaDTO reservaDTO;

    @LocalServerPort
    private int port;

    @Dado("que exista um restaurante com ID {string}")
    public void que_exista_um_restaurante_com_id(String restauranteId) {
        this.restauranteId = restauranteId;
    }

    @Dado("que exista pelo menos um usuario cadastrado")
    public void que_um_usuario_cadastrado() {
        if (this.usuario == null) {
            this.usuario = new UsuarioJPAEntity();
            this.usuario = usuarioRepository.save(usuario);
        }
    }

    @Quando("registrar uma nova reserva")
    public void registrar_uma_nova_reserva() {
        var body = ReservaHelper.gerarCriaReservaDTO();

        response =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("restauranteId", restauranteId)
                        .queryParam("usuarioId", usuario.getId())
                        .body(body)
                        .when()
                        .post("http://localhost:%d/reservas".formatted(port));

        this.reservaDTO = response.then().statusCode(HttpStatus.CREATED.value()).extract().as(ReservaDTO.class);
    }

    @Entao("a reserva e registrada com sucesso")
    public void a_reserva_e_registrada_com_sucesso() {
        response.then().statusCode(HttpStatus.CREATED.value());
    }

    @Quando("buscar a reserva registrada")
    public void buscar_a_reserva_registrada() {
        reservaDTO = response.then().extract().as(ReservaDTO.class);

        response =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .get("http://localhost:%d/reservas/%s".formatted(port, reservaDTO.getId()));
    }

    @Entao("os detalhes da reserva sao exibidos com sucesso")
    public void os_detalhes_da_reserva_sao_exibidos_com_sucesso() {
        response.then().statusCode(HttpStatus.OK.value());
        response.then().body(matchesJsonSchemaInClasspath("schemas/reserva.schema.json"));
    }

    @Quando("atualizar a reserva")
    public void atualizar_a_reserva() {
        var atualizaReservaDTO = ReservaHelper.gerarAtualizaReservaDTO();

        response =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .body(atualizaReservaDTO)
                        .when()
                        .patch("http://localhost:%d/reservas/%s".formatted(port, reservaDTO.getId()));
    }

    @Entao("a reserva e atualizada com sucesso")
    public void a_reserva_e_atualizada_com_sucesso() {
        response.then().statusCode(HttpStatus.OK.value());
    }

    @Quando("deletar a reserva registrada")
    public void deletar_a_reserva_registrada() {
        response =
                given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .delete("http://localhost:%d/reservas/%s".formatted(port, reservaDTO.getId()));
    }

    @Entao("a reserva e deletada com sucesso")
    public void a_reserva_e_deletada_com_sucesso() {
        response.then().statusCode(HttpStatus.NO_CONTENT.value());
    }
}
