package br.com.fiap.reservarestaurante.utils;

import br.com.fiap.reserva.model.AtualizaReservaDTO;
import br.com.fiap.reserva.model.CriaReservaDTO;
import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.get.ReservaGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.retrive.list.ReservaListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.update.ReservaUpdateUseCaseInput;

import java.time.Instant;
import java.time.OffsetDateTime;

public final class ReservaHelper {
    public static final String RESTAURANTE_ID = "2cc9c534-9900-44b7-a0d2-551d38d82953";
    public static final String USUARIO_ID = "dd9e1b59-3a07-4dec-becc-a87888c26bd2";
    public static final String COMENTARIO =
            "Um comentario com mais de 50 caracteres sobre meu restaurante favorito";


    private ReservaHelper() {
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    public static ReservaCreateUseCaseInput gerarReservaCreateUseCaseInput() {
        return new ReservaCreateUseCaseInput(
                RESTAURANTE_ID,
                USUARIO_ID,
                ReservaDTO.StatusEnum.PENDENTE,
                COMENTARIO,
                Instant.parse("2024-12-15T19:00:00Z")
        );
    }

    public static ReservaUpdateUseCaseInput gerarReservaUpdateUseCaseInput(String id) {
        return new ReservaUpdateUseCaseInput(id, ReservaDTO.StatusEnum.CONFIRMADA);
    }

    public static ReservaListUseCaseInput gerarReservaListUseCaseInput() {
        return new ReservaListUseCaseInput(new Page(0, 10));
    }

    public static Reserva gerarReserva(String id) {
        return new Reserva(
                ReservaId.from(id),
                RESTAURANTE_ID,
                USUARIO_ID,
                ReservaDTO.StatusEnum.PENDENTE,
                COMENTARIO,
                Instant.parse("2024-12-15T19:00:00Z"),
                Instant.now(),
                null
        );
    }

    public static Reserva gerarReserva(String id, ReservaDTO.StatusEnum status) {
        return new Reserva(
                ReservaId.from(id),
                RESTAURANTE_ID,
                USUARIO_ID,
                status,
                COMENTARIO,
                Instant.parse("2024-12-15T19:00:00Z"),
                Instant.now(),
                null
        );
    }

    private static ReservaGetByIdUseCaseOutput gerarReservaComStatus(ReservaDTO.StatusEnum status, String dataAlteracao) {
        return new ReservaGetByIdUseCaseOutput(
                ReservaId.from("1ec7e452-1876-451c-b089-726f2f9ef1ba"),
                AvaliacaoHelper.RESTAURANTE_ID,
                AvaliacaoHelper.USUARIO_ID,
                status,
                null,
                Instant.parse("2024-12-02T10:15:30.00Z"),
                Instant.parse("2024-12-01T06:10:30.100Z"),
                Instant.parse(dataAlteracao));
    }

    public static Reserva gerarReservaExcluida(String id) {
        return gerarReserva(id, ReservaDTO.StatusEnum.CANCELADA);
    }

    public static ReservaGetByIdUseCaseOutput gerarReservaFinalizada() {
        return gerarReservaComStatus(ReservaDTO.StatusEnum.FINALIZADA, Instant.now().toString());
    }

    public static ReservaGetByIdUseCaseOutput gerarReservaFinalizadaInvalida() {
        return gerarReservaComStatus(ReservaDTO.StatusEnum.FINALIZADA, "2021-12-01T00:00:00.100Z");
    }

    public static ReservaGetByIdUseCaseOutput gerarReservaConfirmada() {
        return gerarReservaComStatus(ReservaDTO.StatusEnum.CONFIRMADA, Instant.now().toString());
    }

    public static CriaReservaDTO gerarCriaReservaDTO() {
        return new CriaReservaDTO().comentario(COMENTARIO).dataHorarioReserva(OffsetDateTime.parse("2025-12-01T00:00:00.100Z"));
    }

    public static AtualizaReservaDTO gerarAtualizaReservaDTO() {
        return new AtualizaReservaDTO().status(AtualizaReservaDTO.StatusEnum.CONFIRMADA);
    }
}
