package br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get;

import br.com.fiap.reservarestaurante.application.domain.usuario.UsuarioId;
import br.com.fiap.reservarestaurante.application.exceptions.UsuarioException;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUsuarioGetByIdUseCase extends UsuarioGetByIdUseCase {

  private final UsuarioRepository usuarioRepository;

  @Override
  public UsuarioGetByIdUseCaseOutPut execute(String usuarioId) {
    final var id = new UsuarioId(usuarioId);

    return usuarioRepository
        .buscarPorId(id)
        .map(UsuarioGetByIdUseCaseOutPut::from)
        .orElseThrow(
            () ->
                new UsuarioException(
                    "Usuario com ID %s não encontrado.".formatted(usuarioId),
                    org.springframework.http.HttpStatus.BAD_REQUEST));
  }
}
