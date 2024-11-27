package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AvaliacaoJPARepository extends JpaRepository<AvaliacaoJPAEntity, String> {

  @Query(
      """
                  SELECT av FROM AvaliacaoJPAEntity av
                  JOIN ReservaJPAEntity re on (av.reservaId = re.id)
                  WHERE re.restauranteId = :id
                  AND av.ativo = true
                  ORDER BY av.criacao
              """)
  Page<AvaliacaoJPAEntity> buscarPorIdRestaurante(
      Pageable pageable, @Param("id") String restauranteId);

  @Query(
      """
      SELECT new br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante(
          re.restauranteId,
          count(av),
          SUM(av.nota) / count(av)
      )
      FROM AvaliacaoJPAEntity av
      JOIN ReservaJPAEntity re on (av.reservaId = re.id)
      WHERE re.restauranteId = :id
      AND av.ativo = true
      GROUP by re.restauranteId
  """)
  Optional<NotaRestaurante> buscarNotaRestaurante(@Param("id") String restauranteId);

  Page<AvaliacaoJPAEntity> findAllByAtivo(Pageable page, boolean ativo);

  Optional<AvaliacaoJPAEntity> findByReservaId(String reservaId);
}
