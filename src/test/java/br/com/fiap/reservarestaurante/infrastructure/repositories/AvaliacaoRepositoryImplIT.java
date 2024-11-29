package br.com.fiap.reservarestaurante.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.AvaliacaoJPARepository;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class AvaliacaoRepositoryImplIT {

  private final AvaliacaoRepository avaliacaoRepository;
  private final AvaliacaoJPARepository avaliacaoJPARepository;

  @Autowired
  public AvaliacaoRepositoryImplIT(AvaliacaoJPARepository avaliacaoJPARepository) {
    this.avaliacaoRepository = new AvaliacaoRepositoryImpl(avaliacaoJPARepository);
    this.avaliacaoJPARepository = avaliacaoJPARepository;
  }

  @Test
  void devePermitirCriarTabela() {
    long totalTabelasCriada = avaliacaoJPARepository.count();
    assertThat(totalTabelasCriada).isNotNegative();
  }

  @Nested
  class Criar {
    @Test
    void devePermitirCriarAvaliacao() {
      var avaliacao = AvaliacaoHelper.gerarAvaliacaoNova();

      var avaliacaoCriada = avaliacaoRepository.criar(avaliacao);

      assertThat(avaliacaoCriada).isInstanceOf(Avaliacao.class).isNotNull();

      assertThat(avaliacaoCriada).extracting(Avaliacao::getId).isNotNull();
      assertThat(avaliacaoCriada)
          .extracting(Avaliacao::getReservaId)
          .isEqualTo(avaliacao.getReservaId());
      assertThat(avaliacaoCriada)
          .extracting(Avaliacao::getUsuarioId)
          .isEqualTo(avaliacao.getUsuarioId());
      assertThat(avaliacaoCriada)
          .extracting(Avaliacao::getCriacao)
          .isEqualTo(avaliacao.getCriacao());
      assertThat(avaliacaoCriada)
          .extracting(Avaliacao::getAlteracao)
          .isEqualTo(avaliacao.getAlteracao());
      assertThat(avaliacaoCriada)
          .extracting(Avaliacao::getExclusao)
          .isEqualTo(avaliacao.getExclusao());
      assertThat(avaliacaoCriada).extracting(Avaliacao::isAtivo).isEqualTo(avaliacao.isAtivo());
      assertThat(avaliacaoCriada)
          .extracting(Avaliacao::getComentario)
          .isEqualTo(avaliacao.getComentario());
      assertThat(avaliacaoCriada).extracting(Avaliacao::getNota).isEqualTo(avaliacao.getNota());
    }
  }

  @Nested
  class Atualizar {
    @Test
    void devePermitirAtualizarAvaliacao() {
      var id = avaliacaoRepository.criar(AvaliacaoHelper.gerarAvaliacaoNova()).getId();

      var avaliacaoOptional = avaliacaoRepository.buscarPorId(id);

      var novoComentario = "Atualizando meu comentario";
      var novaNota = BigDecimal.valueOf(1.3);

      var avaliacao = avaliacaoOptional.orElseThrow();
      avaliacao.atualizar(novoComentario, novaNota);

      var avaliacaoAtualizada = avaliacaoRepository.atualizar(avaliacao);

      assertThat(avaliacaoAtualizada).isNotNull().isInstanceOf(Avaliacao.class);

      AvaliacaoHelper.validar(avaliacaoAtualizada, avaliacao);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasAvaliacoes() {
      var page = new Page(0, 10);

      var resultadoPaginado = avaliacaoRepository.buscarTudo(page, true);

      assertThat(resultadoPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(resultadoPaginado).extracting(Pagination::perPage).isEqualTo(10);
      assertThat(resultadoPaginado).extracting(Pagination::total).isEqualTo(2L);
      assertThat(resultadoPaginado.items()).hasSize(2);
    }

    @Test
    void devePermitirBuscarAvaliacaoPorId() {
      var avaliacaoId = AvaliacaoId.from("d4322250-3fc0-49f2-99df-0ca87c40dc5a");

      var avaliacaoOptional = avaliacaoRepository.buscarPorId(avaliacaoId);

      assertThat(avaliacaoOptional).isPresent().isInstanceOf(Optional.class);
    }

    @Test
    void devePermitirBuscarAvaliacaoPorIdReserva() {
      var reservaId = "d750f2ba-568b-4d39-98fa-c525736be003";

      var avaliacaoOptional = avaliacaoRepository.buscarPorIdReserva(reservaId);

      assertThat(avaliacaoOptional).isPresent().isInstanceOf(Optional.class);
    }

    @Test
    void devePermitirBuscarAvaliacoesPorIdRestaurante() {
      var page = new Page(0, 10);
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f6";

      var resultadoPaginado = avaliacaoRepository.buscarPorIdRestaurante(page, restauranteId);

      assertThat(resultadoPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(resultadoPaginado).extracting(Pagination::perPage).isEqualTo(10);
      assertThat(resultadoPaginado).extracting(Pagination::total).isEqualTo(2L);
      assertThat(resultadoPaginado.items()).hasSize(2);
    }

    @Test
    void devePermitirBuscarNotaRestaurantePorIdRestaurante() {
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f6";

      var avaliacaoOptional = avaliacaoRepository.buscarNotaRestaurante(restauranteId);

      assertThat(avaliacaoOptional).isPresent().isInstanceOf(Optional.class);

      avaliacaoOptional.ifPresent(
          avaliacaoArmazenada -> {
            assertThat(avaliacaoArmazenada.restauranteId()).isEqualTo(restauranteId);
            assertThat(avaliacaoArmazenada.avaliacoes()).isEqualTo(2L);
            assertThat(avaliacaoArmazenada.nota()).isEqualTo(BigDecimal.valueOf(2.8));
          });
    }
  }
}
