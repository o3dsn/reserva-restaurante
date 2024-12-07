package br.com.fiap.reservarestaurante.infrastructure.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    @Override
    public Usuario criar(Usuario usuario) {
        return null;
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        return null;
    }

    @Override
    public Pagination<Usuario> buscarTudo(Page page) {
        return null;
    }

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return Optional.empty();
    }

    @Override
    public void deletarRestaurante(Usuario usuario) {

    }
}
