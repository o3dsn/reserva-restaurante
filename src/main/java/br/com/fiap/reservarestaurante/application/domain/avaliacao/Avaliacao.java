package br.com.fiap.reservarestaurante.application.domain.avaliacao;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Avaliacao {

  private AvaliacaoId id;
  private String reservaId;
  private String usuarioId;
  private Instant criacao;
  private Instant alteracao;
  private Instant exclusao;
  private boolean ativo;
  private String comentario;
  private BigDecimal nota;

  public static Avaliacao nova(
      String reservaId, String usuarioId, String comentario, BigDecimal nota) {
    return new Avaliacao(
        new AvaliacaoId(null),
        reservaId,
        usuarioId,
        Instant.now(),
        null,
        null,
        true,
        comentario,
        nota);
  }

  public void atualizar(String comentario, BigDecimal nota) {
    this.comentario = comentario;
    this.nota = nota;
    this.alteracao = Instant.now();
  }

  public void excluir() {
    this.exclusao = Instant.now();
    this.ativo = false;
  }
}
