package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaJPARepository extends JpaRepository<ReservaJPAEntity, String> {}
