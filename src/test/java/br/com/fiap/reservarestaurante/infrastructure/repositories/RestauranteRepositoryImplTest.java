package br.com.fiap.reservarestaurante.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.RestauranteJPARepository;
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

class RestauranteRepositoryImplTest {

  AutoCloseable openMocks;
  private RestauranteRepositoryImpl restauranteRepository;

  @Mock private RestauranteJPARepository restauranteJPARepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    restauranteRepository = new RestauranteRepositoryImpl(restauranteJPARepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  class Criar {

    @Test
    void devePermitirCriarRestaurante() {
      var restaurante = RestauranteHelper.gerarRestauranteNovo();
      var restauranteJPAEntity = RestauranteHelper.gerarRestauranteJPAEntityNovo();

      when(restauranteJPARepository.save(any(RestauranteJPAEntity.class)))
          .thenAnswer(i -> i.getArgument(0));

      var restauranteCriado = restauranteRepository.criar(restaurante);

      assertThat(restauranteCriado).isInstanceOf(Restaurante.class).isNotNull();

      RestauranteHelper.validar(restauranteCriado, restaurante);

      verify(restauranteJPARepository, times(1)).save(restauranteJPAEntity);
    }
  }

  @Nested
  class Atualizar {

    @Test
    void devePermitirAtualizarRestaurante() {

      var restaurante = RestauranteHelper.gerarRestaurante("caddcf4d-c37f-47fc-bc77-0699eab2f205");
      var restauranteJPAEntity = RestauranteJPAEntity.of(restaurante);

      when(restauranteJPARepository.save(any(RestauranteJPAEntity.class)))
          .thenAnswer(i -> i.getArgument(0));

      var restauranteAtualizado = restauranteRepository.atualizar(restaurante);

      assertThat(restauranteAtualizado).isNotNull().isInstanceOf(Restaurante.class);

      RestauranteHelper.validar(restauranteAtualizado, restaurante);

      verify(restauranteJPARepository, times(1)).save(restauranteJPAEntity);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarRestaurantePorId() {
      var restaurante = RestauranteHelper.gerarRestaurante("e9d39d9b-fb03-491e-9a62-859d07377e33");
      var restauranteId = restaurante.getId();
      var restauranteJPAEntity = RestauranteJPAEntity.of(restaurante);

      when(restauranteJPARepository.findById(any(String.class)))
          .thenReturn(Optional.of(restauranteJPAEntity));

      var restauranteOptional = restauranteRepository.buscarPorId(restauranteId);

      assertThat(restauranteOptional).isPresent().isInstanceOf(Optional.class);

      RestauranteHelper.validar(restauranteOptional.orElseThrow(), restaurante);

      verify(restauranteJPARepository, times(1)).findById(restauranteId.value());
    }

    @Test
    void devePermitirBuscarTodosRestaurantes() {
      var page = new Page(0, 10);
      var pageable = Pageable.ofSize(page.perPage()).withPage(page.currentPage());

      var r1 = RestauranteHelper.gerarRestaurante("caddcf4d-c37f-47fc-bc77-0699eab2f205");
      var r2 = RestauranteHelper.gerarRestaurante("b5657796-c261-4a72-8b86-680a52b8d206");

      var listOfRestauranteJPAEntity =
          Arrays.asList(RestauranteJPAEntity.of(r1), RestauranteJPAEntity.of(r2));

      when(restauranteJPARepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(listOfRestauranteJPAEntity));

      var resultadoPaginado = restauranteRepository.buscarTudo(page);

      assertThat(resultadoPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(resultadoPaginado).extracting(Pagination::perPage).isEqualTo(2);
      assertThat(resultadoPaginado).extracting(Pagination::total).isEqualTo(2L);
      assertThat(resultadoPaginado.items()).hasSize(2);

      verify(restauranteJPARepository, times(1)).findAll(pageable);
    }
  }
}
