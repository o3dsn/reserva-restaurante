package br.com.fiap.reservarestaurante.utils;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;

public final class UsuarioHelper {

    public static final String ID = "2cc9c534-9900-44b7-a0d2-551d38d82953";
    public static final String NOME = "Restaurante Exemplo";
    public static final String EMAIL = "contato@restauranteexemplo.com";
    public static final String SENHA = "123456";

    public static UsuarioJPAEntity gerarUsuarioJPAEntityNovo() {
        var usuario = gerarUsuarioNovo();
        return UsuarioJPAEntity.of(usuario);
    }

    public static Usuario gerarUsuarioNovo() {
        return Usuario.create(NOME, EMAIL, SENHA);
    }

}
