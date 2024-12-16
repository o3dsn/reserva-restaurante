package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.ReservaJPAEntity;
import br.com.fiap.reservarestaurante.utils.ReservaHelper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ReservaJPARepositoryTest {
  AutoCloseable openMocks;
  @Mock private ReservaJPARepository reservaJPARepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  class Criar {
    @Test
    void devePermitirCriarReserva() {
      var reserva = ReservaHelper.gerarReservaJPAEntity();

      ReservaJPARepository reservaJPARepositoryMock = mock(ReservaJPARepository.class);

      when(reservaJPARepositoryMock.save(any(ReservaJPAEntity.class))).thenReturn(reserva);

      var reservaArmazenado = reservaJPARepositoryMock.save(reserva);

      assertThat(reservaArmazenado)
          .isInstanceOf(ReservaJPAEntity.class)
          .isNotNull()
          .isEqualTo(reserva);

      ReservaHelper.validar(reservaArmazenado, reserva);

      verify(reservaJPARepositoryMock, times(1)).save(reserva);
    }
  }

  @Nested
  class Buscar {

    @Test
    void devePermitirBuscarTodasReservas() {
      var reserva1 = ReservaHelper.gerarReservaJPAEntity("f05e678f-4adf-4de9-94b2-b36fdbdfd57d");
      var reserva2 = ReservaHelper.gerarReservaJPAEntity("5dfb6ef0-9b02-4fe5-bfba-042b0c94d385");
      var listOfReserva = Arrays.asList(reserva1, reserva2);

      when(reservaJPARepository.findAll(any(Pageable.class)))
          .thenReturn(new PageImpl<>(listOfReserva));

      var reservaPaginado = reservaJPARepository.findAll(Pageable.unpaged());

      assertThat(reservaPaginado).hasSize(2).containsExactlyInAnyOrder(reserva1, reserva2);

      verify(reservaJPARepository, times(1)).findAll(Pageable.unpaged());
    }

    @Test
    void devePermitirBuscarReservaPorId() {
      var id = "b0a41850-23cf-4f5e-94f8-2bc22b6ed30a";
      var reserva = ReservaHelper.gerarReservaJPAEntity(id);

      when(reservaJPARepository.findById(String.valueOf(any(String.class))))
          .thenReturn(Optional.of(reserva));

      var reservaOptional = reservaJPARepository.findById(id);

      assertThat(reservaOptional).isPresent().containsSame(reserva);

      reservaOptional.ifPresent(
          reservaArmazenado -> ReservaHelper.validar(reservaArmazenado, reserva));

      verify(reservaJPARepository, times(1)).findById(id);
    }
  }
}
