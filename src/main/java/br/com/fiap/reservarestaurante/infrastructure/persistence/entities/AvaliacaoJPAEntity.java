package br.com.fiap.reservarestaurante.infrastructure.persistence.entities;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.infrastructure.mappers.AvaliacaoMapper;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "avaliacoes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class AvaliacaoJPAEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, length = 36)
  private String id;

  @Column(name = "reserva_id", nullable = false, length = 36)
  private String reservaId;

  @Column(name = "usuario_id", nullable = false, length = 36)
  private String usuarioId;

  @Column(name = "criacao", nullable = false)
  @CreatedDate
  private Instant criacao;

  @Column(name = "alteracao")
  @LastModifiedDate
  private Instant alteracao;

  @Column(name = "exclusao")
  private Instant exclusao;

  @Column(name = "ativo", nullable = false)
  private boolean ativo;

  @Column(name = "comentario", nullable = false, length = 500)
  private String comentario;

  @Column(name = "nota", nullable = false, precision = 1)
  private double nota;

  public static AvaliacaoJPAEntity of(final Avaliacao avaliacao) {
    return new AvaliacaoJPAEntity(
        avaliacao.getId().value(),
        avaliacao.getReservaId(),
        avaliacao.getUsuarioId(),
        avaliacao.getCriacao(),
        avaliacao.getAlteracao(),
        avaliacao.getExclusao(),
        avaliacao.isAtivo(),
        avaliacao.getComentario(),
        avaliacao.getNota());
  }

  public Avaliacao toAvaliacao() {
    return AvaliacaoMapper.INSTANCE.toObject(this);
  }
}
