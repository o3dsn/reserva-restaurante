package br.com.fiap.reservarestaurante.application.usecases.avaliacao.create;

import br.com.fiap.reservarestaurante.application.domain.avaliacao.Avaliacao;
import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.repositories.AvaliacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultAvaliacaoCreateUseCase extends AvaliacaoCreateUseCase {

    private final AvaliacaoRepository avaliacaoRepository;

    @Override
    public AvaliacaoCreateUseCaseOutput execute(AvaliacaoCreateUseCaseInput input) {
        /* TODO
            - pegar reserva e ver se esta finalizada: input.reservaId()
            - pegar usuario e ver se é o mesmo da reserva: input.usuarioId()
            - validar se data alteração tem menos de 24h da data atual
         */
        if (avaliacaoRepository.buscarPorIdReserva(input.reservaId()).isPresent()) {
            throw new AvaliacaoException("Avaliação já registrada para essa reserva", HttpStatus.CONFLICT);
        }

        final var novaAvaliacao = Avaliacao.nova(input.reservaId(), input.usuarioId(), input.comentario(), input.nota());
        return AvaliacaoCreateUseCaseOutput.from(avaliacaoRepository.criar(novaAvaliacao));
    }

}
