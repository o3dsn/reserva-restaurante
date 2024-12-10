package br.com.fiap.reservarestaurante.application.usecases;

public abstract class UnitUseCase<I> {
  public abstract void execute(I input);
}
