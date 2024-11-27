package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.delete.RestauranteDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCase;
import br.com.fiap.reservarestaurante.infrastructure.mappers.RestauranteMapper;
import br.com.fiap.restaurante.api.RestaurantesApi;
import br.com.fiap.restaurante.model.*;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestauranteController implements RestaurantesApi {

  private static final RestauranteMapper mapper = RestauranteMapper.INSTANCE;
  private final RestauranteGetByIdUseCase restauranteGetByIdUseCase;
  private final RestauranteListUseCase restauranteListUseCase;
  private final RestauranteCreateUseCase restaurenteCreateUseCase;
  private final RestauranteUpdateUseCase restauranteUpdateUseCase;
  private final RestauranteDeleteUseCase restauranteDeleteUseCase;

  @Override
  public ResponseEntity<RestauranteDTO> buscarRestaurantePorId(String id) {
    final var output = mapper.toDTO(restauranteGetByIdUseCase.execute(id));
    return ResponseEntity.ok(output);
  }

  @Override
  public ResponseEntity<PaginadaRestauranteDTO> buscarRestaurantes(
      Integer pagina,
      Integer porPagina,
      String cidade,
      String estado,
      Double avaliacaoMinima,
      String tipoCozinhaId) {
    final var input = mapper.fromDTO(new Page(pagina, porPagina));
    final var restaurante = restauranteListUseCase.execute(input).mapItems(mapper::toDTO);

    return ResponseEntity.ok(
        new PaginadaRestauranteDTO()
            .itens(restaurante.items())
            .pagina(restaurante.currentPage())
            .porPagina(restaurante.perPage())
            .total(restaurante.total()));
  }

  @Override
  public ResponseEntity<RestauranteDTO> cadastrarRestaurante(final CriaRestauranteDTO body) {
    final var useCaseInput = mapper.fromDTO(body);
    final var useCaseOutput = restaurenteCreateUseCase.execute(useCaseInput);
    var uri = URI.create("/restaurantes/" + useCaseOutput.id());

    return ResponseEntity.created(uri).body(mapper.toDTO(useCaseOutput));
  }

  @Override
  public ResponseEntity<RestauranteDTO> atualizarRestaurante(
      String id, AtualizaRestauranteDTO body) {
    final var input = mapper.fromDTO(id, body);
    final var output = mapper.toDTO(restauranteUpdateUseCase.execute(input));
    return ResponseEntity.ok(output);
  }

  @Override
  public ResponseEntity<Void> deletarRestaurante(String id) {
    restauranteDeleteUseCase.execute(id);
    return ResponseEntity.noContent().build();
  }
}
