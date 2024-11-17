package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;

import java.util.Optional;

public interface AvaliacaoRepository {

    Avaliacao criar(Avaliacao avaliacao);

    Pagination<Avaliacao> buscarTudo(Page page, boolean ativo);

    Optional<Avaliacao> buscarPorId(AvaliacaoId id);

    Pagination<Avaliacao> buscarPorIdRestaurante(Page page, String id);

    Avaliacao atualizar(Avaliacao avaliacao);

    Optional<NotaRestaurante> buscarNotaRestaurante(String id);

    Optional<Avaliacao> buscarPorIdReserva(String reservaId);

}
