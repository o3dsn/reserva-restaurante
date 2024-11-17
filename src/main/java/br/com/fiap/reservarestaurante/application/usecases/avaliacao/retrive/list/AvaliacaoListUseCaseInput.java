package br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;

public record AvaliacaoListUseCaseInput(Page page, boolean ativo) {}
