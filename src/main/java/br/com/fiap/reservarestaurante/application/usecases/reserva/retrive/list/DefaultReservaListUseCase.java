package br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultReservaListUseCase extends ReservaListUseCase {
    private final ReservaRepository reservaRepository;

    @Override
    public Pagination<ReservaListUseCaseOutput> execute(final ReservaListUseCaseInput input) {
        return reservaRepository.buscarTudo(input.page()).mapItems(ReservaListUseCaseOutput::from);
    }
}
