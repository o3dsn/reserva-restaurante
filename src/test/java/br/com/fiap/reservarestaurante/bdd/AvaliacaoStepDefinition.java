package br.com.fiap.reservarestaurante.bdd;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import br.com.fiap.avaliacao.model.AtualizaAvaliacaoDTO;
import br.com.fiap.avaliacao.model.AvaliacaoDTO;
import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.UsuarioJPARepository;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import java.time.Instant;
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
public class AvaliacaoStepDefinition extends StepDefsDefault {

  @Autowired private RestauranteRepository restauranteRepository;
  private Restaurante restaurante;
  @Autowired private UsuarioJPARepository usuarioRepository;
  private UsuarioJPAEntity usuario;
  @Autowired private ReservaRepository reservaRepository;
  private Reserva reserva;

  private Response response;
  private AvaliacaoDTO avaliacaoDTO;

  @LocalServerPort private int port;

  @Dado("que exista um restaurante com nome {string} e descricao {string} cadastrado")
  public void que_exista_um_restaurante_cadastrado(String nome, String descricao) {
    if (restaurante == null) {
      this.restaurante =
          Restaurante.builder().id(new RestauranteId(null)).nome(nome).descricao(descricao).build();

      this.restaurante = restauranteRepository.criar(restaurante);
    }
  }

  @Dado("que exista um usuario cadastrado")
  public void que_um_usuario_cadastrado() {
    if (this.usuario == null) {
      this.usuario = new UsuarioJPAEntity();
      this.usuario = usuarioRepository.save(usuario);
    }
  }

  @Dado("uma reserva feito polo usuario para o restaurante com status finalizada")
  public void uma_reserva_feito_polo_usuario_para_o_restaurante_com_status_finalizada() {
    var status = ReservaDTO.StatusEnum.FINALIZADA;
    this.reserva =
        Reserva.nova(
            restaurante.getId().value(),
            usuario.getId(),
            status,
            "Reserva feita pelo cucumber",
            Instant.now());
    this.reserva.atualizar(status);
    this.reserva = reservaRepository.criar(reserva);
  }

  @Quando("registrar uma nova avaliacao")
  public void registrar_uma_nova_avaliacao() {
    var body = AvaliacaoHelper.gerarCriaAvaliacaoDTONova();

    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("reservaId", this.reserva.getId().value())
            .queryParam("usuarioId", this.reserva.getUsuarioId())
            .body(body)
            .when()
            .post("http://localhost:%d/avaliacoes".formatted(port));
  }

  @Entao("a avaliacao e registrada com sucesso")
  public void a_avaliacao_e_registrada_com_sucesso() {
    response.then().statusCode(HttpStatus.CREATED.value());
  }

  @Entao("a avaliacao e exibida com sucesso")
  public void deve_ser_apresentada() {
    response.then().body(matchesJsonSchemaInClasspath("schemas/avaliacao.schema.json"));
  }

  @Dado("que uma avaliacao ja foi registrada")
  public void que_uma_avaliacao_ja_foi_registrada() {
    que_exista_um_restaurante_cadastrado("Restaurante 2", "Restaurante 2 outra vez");
    que_um_usuario_cadastrado();
    uma_reserva_feito_polo_usuario_para_o_restaurante_com_status_finalizada();
    registrar_uma_nova_avaliacao();
    avaliacaoDTO = this.response.then().extract().as(AvaliacaoDTO.class);
  }

  @Quando("efetuar a busca da avaliacao")
  public void efetuar_a_busca_da_avaliacao() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("http://localhost:%d/avaliacoes/%s".formatted(port, avaliacaoDTO.getId()));
  }

  @Dado("que exista duas avaliacoes registradas")
  public void que_exista_duas_avaliacoes_registradas() {
    que_uma_avaliacao_ja_foi_registrada();
    que_uma_avaliacao_ja_foi_registrada();
  }

  @Quando("efetuar a busca por todas avaliacoes")
  public void efetuar_a_busca_por_todas_avaliacoes() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get("http://localhost:%d/avaliacoes".formatted(port));
  }

  @Entao("as avaliacoes sao exibidas com sucesso")
  public void as_avaliacoes_sao_exibidas_com_sucesso() {
    response.then().body(matchesJsonSchemaInClasspath("schemas/avaliacao.pagination.schema.json"));
  }

  @Quando("efetuar a busca pelas avaliacoes")
  public void efetuar_a_busca_pelas_avaliacoes() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(
                "http://localhost:%d/avaliacoes/restaurantes/%s"
                    .formatted(port, restaurante.getId()));
  }

  @Quando("efetuar a busca por sua nota")
  public void efetuar_a_busca_por_sua_nota() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(
                "http://localhost:%d/avaliacoes/restaurantes/nota/%s"
                    .formatted(port, restaurante.getId()));
  }

  @Entao("a nota deve ser exibida com sucesso")
  public void a_nota_deve_ser_exibida_com_sucesso() {
    response.then().body(matchesJsonSchemaInClasspath("schemas/notaRestaurante.schema.json"));
  }

  @Quando("efetuar requisicao para alterar avaliacao")
  public void efetuar_requisicao_para_alterar_avaliacao() {
    AtualizaAvaliacaoDTO atualizaAvaliacaoDTO = AvaliacaoHelper.gerarAtualizaAvaliacaoDTO();
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(atualizaAvaliacaoDTO)
            .when()
            .patch("http://localhost:%d/avaliacoes/%s".formatted(port, avaliacaoDTO.getId()));
  }

  @Entao("a avaliacao e atualizada com sucesso")
  public void a_avaliacao_e_atualizada_com_sucesso() {
    response.then().statusCode(HttpStatus.OK.value());
  }

  @Quando("efetuar requisicao para remover avaliacao")
  public void efetuar_requisicao_para_remover_avaliacao() {
    response =
        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("http://localhost:%d/avaliacoes/%s".formatted(port, avaliacaoDTO.getId()));
  }

  @Entao("a avaliacao e removida com sucesso")
  public void a_avaliacao_e_removida_com_sucesso() {
    response.then().statusCode(HttpStatus.NO_CONTENT.value());
  }
}
