package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import java.util.Optional;

public interface UsuarioRepository {

  Usuario criar(Usuario usuario);

  Usuario atualizar(Usuario usuario);

  //    Pagination<Usuario> buscarTudo(Page page);
  //
  Optional<Usuario> buscarPorId(UsuarioId id);
  //
  //    void deletarRestaurante(Usuario usuario);

}
