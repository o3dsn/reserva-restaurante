package br.com.fiap.reservarestaurante.application.usecases.restaurente.create;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRestauranteCreateUseCase extends RestauranteCreateUseCase {

  private final RestauranteRepository repository;

  @Override
  public RestauranteCreateUseCaseOutput execute(RestauranteCreateUseCaseInput input) {
    Restaurante novoRestaurante =
        Restaurante.nova(
            Restaurante.builder()
                .nome(input.nome())
                .descricao(input.descricao())
                .endereco(input.endereco())
                .cidade(input.cidade())
                .estado(input.estado())
                .bairro(input.bairro())
                .tipoCozinhaId(input.tipoCozinhaId())
                .faixaPreco(input.faixaPreco())
                .telefone(input.telefone())
                .email(input.email())
                .avaliacaoMedia(input.avaliacaoMedia())
                .avaliacaoTotal(input.avaliacaoTotal())
                .horarioAbertura(input.horarioAbertura())
                .horarioFechamento(input.horarioFechamento())
                .build());

    return RestauranteCreateUseCaseOutput.from(repository.criar(novoRestaurante));
  }
}
