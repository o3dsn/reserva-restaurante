package br.com.fiap.reservarestaurante.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.RestauranteJPARepository;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class RestauranteRepositoryImplIT {

  private final RestauranteRepository restauranteRepository;

  @Autowired
  public RestauranteRepositoryImplIT(RestauranteJPARepository restauranteJPARepository) {
    this.restauranteRepository = new RestauranteRepositoryImpl(restauranteJPARepository);
  }

  @Nested
  class Criar {

    @Test
    void devePermitirCriarRestaurante() {
      var restaurante = RestauranteHelper.gerarRestauranteNovo();

      var restauranteCriado = restauranteRepository.criar(restaurante);

      assertThat(restauranteCriado).isInstanceOf(Restaurante.class).isNotNull();
      assertThat(restauranteCriado).extracting(Restaurante::getId).isNotNull();
      assertThat(restauranteCriado)
          .extracting(Restaurante::getNome)
          .isEqualTo(restaurante.getNome());
      assertThat(restauranteCriado)
          .extracting(Restaurante::getEndereco)
          .isEqualTo(restaurante.getEndereco());
    }
  }

  @Nested
  class Atualizar {

    @Test
    void devePermitirAtualizarRestaurante() {
      var id = restauranteRepository.criar(RestauranteHelper.gerarRestauranteNovo()).getId();

      var restauranteOptional = restauranteRepository.buscarPorId(id);

      var novoNome = "Restaurante Exemplo";
      var novoEndereco = "Rua Exemplo, 123";

      var restaurante = restauranteOptional.orElseThrow();

      var restauranteAtualizado = restauranteRepository.atualizar(restaurante);

      assertThat(restauranteAtualizado).isNotNull().isInstanceOf(Restaurante.class);
      assertThat(restauranteAtualizado).extracting(Restaurante::getNome).isEqualTo(novoNome);
      assertThat(restauranteAtualizado)
          .extracting(Restaurante::getEndereco)
          .isEqualTo(novoEndereco);
    }
  }

  @Nested
  class Buscar {

    @Test
    void devePermitirBuscarTodosRestaurantes() {
      var page = new Page(0, 10);

      var resultadoPaginado = restauranteRepository.buscarTudo(page);

      assertThat(resultadoPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(resultadoPaginado).extracting(Pagination::perPage).isEqualTo(10);
      assertThat(resultadoPaginado).extracting(Pagination::total).isEqualTo(2L);
      assertThat(resultadoPaginado.items()).hasSize(2);
    }

    @Test
    void devePermitirBuscarRestaurantePorId() {
      Restaurante restaurante = RestauranteHelper.gerarRestauranteNovo();
      Restaurante restauranteCriado = restauranteRepository.criar(restaurante);

      var restauranteId = restauranteCriado.getId();

      var restauranteOptional = restauranteRepository.buscarPorId(restauranteId);

      assertThat(restauranteOptional).isPresent().isInstanceOf(Optional.class);
    }
  }
}
