package br.com.fiap.reservarestaurante.application.usecases.avaliacao.create;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCase;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultAvaliacaoCreateUseCase extends AvaliacaoCreateUseCase {

  private final AvaliacaoRepository avaliacaoRepository;
  private final ReservaGetByIdUseCase reservaGetByIdUseCase;

  @Override
  public AvaliacaoCreateUseCaseOutput execute(AvaliacaoCreateUseCaseInput input) {
    var reservaOutput = reservaGetByIdUseCase.execute(input.reservaId());
    if (!ReservaDTO.StatusEnum.FINALIZADA.equals(reservaOutput.status())) {
      throw new AvaliacaoException("Reserva ainda não finalizada", HttpStatus.CONFLICT);
    }

    if (!reservaOutput.usuarioId().equals(input.usuarioId())) {
      throw new AvaliacaoException(
          "Usuario não pode fazer avaliação para essa reserva", HttpStatus.CONFLICT);
    }

    if (avaliacaoRepository.buscarPorIdReserva(input.reservaId()).isPresent()) {
      throw new AvaliacaoException(
          "Avaliação já registrada para essa reserva", HttpStatus.CONFLICT);
    }

    if (Duration.between(reservaOutput.alteracao(), Instant.now()).toHours() > 24) {
      throw new AvaliacaoException(
          "Tempo limite para realizar avaliação alcançado", HttpStatus.CONFLICT);
    }

    final var novaAvaliacao =
        Avaliacao.nova(input.reservaId(), input.usuarioId(), input.comentario(), input.nota());
    return AvaliacaoCreateUseCaseOutput.from(avaliacaoRepository.criar(novaAvaliacao));
  }
}
