package br.com.fiap.reservarestaurante.infrastructure.mappers;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.get.RestauranteGetByIdUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCaseOutput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCaseOutput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.restaurante.model.AtualizaRestauranteDTO;
import br.com.fiap.restaurante.model.CriaRestauranteDTO;
import br.com.fiap.restaurante.model.RestauranteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    imports = {RestauranteId.class})
public interface RestauranteMapper {

  RestauranteMapper INSTANCE = Mappers.getMapper(RestauranteMapper.class);

  @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
  RestauranteDTO toDTO(RestauranteCreateUseCaseOutput output);

  @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
  RestauranteDTO toDTO(RestauranteGetByIdUseCaseOutput output);

  @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
  RestauranteDTO toDTO(RestauranteListUseCaseOutput output);

  @Mapping(target = "id", expression = "java(java.util.UUID.fromString(output.id().toString()))")
  RestauranteDTO toDTO(RestauranteUpdateUseCaseOutput output);

  RestauranteCreateUseCaseInput fromDTO(CriaRestauranteDTO dto);

  RestauranteListUseCaseInput fromDTO(Page page);

  RestauranteUpdateUseCaseInput fromDTO(String id, AtualizaRestauranteDTO dto);

  RestauranteUpdateUseCaseInput fromDTO(String id);

  @Mapping(target = "id", expression = "java(new RestauranteId(entity.getId()))")
  Restaurante toObject(RestauranteJPAEntity entity);
}
