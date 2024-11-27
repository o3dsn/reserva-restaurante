package br.com.fiap.reservarestaurante.infrastructure.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import br.com.fiap.reservarestaurante.application.repositories.RestauranteRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.RestauranteJPARepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestauranteRepositoryImpl implements RestauranteRepository {

  private final RestauranteJPARepository restauranteJPARepository;

  @Override
  public Restaurante criar(Restaurante restaurante) {
    return save(restaurante);
  }

  @Override
  public Restaurante atualizar(Restaurante restaurante) {
    return save(restaurante);
  }

  @Override
  public Pagination<Restaurante> buscarTudo(Page page) {
    final var withPage = Pageable.ofSize(page.perPage()).withPage(page.currentPage());
    final var pageResult = restauranteJPARepository.findAll(withPage);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(RestauranteJPAEntity::toRestaurante).toList());
  }

  @Override
  public Optional<Restaurante> buscarPorId(RestauranteId id) {
    return restauranteJPARepository.findById(id.value()).map(RestauranteJPAEntity::toRestaurante);
  }

  @Override
  public void deletarRestaurante(Restaurante restaurante) {
    restauranteJPARepository.delete(RestauranteJPAEntity.of(restaurante));
  }

  private Restaurante save(final Restaurante restaurante) {
    return restauranteJPARepository.save(RestauranteJPAEntity.of(restaurante)).toRestaurante();
  }
}
