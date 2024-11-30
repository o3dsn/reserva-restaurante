package br.com.fiap.reservarestaurante.application.usecases.reserva.update;

import br.com.fiap.reserva.model.ReservaDTO;

public record ReservaUpdateUseCaseInput (String id, ReservaDTO.StatusEnum status) {
}
