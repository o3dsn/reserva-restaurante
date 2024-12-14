package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.RestauranteJPAEntity;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class ReservaJPARepositoryIT {

    private final ReservaJPARepository reservaJPARepository;

    @Autowired
    public ReservaJPARepositoryIT(ReservaJPARepository repository) {
        this.reservaJPARepository = repository;
    }

    @Nested
    class Criar {

        @Test
        void devePermitirCriarReserva() {
            var reserva = ReservaHelper.gerarReservaJPAEntityNova();
            var reservaJPA = reservaJPARepository.save(reserva);

            assertThat(reservaJPA).isInstanceOf(ReservaJPAEntity.class).isNotNull().isEqualTo(reserva);

            ReservaHelper.validar(reserva, reservaJPA);
        }
    }

    @Nested
    class Buscar {

        @Test
        void devePermitirBuscarTodosRestaurantes() {
            var reservaPaginado = reservaJPARepository.findAll(Pageable.unpaged());

            assertThat(reservaPaginado).hasSizeGreaterThanOrEqualTo(1);

            assertThat(reservaPaginado.getContent().get(0)).isInstanceOf(ReservaJPAEntity.class);
        }
    }
}
