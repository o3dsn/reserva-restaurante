package br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import java.time.Instant;

public record ReservaGetByIdUseCaseOutput(
    ReservaId id,
    String restauranteId,
    String usuarioId,
    ReservaDTO.StatusEnum status,
    String comentario,
    Instant dataHorarioReserva,
    Instant criacao,
    Instant alteracao) {

  public static ReservaGetByIdUseCaseOutput from(final Reserva reserva) {
    return new ReservaGetByIdUseCaseOutput(
        reserva.getId(),
        reserva.getRestauranteId(),
        reserva.getUsuarioId(),
        reserva.getStatus(),
        reserva.getComentario(),
        reserva.getDataHorarioReserva(),
        reserva.getCriacao(),
        reserva.getAlteracao());
  }
}
