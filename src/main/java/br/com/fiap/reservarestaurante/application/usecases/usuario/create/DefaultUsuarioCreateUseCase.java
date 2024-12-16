package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUsuarioCreateUseCase extends UsuarioCreateUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioCreateUseCaseOutput execute(UsuarioCreateUseCaseInput input) {
        final var novoUsuario = Usuario.create( input.id(), input.nome(), input.email(), input.senha());

        var user  = usuarioRepository.criar(novoUsuario);

        return UsuarioCreateUseCaseOutput.from(user);
    }
}
