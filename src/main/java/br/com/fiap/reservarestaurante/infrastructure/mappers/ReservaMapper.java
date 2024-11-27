package br.com.fiap.reservarestaurante.infrastructure.mappers;

import br.com.fiap.reserva.model.CriaReservaDTO;
import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.reserva.create.ReservaCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Mapper
public interface ReservaMapper {
    ReservaMapper INSTANCE = Mappers.getMapper(ReservaMapper.class);

    @Mapping(target = "dataHorarioReserva", expression = "java(dto.getDataHorarioReserva().toInstant())")
    ReservaCreateUseCaseInput fromDTO(UUID restauranteId, UUID usuarioId, CriaReservaDTO dto);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    @Mapping(target = "dataHorarioReserva", expression = "java(mapOffsetDateTime(output.dataHorarioReserva()))")
    @Mapping(target = "criacao", expression = "java(mapOffsetDateTime(output.criacao()))")
    @Mapping(target = "alteracao", expression = "java(mapOffsetDateTime(null))")
    ReservaDTO toDTO(ReservaCreateUseCaseOutput output);

    @Mapping(target = "id", expression = "java(new ReservaId(entity.getId()))")
    Reserva toObject(ReservaJPAEntity entity);

    default OffsetDateTime mapOffsetDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
