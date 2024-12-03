package br.com.fiap.reservarestaurante.application.usecases.usuario.create;

import br.com.fiap.reservarestaurante.application.domain.usuario.Usuario;
import br.com.fiap.reservarestaurante.application.exceptions.UsuarioException;
import br.com.fiap.reservarestaurante.application.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public class DefaultUsuarioCreateUseCase extends UsuarioCreateUseCase {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UsuarioCreateUseCaseOutput execute(UsuarioCreateUseCaseInput input) {
        if (usuarioRepository.buscarPorId(input.usuarioId()).isPresent()) {
            throw new UsuarioException(
                    "Usuario ja cadastrado no sistema", HttpStatus.CONFLICT);
        }

        final var novoUsuario =
                Usuario.novo(input.nome(), input.email(), input.senha());
        return UsuarioCreateUseCaseOutput.from(usuarioRepository.criar(novoUsuario));
    }
}
