package br.com.fiap.reservarestaurante.application.usecases.reserva.create;

import br.com.fiap.reserva.model.ReservaDTO;

import java.time.Instant;

public record ReservaCreateUseCaseInput(String restauranteId, String usuarioId, ReservaDTO.StatusEnum status, String comentario, Instant dataHorarioReserva) {
}
