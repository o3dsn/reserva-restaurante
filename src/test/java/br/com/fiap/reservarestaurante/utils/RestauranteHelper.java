package br.com.fiap.reservarestaurante.utils;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.create.RestauranteCreateUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.retrive.list.RestauranteListUseCaseInput;
import br.com.fiap.reservarestaurante.application.usecases.restaurente.update.RestauranteUpdateUseCaseInput;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.restaurante.model.AtualizaRestauranteDTO;
import br.com.fiap.restaurante.model.CriaRestauranteDTO;

public final class RestauranteHelper {

  public static final String NOME = "Restaurante Exemplo";
  public static final String DESCRICAO = "Um restaurante com pratos deliciosos.";
  public static final String ENDERECO = "Rua Exemplo, 123";
  public static final String CIDADE = "São Paulo";
  public static final String ESTADO = "SP";
  public static final String BAIRRO = "Centro";
  public static final String TIPO_COZINHA_ID = "4e5d6c7f-1234-5678-9101-11213d41516a";
  public static final String FAIXA_PRECO = "Média";
  public static final String TELEFONE = "+55 11 98765-4321";
  public static final String EMAIL = "contato@restauranteexemplo.com";
  public static final Double AVALIACAO_MEDIA = 4.5;
  public static final Long AVALIACAO_TOTAL = 120L;
  public static final String HORARIO_ABERTURA = "10:00";
  public static final String HORARIO_FECHAMENTO = "22:00";

  public static RestauranteCreateUseCaseInput gerarRestauranteCreateUseCaseInput() {
    return new RestauranteCreateUseCaseInput(
        NOME,
        DESCRICAO,
        ENDERECO,
        CIDADE,
        ESTADO,
        BAIRRO,
        TIPO_COZINHA_ID,
        FAIXA_PRECO,
        TELEFONE,
        EMAIL,
        AVALIACAO_MEDIA,
        AVALIACAO_TOTAL,
        HORARIO_ABERTURA,
        HORARIO_FECHAMENTO);
  }

  public static Restaurante gerarRestaurante(String id) {
    return gerar(id);
  }

  public static RestauranteListUseCaseInput gerarRestauranteListUseCaseInput() {
    return new RestauranteListUseCaseInput(new Page(0, 10));
  }

  public static RestauranteJPAEntity gerarRestauranteJPAEntityNovo() {
    var restaurante = gerarRestauranteNovo();
    return RestauranteJPAEntity.of(restaurante);
  }

  public static RestauranteJPAEntity gerarRestauranteJPAEntity(String id) {
    var restaurante = gerarRestaurante(id);
    return RestauranteJPAEntity.of(restaurante);
  }

  public static Restaurante gerarRestauranteNovo() {
    return Restaurante.nova(gerarRestaurante("2cc9c534-9900-44b7-a0d2-551d38d82953"));
  }

  public static CriaRestauranteDTO gerarCriaRestauranteDTO() {
    return new CriaRestauranteDTO()
        .nome(NOME)
        .descricao(DESCRICAO)
        .endereco(ENDERECO)
        .cidade(CIDADE)
        .estado(ESTADO)
        .bairro(BAIRRO)
        .tipoCozinhaId(TIPO_COZINHA_ID)
        .faixaPreco(FAIXA_PRECO)
        .telefone(TELEFONE)
        .email(EMAIL)
        .horarioAbertura(HORARIO_ABERTURA)
        .horarioFechamento(HORARIO_FECHAMENTO);
  }

  public static Restaurante gerarRestauranteAlterado(String id) {
    var atualizaRestauranteDTO = gerarAtualizaRestauranteDTO();
    return new Restaurante(
        RestauranteId.from(id),
        atualizaRestauranteDTO.getNome(),
        atualizaRestauranteDTO.getDescricao(),
        atualizaRestauranteDTO.getEndereco(),
        atualizaRestauranteDTO.getCidade(),
        atualizaRestauranteDTO.getEstado(),
        atualizaRestauranteDTO.getBairro(),
        atualizaRestauranteDTO.getTipoCozinhaId(),
        atualizaRestauranteDTO.getFaixaPreco(),
        atualizaRestauranteDTO.getTelefone(),
        atualizaRestauranteDTO.getEmail(),
        4.9,
        100L,
        atualizaRestauranteDTO.getHorarioAbertura(),
        atualizaRestauranteDTO.getHorarioFechamento());
  }

  public static RestauranteUpdateUseCaseInput gerarRestauranteUpdateUseCaseInput(String id) {
    return new RestauranteUpdateUseCaseInput(
        id,
        NOME,
        DESCRICAO,
        ENDERECO,
        CIDADE,
        ESTADO,
        BAIRRO,
        TIPO_COZINHA_ID,
        FAIXA_PRECO,
        TELEFONE,
        EMAIL,
        AVALIACAO_MEDIA,
        AVALIACAO_TOTAL,
        HORARIO_ABERTURA,
        HORARIO_FECHAMENTO);
  }

  public static AtualizaRestauranteDTO gerarAtualizaRestauranteDTO() {
    return new AtualizaRestauranteDTO()
        .nome(NOME)
        .descricao(DESCRICAO)
        .endereco(ENDERECO)
        .cidade(CIDADE)
        .estado(ESTADO)
        .bairro(BAIRRO)
        .tipoCozinhaId(TIPO_COZINHA_ID)
        .faixaPreco(FAIXA_PRECO)
        .telefone(TELEFONE)
        .email(EMAIL)
        .horarioAbertura(HORARIO_ABERTURA)
        .horarioFechamento(HORARIO_FECHAMENTO);
  }

  public static Restaurante gerar(String id) {
    return new Restaurante(
        RestauranteId.from(id),
        NOME,
        DESCRICAO,
        ENDERECO,
        CIDADE,
        ESTADO,
        BAIRRO,
        TIPO_COZINHA_ID,
        FAIXA_PRECO,
        TELEFONE,
        EMAIL,
        AVALIACAO_MEDIA,
        AVALIACAO_TOTAL,
        HORARIO_ABERTURA,
        HORARIO_FECHAMENTO);
  }

  public static void validar(Restaurante restauranteArmazenado, Restaurante restaurante) {
    validar(RestauranteJPAEntity.of(restauranteArmazenado), RestauranteJPAEntity.of(restaurante));
  }

  public static void validar(
      RestauranteJPAEntity restauranteArmazenado, RestauranteJPAEntity restaurante) {
    assertThat(restauranteArmazenado).isNotNull();
    assertThat(restaurante).isNotNull();

    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getId)
        .isEqualTo(restaurante.getId());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getNome)
        .isEqualTo(restaurante.getNome());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getDescricao)
        .isEqualTo(restaurante.getDescricao());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getEndereco)
        .isEqualTo(restaurante.getEndereco());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getCidade)
        .isEqualTo(restaurante.getCidade());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getEstado)
        .isEqualTo(restaurante.getEstado());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getBairro)
        .isEqualTo(restaurante.getBairro());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getTipoCozinhaId)
        .isEqualTo(restaurante.getTipoCozinhaId());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getFaixaPreco)
        .isEqualTo(restaurante.getFaixaPreco());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getTelefone)
        .isEqualTo(restaurante.getTelefone());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getEmail)
        .isEqualTo(restaurante.getEmail());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getHorarioAbertura)
        .isEqualTo(restaurante.getHorarioAbertura());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getHorarioFechamento)
        .isEqualTo(restaurante.getHorarioFechamento());
    assertThat(restauranteArmazenado)
        .extracting(RestauranteJPAEntity::getTipoCozinhaId)
        .isEqualTo(restaurante.getTipoCozinhaId());
  }
}
