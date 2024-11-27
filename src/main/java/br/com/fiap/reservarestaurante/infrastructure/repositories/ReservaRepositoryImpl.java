package br.com.fiap.reservarestaurante.infrastructure.repositories;

import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.ReservaJPARepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
