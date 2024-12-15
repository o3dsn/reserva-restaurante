package br.com.fiap.reservarestaurante.application.domain.reserva;

import br.com.fiap.reserva.model.ReservaDTO;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reserva {
  private ReservaId id;
  private String restauranteId;
  private String usuarioId;
  private ReservaDTO.StatusEnum status;
  private String comentario;
  private Instant dataHorarioReserva;
  private Instant criacao;
  private Instant alteracao;

  public static Reserva nova(
      String restauranteId,
      String usuarioId,
      ReservaDTO.StatusEnum status,
      String comentario,
      Instant dataHorarioReserva) {
    return new Reserva(
        new ReservaId(null),
        restauranteId,
        usuarioId,
        status,
        comentario,
        dataHorarioReserva,
        Instant.now(),
        null);
  }

  public void atualizar(ReservaDTO.StatusEnum status) {
    this.status = status;
    this.alteracao = Instant.now();
  }

  public void excluir() {
    this.status = ReservaDTO.StatusEnum.CANCELADA;
    this.alteracao = Instant.now();
  }
}
