package br.com.fiap.reservarestaurante.application.usecases.reserva.update;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import java.time.Instant;

public record ReservaUpdateUseCaseOutput (
        ReservaId id,
        String restauranteId,
        String usuarioId,
        ReservaDTO.StatusEnum status,
        String comentario,
        Instant dataHorarioReserva,
        Instant criacao,
        Instant alteracao
) {
    public static ReservaUpdateUseCaseOutput from(final Reserva reserva) {
        return new ReservaUpdateUseCaseOutput(
                reserva.getId(),
                reserva.getRestauranteId(),
                reserva.getUsuarioId(),
                reserva.getStatus(),
                reserva.getComentario(),
                reserva.getDataHorarioReserva(),
                reserva.getCriacao(),
                reserva.getAlteracao()
        );
    }
}
