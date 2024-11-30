package br.com.fiap.reservarestaurante.application.usecases.reserva.delete;

import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultReservaDeleteUseCase extends ReservaDeleteUseCase {
    private final ReservaRepository reservaRepository;

    @Override
    public void execute(String id) {
        final var reservaId = new ReservaId(id);
        final var reserva = reservaRepository.buscarPorId(reservaId)
                .orElseThrow(() -> new ReservaException("Reserva com ID %s não encontrado.".formatted(reservaId), HttpStatus.NOT_FOUND));

        reserva.excluir();
        reservaRepository.atualizar(reserva);
    }
}
