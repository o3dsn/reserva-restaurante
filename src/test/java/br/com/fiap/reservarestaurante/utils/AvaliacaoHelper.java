package br.com.fiap.reservarestaurante.utils;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.avaliacao.model.AtualizaAvaliacaoDTO;
import br.com.fiap.avaliacao.model.CriaAvaliacaoDTO;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import java.math.BigDecimal;
import java.time.Instant;

public final class AvaliacaoHelper {

  public static final String RESTAURANTE_ID = "2cc9c534-9900-44b7-a0d2-551d38d82953";
  public static final String RESERVA_ID = "e676e9ca-8a65-4a7d-b52a-def2faa150d9";
  public static final String USUARIO_ID = "dd9e1b59-3a07-4dec-becc-a87888c26bd2";
  private static final String COMENTARIO =
      "Um comentario com mais de 50 caracteres sobre meu restaurante favorito";
  private static final BigDecimal NOTA = BigDecimal.valueOf(4.5);
  private AvaliacaoHelper() {
    throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
  }

  public static Avaliacao gerarAvaliacaoNova() {
    return Avaliacao.nova(RESERVA_ID, USUARIO_ID, COMENTARIO, NOTA);
  }

  public static Avaliacao gerarAvaliacaoExcluida(String id) {
    return gerar(id, false, "2024-12-05T10:15:30.00Z");
  }

  public static Avaliacao gerar(String id, boolean ativo, String exclusao) {
    return new Avaliacao(
            AvaliacaoId.from(id),
            RESERVA_ID,
            USUARIO_ID,
            Instant.parse("2024-12-03T10:15:30.00Z"),
            null,
            ativo ? null : Instant.parse(exclusao),
            ativo,
            COMENTARIO,
            NOTA);
  }

  public static Avaliacao gerarAvaliacao(String id) {
    return gerar(id, true, null);
  }

  public static Avaliacao gerarAvaliacaoAlterada(String id) {
    var atualizaAvaliacaoDTO = gerarAtualizaAvaliacaoDTO();
    return new Avaliacao(
            AvaliacaoId.from(id),
            RESERVA_ID,
            USUARIO_ID,
            Instant.parse("2024-12-03T10:15:30.00Z"),
            Instant.parse("2025-01-01T10:15:30.200Z"),
            null,
            true,
            atualizaAvaliacaoDTO.getComentario(),
            atualizaAvaliacaoDTO.getNota());
  }

  public static AvaliacaoJPAEntity gerarAvaliacaoJPAEntityNova() {
    var avaliacao = gerarAvaliacaoNova();
    return AvaliacaoJPAEntity.of(avaliacao);
  }

  public static AvaliacaoJPAEntity gerarAvaliacaoJPAEntity(String id) {
    var avaliacao = gerarAvaliacao(id);
    return AvaliacaoJPAEntity.of(avaliacao);
  }

  public static CriaAvaliacaoDTO gerarCriaAvaliacaoDTONova() {
    return new CriaAvaliacaoDTO().comentario(COMENTARIO).nota(NOTA);
  }

  public static AtualizaAvaliacaoDTO gerarAtualizaAvaliacaoDTO() {
    return new AtualizaAvaliacaoDTO()
        .comentario(COMENTARIO + " Atualizado")
        .nota(BigDecimal.valueOf(3.25));
  }

  public static NotaRestaurante gerarNotaRestaurante() {
    return new NotaRestaurante(AvaliacaoHelper.RESTAURANTE_ID, 3L, BigDecimal.valueOf(4.1));
  }

  public static AvaliacaoCreateUseCaseInput gerarAvaliacaoCreateUseCaseInput() {
    return new AvaliacaoCreateUseCaseInput(RESERVA_ID, USUARIO_ID, COMENTARIO, NOTA);
  }

  public static AvaliacaoUpdateUseCaseInput gerarAvaliacaoUpdateUseCaseInput(String id) {
    return new AvaliacaoUpdateUseCaseInput(id, COMENTARIO + " Atualizado", NOTA);
  }

  public static AvaliacaoListUseCaseInput gerarAvaliacaoListUseCaseInput() {
    return new AvaliacaoListUseCaseInput(new Page(0, 10), true);
  }

  public static AvaliacaoListByIdRestauranteUseCaseInput gerarAvaliacaoListByIdRestauranteUseCaseInput() {
    return new AvaliacaoListByIdRestauranteUseCaseInput(new Page(0, 10), RESTAURANTE_ID);
  }

  public static void validar(Avaliacao avaliacaoArmazenada, Avaliacao avaliacao) {
    validar(AvaliacaoJPAEntity.of(avaliacaoArmazenada), AvaliacaoJPAEntity.of(avaliacao));
  }

  public static void validar(AvaliacaoJPAEntity avaliacaoArmazenada, AvaliacaoJPAEntity avaliacao) {
    assertThat(avaliacaoArmazenada).isNotNull();
    assertThat(avaliacao).isNotNull();

    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getId)
        .isEqualTo(avaliacao.getId());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getReservaId)
        .isEqualTo(avaliacao.getReservaId());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getUsuarioId)
        .isEqualTo(avaliacao.getUsuarioId());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getCriacao)
        .isEqualTo(avaliacao.getCriacao());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getAlteracao)
        .isEqualTo(avaliacao.getAlteracao());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getExclusao)
        .isEqualTo(avaliacao.getExclusao());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::isAtivo)
        .isEqualTo(avaliacao.isAtivo());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getComentario)
        .isEqualTo(avaliacao.getComentario());
    assertThat(avaliacaoArmazenada)
        .extracting(AvaliacaoJPAEntity::getNota)
        .isEqualTo(avaliacao.getNota());
  }
}
