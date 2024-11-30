package br.com.fiap.reservarestaurante.application.exceptions;

public class UsuarioException extends RuntimeException {
  public UsuarioException(String mensagem) {
    super(mensagem);
  }
}
