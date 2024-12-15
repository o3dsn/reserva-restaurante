package br.com.fiap.reservarestaurante.application.usecases.reserva.delete;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultReservaDeleteUseCase extends ReservaDeleteUseCase {
  private final ReservaRepository reservaRepository;

  @Override
  public void execute(String id) {
    final var reservaId = new ReservaId(id);
    final var reserva =
        reservaRepository
            .buscarPorId(reservaId)
            .orElseThrow(
                () ->
                    new ReservaException(
                        "Reserva com ID %s não encontrado.".formatted(reservaId),
                        HttpStatus.NOT_FOUND));

    if (reserva.getStatus() == ReservaDTO.StatusEnum.CANCELADA) {
      throw new AvaliacaoException(
          "Reserva com ID %s já cancelada.".formatted(reservaId), HttpStatus.CONFLICT);
    }

    reserva.excluir();
    reservaRepository.atualizar(reserva);
  }
}
