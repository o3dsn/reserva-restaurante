package br.com.fiap.reservarestaurante.infrastructure.repositories;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.AvaliacaoJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.ReservaJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaRepositoryImpl implements ReservaRepository {
    private final ReservaJPARepository reservaJPARepository;

    @Override
    @Transactional
    public Reserva criar(final Reserva reserva) {
        return save(reserva);
    }

    private Reserva save(final Reserva reserva) {
        return reservaJPARepository.save(ReservaJPAEntity.of(reserva)).toReserva();
    }

    @Override
    public Optional<Reserva> buscarPorId(final ReservaId id) {
        return reservaJPARepository.findById(id.value()).map(ReservaJPAEntity::toReserva);
    }

    @Override
    public Reserva atualizar(Reserva reserva) {
        return save(reserva);
    }

    @Override
    public Pagination<Reserva> buscarTudo(Page page) {
        final var withPage = Pageable.ofSize(page.perPage()).withPage(page.currentPage());
        final var pageResult = reservaJPARepository.findAll(withPage);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(ReservaJPAEntity::toReserva).toList()
        );
    }
}
