package br.com.fiap.reservarestaurante.application.usecases.restaurente.update;

import java.time.Instant;

public record RestauranteUpdateUseCaseInput(
    String id,
    String nome,
    String descricao,
    String endereco,
    String cidade,
    String estado,
    String bairro,
    String tipoCozinhaId,
    String faixaPreco,
    String telefone,
    String email,
    Double avaliacaoMedia,
    Long avaliacaoTotal,
    String horarioAbertura,
    String horarioFechamento) {}
