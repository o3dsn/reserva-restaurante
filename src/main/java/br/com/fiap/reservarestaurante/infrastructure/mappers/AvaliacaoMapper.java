package br.com.fiap.reservarestaurante.infrastructure.mappers;

import br.com.fiap.avaliacao.model.AtualizaAvaliacaoDTO;
import br.com.fiap.avaliacao.model.AvaliacaoDTO;
import br.com.fiap.avaliacao.model.CriaAvaliacaoDTO;
import br.com.fiap.avaliacao.model.NotaRestauranteDTO;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.create.AvaliacaoCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.get.AvaliacaoGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.AvaliacaoListUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.list.byidrestaurant.AvaliacaoListByIdRestauranteUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.retrive.nota.NotaRestauranteGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.avaliacao.update.AvaliacaoUpdateUseCaseOutput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper
public interface AvaliacaoMapper {

    AvaliacaoMapper INSTANCE = Mappers.getMapper(AvaliacaoMapper.class);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    @Mapping(target = "criacao", expression = "java(mapOffsetDateTime(output.criacao()))")
    @Mapping(target = "alteracao", expression = "java(mapOffsetDateTime(output.alteracao()))")
    @Mapping(target = "exclusao", expression = "java(mapOffsetDateTime(output.exclusao()))")
    AvaliacaoDTO toDTO(AvaliacaoCreateUseCaseOutput output);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    @Mapping(target = "criacao", expression = "java(mapOffsetDateTime(output.criacao()))")
    @Mapping(target = "alteracao", expression = "java(mapOffsetDateTime(output.alteracao()))")
    @Mapping(target = "exclusao", expression = "java(mapOffsetDateTime(output.exclusao()))")
    AvaliacaoDTO toDTO(AvaliacaoGetByIdUseCaseOutput output);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    @Mapping(target = "criacao", expression = "java(mapOffsetDateTime(output.criacao()))")
    @Mapping(target = "alteracao", expression = "java(mapOffsetDateTime(output.alteracao()))")
    @Mapping(target = "exclusao", expression = "java(mapOffsetDateTime(output.exclusao()))")
    AvaliacaoDTO toDTO(AvaliacaoListUseCaseOutput output);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    @Mapping(target = "criacao", expression = "java(mapOffsetDateTime(output.criacao()))")
    @Mapping(target = "alteracao", expression = "java(mapOffsetDateTime(output.alteracao()))")
    @Mapping(target = "exclusao", expression = "java(mapOffsetDateTime(output.exclusao()))")
    AvaliacaoDTO toDTO(AvaliacaoListByIdRestauranteUseCaseOutput output);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    @Mapping(target = "criacao", expression = "java(mapOffsetDateTime(output.criacao()))")
    @Mapping(target = "alteracao", expression = "java(mapOffsetDateTime(output.alteracao()))")
    @Mapping(target = "exclusao", expression = "java(mapOffsetDateTime(output.exclusao()))")
    AvaliacaoDTO toDTO(AvaliacaoUpdateUseCaseOutput output);

    NotaRestauranteDTO toDTO(NotaRestauranteGetByIdUseCaseOutput output);

    AvaliacaoCreateUseCaseInput fromDTO(String reservaId, String usuarioId, CriaAvaliacaoDTO dto);

    AvaliacaoListUseCaseInput fromDTO(Page page, boolean ativo);

    AvaliacaoListByIdRestauranteUseCaseInput fromDTO(Page page, String restauranteId);

    AvaliacaoUpdateUseCaseInput fromDTO(String id, AtualizaAvaliacaoDTO dto);

    @Mapping(target = "id", expression = "java(new AvaliacaoId(entity.getId()))")
    Avaliacao toObject(AvaliacaoJPAEntity entity);

    default OffsetDateTime mapOffsetDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
