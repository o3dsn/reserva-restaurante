package br.com.fiap.reservarestaurante.utils;

import static org.assertj.core.api.Assertions.assertThat;

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
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
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
        Instant.parse("2024-12-15T19:00:00Z"));
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
        null);
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
        null);
  }

  private static ReservaGetByIdUseCaseOutput gerarReservaComStatus(
      ReservaDTO.StatusEnum status, String dataAlteracao) {
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
    return new CriaReservaDTO()
        .comentario(COMENTARIO)
        .dataHorarioReserva(OffsetDateTime.parse("2025-12-01T00:00:00.100Z"));
  }

  public static AtualizaReservaDTO gerarAtualizaReservaDTO() {
    return new AtualizaReservaDTO().status(AtualizaReservaDTO.StatusEnum.CONFIRMADA);
  }

  public static ReservaJPAEntity gerarReservaJPAEntity() {
    var reserva = gerarReserva("2cc9c534-9900-44b7-a0d2-551d38d82953");
    return ReservaJPAEntity.of(reserva);
  }

  public static ReservaJPAEntity gerarReservaJPAEntityNova() {
    var reserva = gerarReservaNova();
    return ReservaJPAEntity.of(reserva);
  }

  public static Reserva gerarReservaNova() {
    return Reserva.nova(
        RESTAURANTE_ID,
        USUARIO_ID,
        ReservaDTO.StatusEnum.PENDENTE,
        "comentario",
        Instant.parse("2024-12-02T10:15:30.00Z"));
  }

  public static ReservaJPAEntity gerarReservaJPAEntity(String id) {
    var reserva = gerarReserva(id);
    return ReservaJPAEntity.of(reserva);
  }

  public static void validar(Reserva reservaArmazenado, Reserva reserva) {
    validar(ReservaJPAEntity.of(reservaArmazenado), ReservaJPAEntity.of(reserva));
  }

  public static void validar(ReservaJPAEntity reservaArmazenado, ReservaJPAEntity reserva) {
    assertThat(reservaArmazenado).isNotNull();
    assertThat(reserva).isNotNull();

    assertThat(reservaArmazenado).extracting(ReservaJPAEntity::getId).isEqualTo(reserva.getId());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getRestauranteId)
        .isEqualTo(reserva.getRestauranteId());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getUsuarioId)
        .isEqualTo(reserva.getUsuarioId());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getComentario)
        .isEqualTo(reserva.getComentario());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getStatus)
        .isEqualTo(reserva.getStatus());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getDataHorarioReserva)
        .isEqualTo(reserva.getDataHorarioReserva());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getCriacao)
        .isEqualTo(reserva.getCriacao());
    assertThat(reservaArmazenado)
        .extracting(ReservaJPAEntity::getAlteracao)
        .isEqualTo(reserva.getAlteracao());
  }
}
