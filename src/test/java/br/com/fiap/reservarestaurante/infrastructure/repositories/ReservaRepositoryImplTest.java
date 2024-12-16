package br.com.fiap.reservarestaurante.infrastructure.repositories;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reserva.model.ReservaDTO;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Page;
import br.com.fiap.reservarestaurante.application.domain.paginacao.Pagination;
import br.com.fiap.reservarestaurante.application.domain.reserva.Reserva;
import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import br.com.fiap.reservarestaurante.infrastructure.persistence.repositories.ReservaJPARepository;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import java.util.Arrays;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ReservaRepositoryImplTest {
  AutoCloseable openMocks;
  private ReservaRepositoryImpl reservaRepository;
  @Mock private ReservaJPARepository reservaJPARepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    reservaRepository = new ReservaRepositoryImpl(reservaJPARepository);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  class Criar {
    @Test
    void devePermitirCriarReserva() {
      var reserva = ReservaHelper.gerarReservaNova();
      var reservaJPAEntity = ReservaHelper.gerarReservaJPAEntityNova();

      when(reservaJPARepository.save(any(ReservaJPAEntity.class)))
          .thenAnswer(i -> i.getArgument(0));

      var reservaCriada = reservaRepository.criar(reserva);

      Assertions.assertThat(reservaCriada).isInstanceOf(Reserva.class).isNotNull();

      ReservaHelper.validar(reservaCriada, reserva);

      verify(reservaJPARepository, times(1)).save(reservaJPAEntity);
    }
  }

  @Nested
  class Atualizar {
    @Test
    void devePermitirAtualizarReserva() {
      var status = ReservaDTO.StatusEnum.CONFIRMADA;
      var reserva = ReservaHelper.gerarReserva("4e83ce7d-ce3a-4882-8e50-f895e77d77a4");
      reserva.atualizar(status);
      var reservaJPAEntity = ReservaJPAEntity.of(reserva);

      when(reservaJPARepository.save(any(ReservaJPAEntity.class)))
          .thenAnswer(i -> i.getArgument(0));

      var reservaAtualizada = reservaRepository.atualizar(reserva);

      Assertions.assertThat(reservaAtualizada).isNotNull().isInstanceOf(Reserva.class);

      ReservaHelper.validar(reservaAtualizada, reserva);

      verify(reservaJPARepository, times(1)).save(reservaJPAEntity);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodasReservas() {
      var page = new Page(0, 10);
      var pageable = Pageable.ofSize(page.perPage()).withPage(page.currentPage());
      var reserva1 = ReservaHelper.gerarReserva("caddcf4d-c37f-47fc-bc77-0699eab2f205");
      var reserva2 = ReservaHelper.gerarReserva("b5657796-c261-4a72-8b86-680a52b8d206");
      var listOfReservaJPAEntity =
          Arrays.asList(ReservaJPAEntity.of(reserva1), ReservaJPAEntity.of(reserva2));

      when(reservaJPARepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(listOfReservaJPAEntity));

      var reservaPaginado = reservaRepository.buscarTudo(page);

      Assertions.assertThat(reservaPaginado).extracting(Pagination::currentPage).isEqualTo(0);
      Assertions.assertThat(reservaPaginado).extracting(Pagination::perPage).isEqualTo(2);
      Assertions.assertThat(reservaPaginado).extracting(Pagination::total).isEqualTo(2L);
      Assertions.assertThat(reservaPaginado.items()).hasSize(2);

      verify(reservaJPARepository, times(1)).findAll(pageable);
    }

    @Test
    void devePermitirBuscarReservaPorId() {
      var reserva = ReservaHelper.gerarReserva("4af2ec02-fa75-4a6a-83f7-7376ac2df0a7");
      var reservaId = reserva.getId();
      var reservaJPAEntity = ReservaJPAEntity.of(reserva);

      when(reservaJPARepository.findById(any(String.class)))
          .thenReturn(Optional.of(reservaJPAEntity));

      var reservaOptional = reservaRepository.buscarPorId(reservaId);

      Assertions.assertThat(reservaOptional).isPresent().isInstanceOf(Optional.class);

      ReservaHelper.validar(reservaOptional.orElseThrow(), reserva);

      verify(reservaJPARepository, times(1)).findById(reservaId.value());
    }
  }
}
