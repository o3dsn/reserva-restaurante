package br.com.fiap.reservarestaurante.utils;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCaseInput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.usuario.model.CadastrarUsuarioDTO;

import static org.assertj.core.api.Assertions.assertThat;

public final class UsuarioHelper {

    public static final String ID = "2cc9c534-9900-44b7-a0d2-551d38d82953";
    public static final String NOME = "Joao do Bairro";
    public static final String EMAIL = "joao@exemplo.com";
    public static final String SENHA = "123456";

    public static UsuarioJPAEntity gerarUsuarioJPAEntityNovo() {
        var usuario = gerarUsuarioNovo();
        return UsuarioJPAEntity.of(usuario);
    }

    public static Usuario gerarUsuarioNovo() {
        return  Usuario.novo(gerar("2cd9c534-9900-44b7-a0d2-551d38d82953"));
    }


    public static void validar(Usuario usuarioArmazenado, Usuario usuario) {
        validar(UsuarioJPAEntity.of(usuarioArmazenado), UsuarioJPAEntity.of(usuario));
    }

    public static void validar(
            UsuarioJPAEntity usuarioArmazenado, UsuarioJPAEntity usuario) {
        assertThat(usuarioArmazenado).isNotNull();
        assertThat(usuario).isNotNull();

        assertThat(usuarioArmazenado)
                .extracting(UsuarioJPAEntity::getId)
                .isEqualTo(usuario.getId());
        assertThat(usuarioArmazenado)
                .extracting(UsuarioJPAEntity::getNome)
                .isEqualTo(usuario.getNome());
    }

    public static Usuario gerar(String id) {
        return new Usuario(
                UsuarioId.from(id),
                NOME,
                EMAIL,
                SENHA);
    }


    public static CadastrarUsuarioDTO gerarUsuarioDTO() {

        return new CadastrarUsuarioDTO()
                .nome(NOME)
                .email(EMAIL)
                .senha(SENHA);
    }

    public static UsuarioCreateUseCaseInput gerarRestauranteCreateUseCaseInput() {
        return new UsuarioCreateUseCaseInput(
                ID,
                NOME,
                EMAIL,
                SENHA);
    }

}
