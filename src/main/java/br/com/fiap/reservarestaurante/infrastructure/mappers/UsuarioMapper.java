package br.com.fiap.reservarestaurante.infrastructure.mappers;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.usuario.model.CadastrarUsuarioDTO;
import br.com.fiap.usuario.model.UsuarioDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
imports = {UsuarioId.class})
public interface UsuarioMapper {

    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);

    @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
    UsuarioDTO toDTO(UsuarioCreateUseCaseOutput output);

    UsuarioCreateUseCaseInput fromDTO(CadastrarUsuarioDTO dto);

    @Mapping(target = "id", expression = "java(new UsuarioId(entity.getId()))")
    Usuario toObject(UsuarioJPAEntity entity);
}
