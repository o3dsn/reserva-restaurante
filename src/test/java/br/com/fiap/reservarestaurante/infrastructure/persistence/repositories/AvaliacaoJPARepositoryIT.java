package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import br.com.fiap.reservarestaurante.utils.AvaliacaoHelper;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class AvaliacaoJPARepositoryIT {

  private final AvaliacaoJPARepository avaliacaoJPARepository;

  @Autowired
  public AvaliacaoJPARepositoryIT(AvaliacaoJPARepository avaliacaoJPARepository) {
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
      var avaliacao = AvaliacaoHelper.gerarAvaliacaoJPAEntityNova();

      var avaliacaoArmazenada = avaliacaoJPARepository.save(avaliacao);

      assertThat(avaliacaoArmazenada)
          .isInstanceOf(AvaliacaoJPAEntity.class)
          .isNotNull()
          .isEqualTo(avaliacao);

      AvaliacaoHelper.validar(avaliacaoArmazenada, avaliacao);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasAvaliacoesAtivas() {
      var resultadoPaginado = avaliacaoJPARepository.findAllByAtivo(Pageable.unpaged(), true);

      assertThat(resultadoPaginado).hasSizeGreaterThanOrEqualTo(2);

      assertThat(resultadoPaginado.getContent().get(0)).isInstanceOf(AvaliacaoJPAEntity.class);
    }

    @Test
    void devePermitirBuscarTodasAvaliacoesInativas() {
      var resultadoPaginado = avaliacaoJPARepository.findAllByAtivo(Pageable.unpaged(), false);

      assertThat(resultadoPaginado).hasSizeGreaterThanOrEqualTo(1);

      assertThat(resultadoPaginado.getContent().get(0)).isInstanceOf(AvaliacaoJPAEntity.class);
    }

    @Test
    void devePermitirBuscarAvaliacaoPorId() {
      var id = "73168510-2714-4cd9-a2e6-149b3fc862d6";

      var avaliacaoOptional = avaliacaoJPARepository.findById(id);

      avaliacaoOptional.ifPresent(
          avaliacaoArmazenada -> {
            assertThat(avaliacaoArmazenada).extracting(AvaliacaoJPAEntity::getId).isEqualTo(id);
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getReservaId)
                .isEqualTo("093bff48-6e42-4939-99d9-959f61b41fdd");
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getUsuarioId)
                .isEqualTo("afaa347c-b698-4e51-b71a-d861c5f480ba");
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getCriacao)
                .isEqualTo(Instant.parse("2024-11-01T03:00:00.100Z"));
            assertThat(avaliacaoArmazenada).extracting(AvaliacaoJPAEntity::getAlteracao).isNull();
            assertThat(avaliacaoArmazenada).extracting(AvaliacaoJPAEntity::getExclusao).isNull();
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::isAtivo)
                .isEqualTo(Boolean.TRUE);
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getComentario)
                .isEqualTo(
                    "O empenho em analisar a execução dos pontos do programa nos obriga à análise das condições inegavelmente apropriadas.");
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getNota)
                .isEqualTo(BigDecimal.valueOf(4.3));
          });
    }

    @Test
    void devePermitirBuscarAvaliacaoPorIdReserva() {
      var reservaId = "bb0bd99f-9ae0-4901-b484-8438fb72308d";

      var avaliacaoOptional = avaliacaoJPARepository.findByReservaId(reservaId);

      avaliacaoOptional.ifPresent(
          avaliacaoArmazenada -> {
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getId)
                .isEqualTo("d4322250-3fc0-49f2-99df-0ca87c40dc5a");
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getReservaId)
                .isEqualTo(reservaId);
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getUsuarioId)
                .isEqualTo("afaa347c-b698-4e51-b71a-d861c5f480ba");
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getCriacao)
                .isEqualTo(Instant.parse("2024-10-01T03:00:00.200Z"));
            assertThat(avaliacaoArmazenada).extracting(AvaliacaoJPAEntity::getAlteracao).isNull();
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getExclusao)
                .isEqualTo(Instant.parse("2024-10-07T03:00:00.200Z"));
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::isAtivo)
                .isEqualTo(Boolean.FALSE);
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getComentario)
                .isEqualTo(
                    "O empenho em analisar a execução dos pontos do programa nos obriga à análise das condições inegavelmente apropriadas.");
            assertThat(avaliacaoArmazenada)
                .extracting(AvaliacaoJPAEntity::getNota)
                .isEqualTo(BigDecimal.valueOf(2.7));
          });
    }

    @Test
    void devePermitirBuscarAvaliacoesPorIdRestaurante() {
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f6";

      var resultadoPaginado =
          avaliacaoJPARepository.buscarPorIdRestaurante(Pageable.unpaged(), restauranteId);

      assertThat(resultadoPaginado).hasSize(2);

      assertThat(resultadoPaginado.getContent().get(0)).isInstanceOf(AvaliacaoJPAEntity.class);
    }

    @Test
    void devePermitirBuscarNotaRestaurantePorIdRestaurante() {
      var restauranteId = "177d17ed-9b8b-480f-becf-bb57c896f0f6";

      var notaRestauranteOptional = avaliacaoJPARepository.buscarNotaRestaurante(restauranteId);

      notaRestauranteOptional.ifPresent(
          notaRestauranteArmazenada -> {
            assertThat(notaRestauranteArmazenada)
                .extracting(NotaRestaurante::restauranteId)
                .isEqualTo(restauranteId);
            assertThat(notaRestauranteArmazenada)
                .extracting(NotaRestaurante::avaliacoes)
                .isEqualTo(2L);
            assertThat(notaRestauranteArmazenada)
                .extracting(NotaRestaurante::nota)
                .isEqualTo(BigDecimal.valueOf(2.8));
          });
    }
  }
}
