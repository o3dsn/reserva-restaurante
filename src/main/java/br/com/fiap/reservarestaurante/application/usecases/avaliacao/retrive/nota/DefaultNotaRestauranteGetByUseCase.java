package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota;

import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultNotaRestauranteGetByUseCase extends NotaRestauranteGetByIdUseCase {

  private final AvaliacaoRepository avaliacaoRepository;

  @Override
  public NotaRestauranteGetByIdUseCaseOutput execute(final String id) {
    return avaliacaoRepository
        .buscarNotaRestaurante(id)
        .map(NotaRestauranteGetByIdUseCaseOutput::from)
        .orElseThrow(
            () ->
                new AvaliacaoException(
                    "Nota para o restaurante com ID %s não encontrado.".formatted(id),
                    HttpStatus.NOT_FOUND));
  }
}
