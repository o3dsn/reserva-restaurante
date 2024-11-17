package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultAvaliacaoGetByIdUseCase extends AvaliacaoGetByIdUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    @Override
    public AvaliacaoGetByIdUseCaseOutput execute(final String id) {
        final var avaliacaoId = new AvaliacaoId(id);
        return avaliacaoRepository.buscarPorId(avaliacaoId)
                .map(AvaliacaoGetByIdUseCaseOutput::from)
                .orElseThrow(()->new AvaliacaoException("Avaliação com ID %s não encontrado.".formatted(avaliacaoId), HttpStatus.NOT_FOUND));
    }
}
