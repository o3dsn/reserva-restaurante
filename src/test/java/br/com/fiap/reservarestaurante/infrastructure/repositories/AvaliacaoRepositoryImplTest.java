package br.com.fiap.reservarestaurante.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.AvaliacaoJPARepository;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class AvaliacaoRepositoryImplTest {

  AutoCloseable openMocks;
  private AvaliacaoRepositoryImpl avaliacaoRepository;
  @Mock private AvaliacaoJPARepository avaliacaoJPARepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    avaliacaoRepository = new AvaliacaoRepositoryImpl(avaliacaoJPARepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  class Criar {
    @Test
    void devePermitirCriarAvaliacao() {
      var avaliacao = AvaliacaoHelper.gerarAvaliacaoNova();
      var avaliacaoJPAEntity = AvaliacaoHelper.gerarAvaliacaoJPAEntityNova();

      when(avaliacaoJPARepository.save(any(AvaliacaoJPAEntity.class)))
          .thenAnswer(i -> i.getArgument(0));

      var avaliacaoCriada = avaliacaoRepository.criar(avaliacao);

      assertThat(avaliacaoCriada).isInstanceOf(Avaliacao.class).isNotNull();

      AvaliacaoHelper.validar(avaliacaoCriada, avaliacao);

      verify(avaliacaoJPARepository, times(1)).save(avaliacaoJPAEntity);
    }
  }

  @Nested
  class Atualizar {
    @Test
    void devePermitirAtualizarAvaliacao() {
      var novoComentario = "Atualizando meu comentario";
      var novaNota = BigDecimal.valueOf(1.3);

      var avaliacao = AvaliacaoHelper.gerarAvaliacao("4e83ce7d-ce3a-4882-8e50-f895e77d77a4");
      avaliacao.atualizar(novoComentario, novaNota);
      var avaliacaoJPAEntity = AvaliacaoJPAEntity.of(avaliacao);

      when(avaliacaoJPARepository.save(any(AvaliacaoJPAEntity.class)))
          .thenAnswer(i -> i.getArgument(0));

      var avaliacaoAtualizada = avaliacaoRepository.atualizar(avaliacao);

      assertThat(avaliacaoAtualizada).isNotNull().isInstanceOf(Avaliacao.class);

      AvaliacaoHelper.validar(avaliacaoAtualizada, avaliacao);

      verify(avaliacaoJPARepository, times(1)).save(avaliacaoJPAEntity);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasAvaliacoes() {
      var page = new Page(0, 10);
      var pageable = Pageable.ofSize(page.perPage()).withPage(page.currentPage());

      var av1 = AvaliacaoHelper.gerarAvaliacao("caddcf4d-c37f-47fc-bc77-0699eab2f205");
      var av2 = AvaliacaoHelper.gerarAvaliacao("b5657796-c261-4a72-8b86-680a52b8d206");

      var listOfAvaliacaoJPAEntity =
          Arrays.asList(AvaliacaoJPAEntity.of(av1), AvaliacaoJPAEntity.of(av2));

      when(avaliacaoJPARepository.findAllByAtivo(any(Pageable.class), anyBoolean()))
          .thenReturn(new PageImpl<>(listOfAvaliacaoJPAEntity));

      var resultadoPaginado = avaliacaoRepository.buscarTudo(page, true);

      assertThat(resultadoPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(resultadoPaginado).extracting(Pagination::perPage).isEqualTo(2);
      assertThat(resultadoPaginado).extracting(Pagination::total).isEqualTo(2L);
      assertThat(resultadoPaginado.items()).hasSize(2);

      verify(avaliacaoJPARepository, times(1)).findAllByAtivo(pageable, true);
    }

    @Test
    void devePermitirBuscarAvaliacaoPorId() {
      var avaliacao = AvaliacaoHelper.gerarAvaliacao("4af2ec02-fa75-4a6a-83f7-7376ac2df0a7");
      var avaliacaoId = avaliacao.getId();
      var avaliacaoJPAEntity = AvaliacaoJPAEntity.of(avaliacao);

      when(avaliacaoJPARepository.findById(any(String.class)))
          .thenReturn(Optional.of(avaliacaoJPAEntity));

      var avaliacaoOptional = avaliacaoRepository.buscarPorId(avaliacaoId);

      assertThat(avaliacaoOptional).isPresent().isInstanceOf(Optional.class);

      AvaliacaoHelper.validar(avaliacaoOptional.orElseThrow(), avaliacao);

      verify(avaliacaoJPARepository, times(1)).findById(avaliacaoId.value());
    }

    @Test
    void devePermitirBuscarAvaliacaoPorIdReserva() {
      var avaliacaoJPAEntity =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("7797edd5-30d5-44cd-948b-64b9509409e6");
      var avaliacao = avaliacaoJPAEntity.toAvaliacao();

      when(avaliacaoJPARepository.findByReservaId(any(String.class)))
          .thenReturn(Optional.of(avaliacaoJPAEntity));

      var avaliacaoOptional = avaliacaoRepository.buscarPorIdReserva(AvaliacaoHelper.RESERVA_ID);

      assertThat(avaliacaoOptional).isPresent().isInstanceOf(Optional.class);

      AvaliacaoHelper.validar(avaliacaoOptional.orElseThrow(), avaliacao);

      verify(avaliacaoJPARepository, times(1)).findByReservaId(AvaliacaoHelper.RESERVA_ID);
    }

    @Test
    void devePermitirBuscarAvaliacoesPorIdRestaurante() {
      var page = new Page(0, 10);
      var pageable = Pageable.ofSize(page.perPage()).withPage(page.currentPage());

      var avaliacaoJPAEntity =
          AvaliacaoHelper.gerarAvaliacaoJPAEntity("e9d39d9b-fb03-491e-9a62-859d07377e33");

      when(avaliacaoJPARepository.buscarPorIdRestaurante(any(Pageable.class), any(String.class)))
          .thenReturn(new PageImpl<>(List.of(avaliacaoJPAEntity)));

      var resultadoPaginado =
          avaliacaoRepository.buscarPorIdRestaurante(page, AvaliacaoHelper.RESTAURANTE_ID);

      assertThat(resultadoPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(resultadoPaginado).extracting(Pagination::perPage).isEqualTo(1);
      assertThat(resultadoPaginado).extracting(Pagination::total).isEqualTo(1L);
      assertThat(resultadoPaginado.items()).hasSize(1);

      verify(avaliacaoJPARepository, times(1))
          .buscarPorIdRestaurante(pageable, AvaliacaoHelper.RESTAURANTE_ID);
    }

    @Test
    void devePermitirBuscarNotaRestaurantePorIdRestaurante() {
      var notaRestaurante = AvaliacaoHelper.gerarNotaRestaurante();

      when(avaliacaoJPARepository.buscarNotaRestaurante(any(String.class)))
          .thenReturn(Optional.of(notaRestaurante));

      var avaliacaoOptional =
          avaliacaoRepository.buscarNotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID);

      assertThat(avaliacaoOptional).isPresent().isInstanceOf(Optional.class);

      avaliacaoOptional.ifPresent(
          avaliacaoArmazenada -> {
            assertThat(avaliacaoArmazenada.restauranteId())
                .isEqualTo(AvaliacaoHelper.RESTAURANTE_ID);
            assertThat(avaliacaoArmazenada.avaliacoes()).isEqualTo(3L);
            assertThat(avaliacaoArmazenada.nota()).isEqualTo(BigDecimal.valueOf(4.1));
          });

      verify(avaliacaoJPARepository, times(1))
          .buscarNotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID);
    }
  }
}
