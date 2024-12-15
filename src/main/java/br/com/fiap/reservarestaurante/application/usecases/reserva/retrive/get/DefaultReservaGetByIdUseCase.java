package br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get;

import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultReservaGetByIdUseCase extends ReservaGetByIdUseCase {
  private final ReservaRepository reservaRepository;

  @Override
  public ReservaGetByIdUseCaseOutput execute(final String id) {
    final var reservarId = new ReservaId(id);
    return reservaRepository
        .buscarPorId(reservarId)
        .map(ReservaGetByIdUseCaseOutput::from)
        .orElseThrow(
            () ->
                new ReservaException(
                    "Reserva com ID %s não encontrado.".formatted(reservarId),
                    HttpStatus.NOT_FOUND));
  }
}
