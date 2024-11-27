package br.com.fiap.reservarestaurante.application.repositories;

import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;

public interface ReservaRepository {
  Reserva criar(Reserva reserva);
}
