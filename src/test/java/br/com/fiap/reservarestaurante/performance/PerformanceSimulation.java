package br.com.fiap.reservarestaurante.performance;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

import br.com.fiap.reserva.model.AtualizaReservaDTO;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import br.com.fiap.reservarestaurante.utils.JsonUtil;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import br.com.fiap.usuario.model.CadastrarUsuarioDTO;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;

public class PerformanceSimulation extends Simulation {

  final String criaRestauranteDTO = JsonUtil.toJson(RestauranteHelper.gerarCriaRestauranteDTO());
  final String criaUsuarioDTO = JsonUtil.toJson(gerarCadastrarUsuarioDTO());
  final String criaReservaDTO = JsonUtil.toJson(ReservaHelper.gerarCriaReservaDTO());
  final String atualizaReservaDTO = JsonUtil.toJson(gerarAtualizaReservaDTO());
  final String criaAvaliacaoDTO = JsonUtil.toJson(AvaliacaoHelper.gerarCriaAvaliacaoDTONova());
  private final HttpProtocolBuilder httpProtocolBuilder =
      http.baseUrl("http://localhost:8080").header("Content-Type", "application/json");
  ActionBuilder adicionarRestauranteRequest =
      http("adicionar restaurante")
          .post("/restaurantes")
          .body(StringBody(criaRestauranteDTO))
          .check(status().is(201))
          .check(jsonPath("$.id").saveAs("restauranteId"));
  ActionBuilder adicionarUsuarioRequest =
        http("adicionar usuario")
          .post("/usuario/novo")
          .body(StringBody(criaUsuarioDTO))
          .check(status().is(201))
          .check(jsonPath("$.id").saveAs("usuarioId"));
  ActionBuilder adicionarReservaRequest =
      http("adicionar reserva")
          .post("/reservas")
          .body(StringBody(criaReservaDTO))
          .queryParam("restauranteId", "#{restauranteId}")
          .queryParam("usuarioId", "#{usuarioId}")
          .check(status().is(201))
          .check(jsonPath("$.id").saveAs("reservaId"));
  ActionBuilder atualizarReservaRequest =
      http("atualizar reserva")
          .patch("/reservas/%s".formatted("#{reservaId}"))
          .body(StringBody(atualizaReservaDTO))
          .check(status().is(200));
  ActionBuilder adicionarAvaliacaoRequest =
      http("adicionar avaliacao")
          .post("/avaliacoes")
          .queryParam("reservaId", "#{reservaId}")
          .queryParam("usuarioId", "#{usuarioId}")
          .body(StringBody(criaAvaliacaoDTO))
          .check(status().is(201))
          .check(jsonPath("$.id").saveAs("avaliacaoId"));
  ActionBuilder buscarAvaliacaoRequest =
      http("buscar avaliacao")
          .get("/avaliacoes/%s".formatted("#{avaliacaoId}"))
          .check(status().is(200));
  ScenarioBuilder scenarioAdicionarAvaliacao =
      scenario("adicionar avaliacao")
          .exec(adicionarUsuarioRequest)
          .exec(adicionarRestauranteRequest)
          .exec(adicionarReservaRequest)
          .exec(atualizarReservaRequest)
          .exec(adicionarAvaliacaoRequest);
  ScenarioBuilder scenarioBuscarAvaliacao =
      scenario("buscar avaliacao")
          .exec(adicionarUsuarioRequest)
          .exec(adicionarRestauranteRequest)
          .exec(adicionarReservaRequest)
          .exec(atualizarReservaRequest)
          .exec(adicionarAvaliacaoRequest)
          .exec(buscarAvaliacaoRequest);

  {
    setUp(
            scenarioAdicionarAvaliacao.injectOpen(
                rampUsersPerSec(1).to(5).during(Duration.ofSeconds(5)),
                constantUsersPerSec(5).during(Duration.ofSeconds(15)),
                rampUsersPerSec(5).to(1).during(Duration.ofSeconds(5))
            ),
            scenarioBuscarAvaliacao.injectOpen(
                rampUsersPerSec(1).to(5).during(Duration.ofSeconds(5)),
                constantUsersPerSec(5).during(Duration.ofSeconds(15)),
                rampUsersPerSec(5).to(1).during(Duration.ofSeconds(5))))
        .protocols(httpProtocolBuilder)
        .assertions(global().responseTime().max().lt(100), global().failedRequests().count().is(0L)
    );
  }

  private CadastrarUsuarioDTO gerarCadastrarUsuarioDTO() {
    return new CadastrarUsuarioDTO()
            .nome("usuario test")
            .email("usuario.test@gmail.com")
            .senha("usuario123");
  }

  private AtualizaReservaDTO gerarAtualizaReservaDTO() {
    return new AtualizaReservaDTO()
            .status(AtualizaReservaDTO.StatusEnum.FINALIZADA);
  }
}
