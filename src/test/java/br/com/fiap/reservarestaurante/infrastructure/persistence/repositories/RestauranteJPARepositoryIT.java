package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class RestauranteJPARepositoryIT {

  private final RestauranteJPARepository restauranteJPARepository;

  @Autowired
  public RestauranteJPARepositoryIT(RestauranteJPARepository repository) {
    this.restauranteJPARepository = repository;
  }

  @Nested
  class Criar {

    @Test
    void devePermitirCriarRestaurante() {
      var restaurante = RestauranteHelper.gerarRestauranteJPAEntityNovo();
      var restauranteJPA = restauranteJPARepository.save(restaurante);

      assertThat(restauranteJPA)
          .isInstanceOf(RestauranteJPAEntity.class)
          .isNotNull()
          .isEqualTo(restaurante);

      RestauranteHelper.validar(restaurante, restauranteJPA);
    }
  }

  @Nested
  class Buscar {

    @Test
    void devePermitirBuscarTodosRestaurantes() {
      var resultadoPaginado = restauranteJPARepository.findAll(Pageable.unpaged());

      assertThat(resultadoPaginado).hasSizeGreaterThanOrEqualTo(1);

      assertThat(resultadoPaginado.getContent().get(0)).isInstanceOf(RestauranteJPAEntity.class);
    }
  }
}
