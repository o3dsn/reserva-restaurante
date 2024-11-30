package br.com.fiap.reservarestaurante.application.usecases;

public abstract class UnitUseCase<IN> {

  public abstract void execute(IN input);
}
