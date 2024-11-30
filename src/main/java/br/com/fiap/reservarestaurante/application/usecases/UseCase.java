package br.com.fiap.reservarestaurante.application.usecases;

public abstract class UseCase<IN, OUT> {

  public abstract OUT execute(IN input);
}
