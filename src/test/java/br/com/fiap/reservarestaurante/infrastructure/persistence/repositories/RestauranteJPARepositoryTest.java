package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.reservarestaurante.utils.RestauranteHelper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class RestauranteJPARepositoryTest {

  AutoCloseable openMocks;
  @Mock private RestauranteJPARepository restauranteJPARepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  class Criar {

    @Test
    void devePermitirCriarRestaurante() {
      var restaurante = RestauranteHelper.gerarRestauranteJPAEntityNovo();

      RestauranteJPARepository restauranteJPARepositoryMock = mock(RestauranteJPARepository.class);

      when(restauranteJPARepositoryMock.save(any(RestauranteJPAEntity.class)))
          .thenReturn(restaurante);

      var restauranteArmazenado = restauranteJPARepositoryMock.save(restaurante);

      assertThat(restauranteArmazenado)
          .isInstanceOf(RestauranteJPAEntity.class)
          .isNotNull()
          .isEqualTo(restaurante);

      RestauranteHelper.validar(restauranteArmazenado, restaurante);

      verify(restauranteJPARepositoryMock, times(1)).save(restaurante);
    }
  }

  @Nested
  class Buscar {

    @Test
    void devePermitirBuscarTodosRestaurantes() {
      var restaurante1 =
          RestauranteHelper.gerarRestauranteJPAEntity("f05e678f-4adf-4de9-94b2-b36fdbdfd57d");
      var restaurante2 =
          RestauranteHelper.gerarRestauranteJPAEntity("5dfb6ef0-9b02-4fe5-bfba-042b0c94d385");
      var listOfRestaurante = Arrays.asList(restaurante1, restaurante2);

      when(restauranteJPARepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(listOfRestaurante));

      var resultadoPaginado = restauranteJPARepository.findAll(Pageable.unpaged());

      assertThat(resultadoPaginado)
          .hasSize(2)
          .containsExactlyInAnyOrder(restaurante1, restaurante2);

      verify(restauranteJPARepository, times(1)).findAll(Pageable.unpaged());
    }

    @Test
    void devePermitirBuscarRestaurantePorId() {
      var id = "b0a41850-23cf-4f5e-94f8-2bc22b6ed30a";
      var restaurante = RestauranteHelper.gerarRestauranteJPAEntity(id);

      when(restauranteJPARepository.findById(String.valueOf(any(String.class))))
          .thenReturn(Optional.of(restaurante));

      var restauranteOptional = restauranteJPARepository.findById(id);

      assertThat(restauranteOptional).isPresent().containsSame(restaurante);

      restauranteOptional.ifPresent(
          restauranteArmazenado -> RestauranteHelper.validar(restauranteArmazenado, restaurante));

      verify(restauranteJPARepository, times(1)).findById(id);
    }
  }
}
