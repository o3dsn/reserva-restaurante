package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reserva.api.ReservasApi;
import br.com.fiap.reserva.model.AtualizaReservaDTO;
import br.com.fiap.reserva.model.CriaReservaDTO;
import br.com.fiap.reserva.model.PaginadaReservaDTO;
import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.delete.ReservaDeleteUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list.ReservaListUseCase;
import br.com.fiap.reservarestaurante.application.usecases.reserva.update.ReservaUpdateUseCase;
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
    private final ReservaDeleteUseCase reservaDeleteUseCase;
    private final ReservaListUseCase reservaListUseCase;
    private final ReservaGetByIdUseCase reservaGetByIdUseCase;
    private final ReservaUpdateUseCase reservaUpdateUseCase;

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

    @Override
    public ResponseEntity<PaginadaReservaDTO> buscarReservas(Integer pagina, Integer porPagina) {
        final var useCaseInput = reservaMapper.fromDTO(new Page(pagina, porPagina));
        final var reservas = reservaListUseCase.execute(useCaseInput).mapItems(reservaMapper::toDTO);
        final var paginatedReservas = new PaginadaReservaDTO()
                .items(reservas.items())
                .pagina(reservas.currentPage())
                .porPagina(reservas.perPage())
                .total(reservas.total());
        return ResponseEntity.ok(paginatedReservas);
    }

    @Override
    public ResponseEntity<ReservaDTO> buscarReservaPorId(String id) {
        final var useCaseOutput = reservaMapper.toDTO(reservaGetByIdUseCase.execute(id));
        return ResponseEntity.ok(useCaseOutput);
    }

    @Override
    public ResponseEntity<ReservaDTO> atualizarReserva(final String id, final AtualizaReservaDTO body) {
        final var useCaseInput = reservaMapper.fromDTO(id, body);
        final var useCaseOutput = reservaMapper.toDTO(reservaUpdateUseCase.execute(useCaseInput));
        return ResponseEntity.ok(useCaseOutput);
    }

    @Override
    public ResponseEntity<Void> deletarReserva(String id) {
        reservaDeleteUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
