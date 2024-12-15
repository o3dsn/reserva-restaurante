package br.com.fiap.reservarestaurante.infrastructure.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.application.domain.reserva.ReservaId;
import br.com.fiap.reservarestaurante.application.repositories.ReservaRepository;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.ReservaJPARepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class ReservaRepositoryImplIT {

  private final ReservaRepository reservaRepository;

  @Autowired
  public ReservaRepositoryImplIT(ReservaJPARepository reservaJPARepository) {
    this.reservaRepository = new ReservaRepositoryImpl(reservaJPARepository);
  }

  @Nested
  class Criar {
    @Test
    void devePermitirCriarReserva() {
      var reserva = ReservaHelper.gerarReservaNova();
      var reservaCriada = reservaRepository.criar(reserva);

      assertThat(reservaCriada).isInstanceOf(Reserva.class).isNotNull();
      assertThat(reservaCriada).extracting(Reserva::getId).isNotNull();
      assertThat(reservaCriada)
          .extracting(Reserva::getRestauranteId)
          .isEqualTo(reserva.getRestauranteId());
      assertThat(reservaCriada).extracting(Reserva::getUsuarioId).isEqualTo(reserva.getUsuarioId());
      assertThat(reservaCriada)
          .extracting(Reserva::getComentario)
          .isEqualTo(reserva.getComentario());
      assertThat(reservaCriada).extracting(Reserva::getStatus).isEqualTo(reserva.getStatus());
      assertThat(reservaCriada)
          .extracting(Reserva::getDataHorarioReserva)
          .isEqualTo(reserva.getDataHorarioReserva());
      assertThat(reservaCriada).extracting(Reserva::getCriacao).isEqualTo(reserva.getCriacao());
      assertThat(reservaCriada).extracting(Reserva::getAlteracao).isEqualTo(reserva.getAlteracao());
    }
  }

  @Nested
  class Atualizar {
    @Test
    void devePermitirAtualizarReserva() {
      var id = reservaRepository.criar(ReservaHelper.gerarReservaNova()).getId();
      var reservaOptional = reservaRepository.buscarPorId(id);
      var reserva = reservaOptional.orElseThrow();

      reserva.atualizar(ReservaDTO.StatusEnum.CONFIRMADA);

      var reservaAtualizada = reservaRepository.atualizar(reserva);

      assertThat(reservaAtualizada).isNotNull().isInstanceOf(Reserva.class);

      ReservaHelper.validar(reservaAtualizada, reserva);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasReservas() {
      var page = new Page(0, 10);
      var reservaPaginada = reservaRepository.buscarTudo(page);

      assertThat(reservaPaginada).extracting(Pagination::currentPage).isEqualTo(0);
      assertThat(reservaPaginada).extracting(Pagination::perPage).isEqualTo(10);
      assertThat(reservaPaginada).extracting(Pagination::total).isEqualTo(6L);
      assertThat(reservaPaginada.items()).hasSize(6);
    }

    @Test
    void devePermitirBuscarReservaPorId() {
      var reservaId = ReservaId.from("093bff48-6e42-4939-99d9-959f61b41fdd");
      var reservaOptional = reservaRepository.buscarPorId(reservaId);

      assertThat(reservaOptional).isPresent().isInstanceOf(Optional.class);
    }
  }
}
