package br.com.fiap.reservarestaurante.application.domain.usuario;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class Usuario {

    private UsuarioId id;
    private String nome;
    private String email;
    private String senha;

    public static Usuario novo(String nome, String email, String senha) {
      return new Usuario(
        null,
        nome,
        email,
        senha);

    }

    public void atualizar(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public void excluir() {
        this.id = null;
    }
}
