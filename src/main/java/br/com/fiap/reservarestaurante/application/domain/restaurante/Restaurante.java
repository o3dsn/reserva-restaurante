package br.com.fiap.reservarestaurante.application.domain.restaurante;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Restaurante {

  private RestauranteId id;
  private String nome;
  private String descricao;
  private String endereco;
  private String cidade;
  private String estado;
  private String bairro;
  private String tipoCozinhaId;
  private String faixaPreco;
  private String telefone;
  private String email;
  private Double avaliacaoMedia;
  private Long avaliacaoTotal;
  private String horarioAbertura;
  private String horarioFechamento;

  public static Restaurante nova(Restaurante restaurante) {
    return new Restaurante(
        new RestauranteId(null),
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
