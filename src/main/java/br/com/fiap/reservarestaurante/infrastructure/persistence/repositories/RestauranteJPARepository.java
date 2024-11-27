package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestauranteJPARepository extends JpaRepository<RestauranteJPAEntity, String> {

  Page<RestauranteJPAEntity> findAll(Pageable pageable);
}
