package br.com.fiap.reservarestaurante.infrastructure.repositories;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.AvaliacaoId;
import br.com.fiap.reservarestaurante.application.domain.avaliacao.NotaRestaurante;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.AvaliacaoJPARepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvaliacaoRepositoryImpl implements AvaliacaoRepository {

  private final AvaliacaoJPARepository avaliacaoJPARepository;

  @Override
  @Transactional
  public Avaliacao criar(final Avaliacao avaliacao) {
    return save(avaliacao);
  }

  @Override
  public Pagination<Avaliacao> buscarTudo(Page page, boolean ativo) {
    final var withPage = Pageable.ofSize(page.perPage()).withPage(page.currentPage());
    final var pageResult = avaliacaoJPARepository.findAllByAtivo(withPage, ativo);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(AvaliacaoJPAEntity::toAvaliacao).toList());
  }

  @Override
  public Optional<Avaliacao> buscarPorId(final AvaliacaoId id) {
    return avaliacaoJPARepository.findById(id.value()).map(AvaliacaoJPAEntity::toAvaliacao);
  }

  @Override
  public Pagination<Avaliacao> buscarPorIdRestaurante(Page page, String id) {
    final var withPage = Pageable.ofSize(page.perPage()).withPage(page.currentPage());
    final var pageResult = avaliacaoJPARepository.buscarPorIdRestaurante(withPage, id);
    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(AvaliacaoJPAEntity::toAvaliacao).toList());
  }

  @Override
  public Avaliacao atualizar(Avaliacao avaliacao) {
    return save(avaliacao);
  }

  @Override
  public Optional<NotaRestaurante> buscarNotaRestaurante(String id) {
    return avaliacaoJPARepository.buscarNotaRestaurante(id);
  }

  @Override
  public Optional<Avaliacao> buscarPorIdReserva(final String reservaId) {
    return avaliacaoJPARepository.findByReservaId(reservaId).map(AvaliacaoJPAEntity::toAvaliacao);
  }

  private Avaliacao save(final Avaliacao avaliacao) {
    return avaliacaoJPARepository.save(AvaliacaoJPAEntity.of(avaliacao)).toAvaliacao();
  }
}
