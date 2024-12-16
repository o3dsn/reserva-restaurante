package br.com.fiap.reservarestaurante.application.usecases.reserva.create;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultReservaCreateUseCase extends ReservaCreateUseCase {
  private final ReservaRepository reservaRepository;

  @Override
  public ReservaCreateUseCaseOutput execute(ReservaCreateUseCaseInput input) {
    final var novaReserva =
        Reserva.nova(
            input.restauranteId(),
            input.usuarioId(),
            ReservaDTO.StatusEnum.PENDENTE,
            input.comentario(),
            input.dataHorarioReserva());
    return ReservaCreateUseCaseOutput.from(reservaRepository.criar(novaReserva));
  }
}
