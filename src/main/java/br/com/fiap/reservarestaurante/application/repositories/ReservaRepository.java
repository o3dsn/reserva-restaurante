package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;

import java.util.Optional;

public interface ReservaRepository {
    Reserva criar(Reserva reserva);
    Optional<Reserva> buscarPorId(ReservaId id);
    Reserva atualizar(Reserva reserva);
    Pagination<Reserva> buscarTudo(Page page);
}
