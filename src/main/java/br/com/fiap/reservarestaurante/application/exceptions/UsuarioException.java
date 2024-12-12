package br.com.fiap.reservarestaurante.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsuarioException extends RuntimeException {

  private final HttpStatus status;
  public UsuarioException(String mensagem, HttpStatus status) {
    super(mensagem);
    this.status = status;
  }
}
