package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario criar(Usuario restaurante);

    Usuario atualizar(Usuario restaurante);

    Pagination<Usuario> buscarTudo(Page page);

    Optional<Usuario> buscarPorId(UsuarioId id);

    void deletarRestaurante(Usuario usuario);

}
