package br.com.fiap.reservarestaurante.application.domain.usuario;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
@Builder
public class Usuario {

    private UsuarioId id;
    private String nome;
    private String email;
    private String senha;

    public static Usuario create(String id, String nome, String email, String senha) {

//       final var usuarioid = id == null ? null : new UsuarioId(id);

        final var usuarioId = (id == null || id.isBlank())
                ? new UsuarioId(UUID.randomUUID().toString())
                : new UsuarioId(id);


        return new Usuario(
                usuarioId,
                nome,
                email,
                senha);

    }

//    public static Usuario create(String id, String nome, String email, String senha) {
//        final var usuarioId = (id == null || id.isBlank())
//                ? new UsuarioId(UUID.randomUUID().toString())
//                : new UsuarioId(id);
//
//        return new Usuario(
//                usuarioId,
//                nome,
//                email,
//                senha
//        );
//    }


    public static Usuario novo(Usuario usuario) {
        return new Usuario(
                new UsuarioId(null),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha());
    }

}
