package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class AvaliacaoJPARepositoryTest {

  AutoCloseable openMocks;
  @Mock private AvaliacaoJPARepository avaliacaoJPARepository;

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
    void devePermitirCriarAvaliacao() {
      var avaliacao = AvaliacaoHelper.gerarAvaliacaoJPAEntityNova();

      when(avaliacaoJPARepository.save(any(AvaliacaoJPAEntity.class))).thenReturn(avaliacao);

      var avaliacaoArmazenada = avaliacaoJPARepository.save(avaliacao);

      assertThat(avaliacaoArmazenada)
          .isInstanceOf(AvaliacaoJPAEntity.class)
          .isNotNull()
          .isEqualTo(avaliacao);

      AvaliacaoHelper.validar(avaliacaoArmazenada, avaliacao);

      verify(avaliacaoJPARepository, times(1)).save(avaliacao);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasAvaliacoes() {
      var avaliacao1 =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("7f93c846-d3e5-4e85-8eef-18d3cf12c37c");
      var avaliacao2 =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("091a4ecb-cfed-446c-bfce-cf54da57535f");
      var listOfAvaliacao = Arrays.asList(avaliacao1, avaliacao2);

      when(avaliacaoJPARepository.findAllByAtivo(any(Pageable.class), anyBoolean()))
          .thenReturn(new PageImpl<>(listOfAvaliacao));

      var resultadoPaginado = avaliacaoJPARepository.findAllByAtivo(Pageable.unpaged(), true);

      assertThat(resultadoPaginado).hasSize(2).containsExactlyInAnyOrder(avaliacao1, avaliacao2);

      verify(avaliacaoJPARepository, times(1)).findAllByAtivo(Pageable.unpaged(), true);
    }

    @Test
    void devePermitirBuscarAvaliacaoPorId() {
      var id = "2c565267-14b0-4bf9-967b-6c59262ed89e";
      var avaliacao = AvaliacaoHelper.gerarAvaliacaoJPAEntity(id);

      when(avaliacaoJPARepository.findById(any(String.class))).thenReturn(Optional.of(avaliacao));

      var avaliacaoOptional = avaliacaoJPARepository.findById(id);

      assertThat(avaliacaoOptional).isPresent().containsSame(avaliacao);

      avaliacaoOptional.ifPresent(
          avaliacaoArmazenada -> AvaliacaoHelper.validar(avaliacaoArmazenada, avaliacao));

      verify(avaliacaoJPARepository, times(1)).findById(id);
    }

    @Test
    void devePermitirBuscarAvaliacaoPorIdReserva() {
      var avaliacao =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("15548fc1-bdbe-41cc-bc69-e4e07c98f92d");

      when(avaliacaoJPARepository.findByReservaId(any(String.class)))
          .thenReturn(Optional.of(avaliacao));

      var avaliacaoOptional = avaliacaoJPARepository.findByReservaId(AvaliacaoHelper.RESERVA_ID);

      assertThat(avaliacaoOptional).isPresent().containsSame(avaliacao);

      avaliacaoOptional.ifPresent(
          avaliacaoArmazenada -> AvaliacaoHelper.validar(avaliacaoArmazenada, avaliacao));

      verify(avaliacaoJPARepository, times(1)).findByReservaId(AvaliacaoHelper.RESERVA_ID);
    }

    @Test
    void devePermitirBuscarAvaliacoesPorIdRestaurante() {
      var avaliacao1 =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("312f4c63-6533-47f7-b876-658655594107");
      var avaliacao2 =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("2d6cf92e-a540-4c4a-8b32-181959a47c1c");
      var listOfAvaliacao = Arrays.asList(avaliacao1, avaliacao2);

      when(avaliacaoJPARepository.buscarPorIdRestaurante(any(Pageable.class), any(String.class)))
          .thenReturn(new PageImpl<>(listOfAvaliacao));

      var resultadoPaginado =
          avaliacaoJPARepository.buscarPorIdRestaurante(
              Pageable.unpaged(), AvaliacaoHelper.RESTAURANTE_ID);

      assertThat(resultadoPaginado).hasSize(2).containsExactlyInAnyOrder(avaliacao1, avaliacao2);

      verify(avaliacaoJPARepository, times(1))
          .buscarPorIdRestaurante(Pageable.unpaged(), AvaliacaoHelper.RESTAURANTE_ID);
    }

    @Test
    void devePermitirBuscarNotaRestaurantePorIdRestaurante() {
      var notaRestaurante = AvaliacaoHelper.gerarNotaRestaurante();

      when(avaliacaoJPARepository.buscarNotaRestaurante(any(String.class)))
          .thenReturn(Optional.of(notaRestaurante));

      var notaRestauranteOptional =
          avaliacaoJPARepository.buscarNotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID);

      assertThat(notaRestauranteOptional).isPresent().containsSame(notaRestaurante);

      notaRestauranteOptional.ifPresent(
          notaRestauranteArmazenada -> {
            assertThat(notaRestauranteArmazenada)
                .extracting(NotaRestaurante::restauranteId)
                .isEqualTo(notaRestaurante.restauranteId());
            assertThat(notaRestauranteArmazenada)
                .extracting(NotaRestaurante::avaliacoes)
                .isEqualTo(notaRestaurante.avaliacoes());
            assertThat(notaRestauranteArmazenada)
                .extracting(NotaRestaurante::nota)
                .isEqualTo(notaRestaurante.nota());
          });

      verify(avaliacaoJPARepository, times(1))
          .buscarNotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID);
    }
  }
}
