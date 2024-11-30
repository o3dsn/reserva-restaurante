package br.com.fiap.reservarestaurante.application.usecases.avaliacao.update;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultAvaliacaoUpdateUseCase extends AvaliacaoUpdateUseCase {

  private final AvaliacaoRepository avaliacaoRepository;

  @Override
  public AvaliacaoUpdateUseCaseOutput execute(AvaliacaoUpdateUseCaseInput input) {
    final var avaliacaoId = new AvaliacaoId(input.id());
    final var avaliacao =
        avaliacaoRepository
            .buscarPorId(avaliacaoId)
            .orElseThrow(
                () ->
                    new AvaliacaoException(
                        "Avaliação com ID %s não encontrado.".formatted(avaliacaoId),
                        HttpStatus.NOT_FOUND));

    if (!avaliacao.isAtivo()) {
      throw new AvaliacaoException(
          "Avaliação com ID %s não disponível para atualização.".formatted(avaliacaoId),
          HttpStatus.CONFLICT);
    }
    avaliacao.atualizar(input.comentario(), input.nota());
    return AvaliacaoUpdateUseCaseOutput.from(avaliacaoRepository.atualizar(avaliacao));
  }
}
