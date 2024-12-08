package br.com.fiap.reservarestaurante.infrastructure.persistence.entities;

import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.infrastructure.mappers.RestauranteMapper;
import br.com.fiap.reservarestaurante.infrastructure.mappers.UsuarioMapper;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioJPAEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", nullable = false, unique = true, length = 36)
  private String id;
  private String nome;
  private String email;
  private String senha;


  public static UsuarioJPAEntity of(final Usuario usuario) {
    return new UsuarioJPAEntity(
        usuario.getId().value(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getSenha()
    );
  }

  public Usuario toUsuario() {
    return UsuarioMapper.INSTANCE.toObject(this);
  }


}
