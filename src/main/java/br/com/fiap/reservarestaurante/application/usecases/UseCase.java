package br.com.fiap.reservarestaurante.application.usecases;

public abstract class UseCase<I, O> {
  public abstract O execute(I input);
}
