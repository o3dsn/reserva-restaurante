package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.reservarestaurante.utils.UsuarioHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class UsuarioJPARepositoryIT {

    private final UsuarioJPARepository usuarioJPARepository;

    @Autowired
    public UsuarioJPARepositoryIT(UsuarioJPARepository repository) {
        this.usuarioJPARepository = repository;
    }

    @Nested
    class Criar {

        @Test
        void devePermitirCriarUsuario() {
            var usuario = UsuarioHelper.gerarUsuarioJPAEntityNovo();
            var usuarioJPA = usuarioJPARepository.save(usuario);

            assertThat(usuarioJPA)
                    .isInstanceOf(UsuarioJPAEntity.class)
                    .isNotNull()
                    .isEqualTo(usuario);

            UsuarioHelper.validar(usuario, usuarioJPA);
        }
    }

    @Nested
    class Buscar {

        @Test
        void devePermitirBuscarTodosUsuarios() {
            var resultadoPaginado = usuarioJPARepository.findAll(Pageable.unpaged());

            assertThat(resultadoPaginado).hasSizeGreaterThanOrEqualTo(1);

            assertThat(resultadoPaginado.getContent().get(0)).isInstanceOf(UsuarioJPAEntity.class);
        }
    }
}
