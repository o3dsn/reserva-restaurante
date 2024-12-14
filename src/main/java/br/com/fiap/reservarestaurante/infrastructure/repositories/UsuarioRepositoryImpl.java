package br.com.fiap.reservarestaurante.infrastructure.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.UsuarioJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJPARepository usuarioJPARepository;

    @Override
    public Usuario criar(Usuario usuario) {
        return save(usuario);
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        return null;
    }

    @Override
    public Optional<Usuario> buscarPorId(UsuarioId id) {
        return usuarioJPARepository.findById(id.value()).map(UsuarioJPAEntity::toUsuario);
    }

    private Usuario save(final Usuario usuario) {
        return usuarioJPARepository.save(UsuarioJPAEntity.of(usuario)).toUsuario();
    }

}
