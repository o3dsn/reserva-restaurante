package br.com.fiap.reservarestaurante.application.usecases.avaliacao.delete;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultAvaliacaoDeleteUseCase extends AvaliacaoDeleteUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    @Override
    public void execute(String id) {
        final var avaliacaoId = new AvaliacaoId(id);
        final var avaliacao = avaliacaoRepository.buscarPorId(avaliacaoId)
                .orElseThrow(()->new AvaliacaoException("Avaliação com ID %s não encontrado.".formatted(avaliacaoId), HttpStatus.NOT_FOUND));

        if (!avaliacao.isAtivo()) {
            throw new AvaliacaoException("Avaliação com ID %s já deletada.".formatted(avaliacaoId), HttpStatus.CONFLICT);
        }

        avaliacao.excluir();
        avaliacaoRepository.atualizar(avaliacao);
    }

}
