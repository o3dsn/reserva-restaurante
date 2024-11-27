package br.com.fiap.reservarestaurante.application.domain.restaurante;

import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import java.util.UUID;
import org.springframework.http.HttpStatus;

public record RestauranteId(String value) {

  public static RestauranteId from(String value) {
    try {
      return new RestauranteId(UUID.fromString(value).toString());
    } catch (IllegalArgumentException e) {
      throw new RestauranteException(
          "Restaurante com ID %s não é valido.".formatted(value), HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
