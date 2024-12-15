package br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;

public record RestauranteListUseCaseOutput(
    RestauranteId id,
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
    String horarioFechamento) {

  public static RestauranteListUseCaseOutput from(final Restaurante restaurante) {
    return new RestauranteListUseCaseOutput(
        restaurante.getId(),
        restaurante.getNome(),
        restaurante.getDescricao(),
        restaurante.getEndereco(),
        restaurante.getCidade(),
        restaurante.getEstado(),
        restaurante.getBairro(),
        restaurante.getTipoCozinhaId(),
        restaurante.getFaixaPreco(),
        restaurante.getTelefone(),
        restaurante.getEmail(),
        restaurante.getAvaliacaoMedia(),
        restaurante.getAvaliacaoTotal(),
        restaurante.getHorarioAbertura(),
        restaurante.getHorarioFechamento());
  }
}
