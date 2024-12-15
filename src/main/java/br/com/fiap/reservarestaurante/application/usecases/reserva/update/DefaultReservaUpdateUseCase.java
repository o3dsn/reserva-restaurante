package br.com.fiap.reservarestaurante.application.usecases.reserva.update;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultReservaUpdateUseCase extends ReservaUpdateUseCase {
  private final ReservaRepository reservaRepository;

  @Override
  public ReservaUpdateUseCaseOutput execute(ReservaUpdateUseCaseInput input) {
    final var reservaId = new ReservaId(input.id());
    final var reserva =
        reservaRepository
            .buscarPorId(reservaId)
            .orElseThrow(
                () ->
                    new ReservaException(
                        "Reserva com ID %s não encontrado.".formatted(reservaId),
                        HttpStatus.NOT_FOUND));

    if (reserva.getStatus() == ReservaDTO.StatusEnum.CANCELADA) {
      throw new ReservaException(
          "Reserva com ID %s já cancelada.".formatted(reservaId), HttpStatus.CONFLICT);
    }

    reserva.atualizar(input.status());

    return ReservaUpdateUseCaseOutput.from(reservaRepository.atualizar(reserva));
  }
}
