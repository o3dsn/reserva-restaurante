package br.com.fiap.reservarestaurante.infrastructure.mappers;

import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseOutput;
import br.com.fiap.restaurante.model.RestauranteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
imports = {UsuarioId.class})
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    UsuarioDTO toDTO(RestauranteCreateUseCaseOutput output);


}
