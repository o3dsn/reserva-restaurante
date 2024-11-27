package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reserva.api.ReservasApi;
import br.com.fiap.reserva.model.CriaReservaDTO;
import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCase;
import br.com.fiap.reservarestaurante.infrastructure.mappers.ReservaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReservaController implements ReservasApi {
    private static final ReservaMapper reservaMapper = ReservaMapper.INSTANCE;
    private final ReservaCreateUseCase reservaCreateUseCase;

    @Override
    public ResponseEntity<ReservaDTO> cadastrarReserva(
            UUID restauranteId,
            UUID usuarioId,
            CriaReservaDTO body) {
        final var useCaseInput = reservaMapper.fromDTO(restauranteId, usuarioId, body);
        final var useCaseOutput = reservaCreateUseCase.execute(useCaseInput);
        var uri = URI.create("/reservas/" + useCaseOutput.id());
        return ResponseEntity.created(uri).body(reservaMapper.toDTO(useCaseOutput));
    }
}
