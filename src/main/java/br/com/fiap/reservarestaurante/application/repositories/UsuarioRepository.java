package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    Usuario criar(Usuario usuario);

    Usuario atualizar(Usuario usuario);

//    Pagination<Usuario> buscarTudo(Page page);
//
//    Optional<Usuario> buscarPorId(String id);
//
//    void deletarRestaurante(Usuario usuario);

}
