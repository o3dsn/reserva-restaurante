package br.com.fiap.reservarestaurante.infrastructure.persistence.entities;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.infrastructure.mappers.RestauranteMapper;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurantes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RestauranteJPAEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, length = 36)
  private String id;

  private String nome;

  private String descricao;

  private String endereco;

  private String cidade;

  private String estado;

  private String bairro;

  @Column(name = "tipo_cozinha_id", length = 36)
  private String tipoCozinhaId;

  private String faixaPreco;

  private String telefone;

  private String email;

  private Double avaliacaoMedia;

  private Long avaliacaoTotal;

  private String horarioAbertura;

  private String horarioFechamento;

  public static RestauranteJPAEntity of(final Restaurante restaurante) {
    return new RestauranteJPAEntity(
        restaurante.getId().value(),
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

  public Restaurante toRestaurante() {
    return RestauranteMapper.INSTANCE.toObject(this);
  }
}
