package br.com.fiap.reservarestaurante.application.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioException extends RuntimeException {

  private final HttpStatus status;

  public UsuarioException(String mensagem, HttpStatus status) {
    super(mensagem);
    this.status = status;
  }
}
