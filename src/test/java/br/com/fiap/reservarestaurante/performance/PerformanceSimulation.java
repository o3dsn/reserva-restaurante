package br.com.fiap.reservarestaurante.performance;

import br.com.fiap.avaliacao.model.CriaAvaliacaoDTO;
import br.com.fiap.reserva.model.AtualizaReservaDTO;
import br.com.fiap.reserva.model.CriaReservaDTO;
import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import br.com.fiap.reservarestaurante.utils.JsonUtil;
import br.com.fiap.restaurante.model.CriaRestauranteDTO;
import br.com.fiap.restaurante.model.RestauranteDTO;
import io.gatling.javaapi.core.ActionBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.function.Function;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class PerformanceSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocolBuilder =
            http
                .baseUrl("http://localhost:8080")
                .header("Content-Type","application/json");
    final String criaRestauranteDTO =
            JsonUtil.toJson(criarRestauranteRequest());
    final String usuarioId = "0e7b970b-7d9c-4db8-b9c6-8da285ed81d0";
    final String criaReservaDTO =
            JsonUtil.toJson(criarReservaRequest());
    final String atualizaReservaDTO =
            JsonUtil.toJson(atualizarReservaDTO());
    final String criaAvaliacaoDTO =
            JsonUtil.toJson(AvaliacaoHelper.gerarCriaAvaliacaoDTONova());

    private CriaRestauranteDTO criarRestauranteRequest() {
        return new CriaRestauranteDTO()
                .nome("performance")
                .descricao("cria Performance Restaurante")
                .horarioFechamento("22:00:00")
                .tipoCozinhaId("Cozinha")
                .telefone("+55 11 91111-1111")
                .estado("XX")
                .cidade("cidade")
                .faixaPreco("$$ - $$$")
                .endereco("Rua da performance, 404")
                .bairro("bairro")
                .email("performance@gmail.com")
                .horarioAbertura("06:00:00");
    }

    private CriaReservaDTO criarReservaRequest() {
        return new CriaReservaDTO()
                .comentario("um comentario da performance")
                .dataHorarioReserva(OffsetDateTime.parse(Instant.now().toString()));
    }

    private AtualizaReservaDTO atualizarReservaDTO() {
        return new AtualizaReservaDTO()
                .status(AtualizaReservaDTO.StatusEnum.FINALIZADA);
    }

  ActionBuilder adicionarRestauranteRequest =
      http("adicionar restaurante")
          .post("/restaurantes")
          .body(StringBody(criaRestauranteDTO))
          .check(status().is(201))
          .check(jsonPath("$.id").saveAs("restauranteId"));

  ActionBuilder adicionarReservaRequest =
     http("adicionar reserva")
        .post("/reservas")
                .body(StringBody(criaReservaDTO))
                .queryParam("restauranteId", "#{restauranteId}")
                .queryParam("usuarioId", usuarioId)
                .check(status().is(201))
                .check(jsonPath("$.id").saveAs("reservaId"));

    ActionBuilder atualizarReservaRequest =
            http("atualizar reserva")
                    .patch("/reservas/%s".formatted("#{reservaId}"))
                    .body(StringBody(atualizaReservaDTO))
                    .check(status().is(200))
                    .check(bodyString().saveAs("responseReserva"));

    ActionBuilder adicionarAvaliacaoRequest =
            http("adicionar avaliacao")
                    .post("/avaliacoes")
                    .queryParam("reservaId", "#{reservaId}")
                    .queryParam("usuarioId", usuarioId)
                    .body(StringBody(criaAvaliacaoDTO))
                    .check(status().is(201))
                    .check(jsonPath("$.id").saveAs("avaliacaoId"));

    ActionBuilder buscarAvaliacaoRequest =
            http("buscar avaliacao")
                    .get("/avaliacoes/%s".formatted("#{avaliacaoId}"))
                    .check(status().is(200));

  ScenarioBuilder scenarioAdicionarAvaliacao =
      scenario("adicionar avaliacao")
          .exec(adicionarRestauranteRequest)
          .exec(adicionarReservaRequest)
          .exec(atualizarReservaRequest)
          .exec(adicionarAvaliacaoRequest);

    ScenarioBuilder scenarioBuscarAvaliacao =
        scenario("buscar avaliacao")
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
                    rampUsersPerSec(5).to(1).during(Duration.ofSeconds(5))
            )
    )
        .protocols(httpProtocolBuilder)
        .assertions(global().responseTime().max().lt(100), global().failedRequests().count().is(0L));
    }
}