package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.restaurante.Restaurante;
import br.com.fiap.reservarestaurante.application.domain.restaurante.RestauranteId;
import java.util.Optional;

public interface RestauranteRepository {

  Restaurante criar(Restaurante restaurante);

  Restaurante atualizar(Restaurante restaurante);

  Pagination<Restaurante> buscarTudo(Page page);

  Optional<Restaurante> buscarPorId(RestauranteId id);

  void deletarRestaurante(Restaurante restaurante);
}
