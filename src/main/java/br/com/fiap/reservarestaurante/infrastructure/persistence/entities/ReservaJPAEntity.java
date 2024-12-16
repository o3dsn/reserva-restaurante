package br.com.fiap.reservarestaurante.infrastructure.persistence.entities;

import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.infrastructure.mappers.ReservaMapper;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "reservas")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class ReservaJPAEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, length = 36)
  private String id;

  @Column(name = "restaurante_id", nullable = false, length = 36)
  private String restauranteId;

  @Column(name = "usuario_id", nullable = false, length = 36)
  private String usuarioId;

  @Column(name = "status", nullable = false, length = 36)
  private String status;

  @Column(name = "comentario", nullable = false, length = 500)
  private String comentario;

  @Column(name = "data_horario_reserva", nullable = false)
  @CreatedDate
  private Instant dataHorarioReserva;

  @Column(name = "criacao", nullable = false)
  @CreatedDate
  private Instant criacao;

  @Column(name = "alteracao")
  @LastModifiedDate
  private Instant alteracao;

  public static ReservaJPAEntity of(final Reserva reserva) {
    return new ReservaJPAEntity(
        reserva.getId().value(),
        reserva.getRestauranteId(),
        reserva.getUsuarioId(),
        reserva.getStatus().toString(),
        reserva.getComentario(),
        reserva.getDataHorarioReserva(),
        reserva.getCriacao(),
        reserva.getAlteracao());
  }

  public Reserva toReserva() {
    return ReservaMapper.INSTANCE.toObject(this);
  }
}
