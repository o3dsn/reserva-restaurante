package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultAvaliacaoListUseCase extends AvaliacaoListUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    @Override
    public Pagination<AvaliacaoListUseCaseOutput> execute(final AvaliacaoListUseCaseInput input) {
        return avaliacaoRepository.buscarTudo(input.page(), input.ativo()).mapItems(AvaliacaoListUseCaseOutput::from);
    }

}
