package br.com.fiap.reservarestaurante.bdd;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
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
public class RestauranteStepDefinition extends StepDefsDefault {

  @Autowired private RestauranteRepository restauranteRepository;

  private Restaurante restaurante;
  private Response response;

  @LocalServerPort private int port;

  @Dado("que exista um restaurante cadastrado com nome {string} e descricao {string}")
  public void que_exista_um_restaurante_cadastrado(String nome, String descricao) {
    this.restaurante =
        Restaurante.builder().id(new RestauranteId(null)).nome(nome).descricao(descricao).build();
    this.restaurante = restauranteRepository.criar(restaurante);
  }

  @Quando("registrar um novo restaurante com nome {string} e descricao {string}")
  public void registrar_um_novo_restaurante(String nome, String descricao) {
    Restaurante novoRestaurante =
        Restaurante.builder().id(new RestauranteId(null)).nome(nome).descricao(descricao).build();

    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(novoRestaurante)
            .when()
            .post("http://localhost:%d/restaurantes".formatted(port));
  }

  @Entao("o restaurante e registrado com sucesso")
  public void o_restaurante_e_registrado_com_sucesso() {
    response.then().statusCode(HttpStatus.CREATED.value());
  }

  @Quando("buscar os detalhes do restaurante")
  public void buscar_os_detalhes_do_restaurante() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(
                "http://localhost:%d/restaurantes/%s".formatted(port, restaurante.getId().value()));
  }

  @Entao("os detalhes do restaurante sao exibidos com sucesso")
  public void os_detalhes_do_restaurante_sao_exibidos_com_sucesso() {
    response.then().body(matchesJsonSchemaInClasspath("schemas/restaurante.schema.json"));
  }

  @Quando("atualizar o restaurante com nome {string} e descricao {string}")
  public void atualizar_o_restaurante(String nome, String descricao) {
    Restaurante atualizacao =
        Restaurante.builder().id(restaurante.getId()).nome(nome).descricao(descricao).build();

    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(atualizacao)
            .when()
            .patch(
                "http://localhost:%d/restaurantes/%s".formatted(port, restaurante.getId().value()));
  }

  @Entao("o restaurante e atualizado com sucesso")
  public void o_restaurante_e_atualizado_com_sucesso() {
    response.then().statusCode(HttpStatus.OK.value());
  }

  @Quando("remover o restaurante")
  public void remover_o_restaurante() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(
                "http://localhost:%d/restaurantes/%s".formatted(port, restaurante.getId().value()));
  }

  @Entao("o restaurante e removido com sucesso")
  public void o_restaurante_e_removido_com_sucesso() {
    response.then().statusCode(HttpStatus.NO_CONTENT.value());
  }
}
