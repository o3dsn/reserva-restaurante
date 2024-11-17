package br.com.fiap.reservarestaurante.application.domain.avaliacao;

import br.com.fiap.reservarestaurante.application.utils.NumberUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Avaliacao {

    private AvaliacaoId id;
    private String reservaId;
    private String usuarioId;
    private Instant criacao;
    private Instant alteracao;
    private Instant exclusao;
    private boolean ativo;
    private String comentario;
    private double nota;

    public static Avaliacao nova(String reservaId, String usuarioId, String comentario, double nota) {
        return new Avaliacao(new AvaliacaoId(null), reservaId, usuarioId, Instant.now(), null, null, true, comentario, NumberUtils.roundToOneDecimalPlace(nota));
    }

    public void atualizar(String comentario, double nota) {
        this.comentario = comentario;
        this.nota = NumberUtils.roundToOneDecimalPlace(nota);
        this.alteracao = Instant.now();
    }

    public void excluir() {
        this.exclusao = Instant.now();
        this.ativo = false;
    }
}
