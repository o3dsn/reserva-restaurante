package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.usuario.retrive.get.UsuarioGetByIdUseCase;
import br.com.fiap.reservarestaurante.infrastructure.mappers.UsuarioMapper;
import br.com.fiap.usuario.api.UsuarioApi;
import br.com.fiap.usuario.model.CadastrarUsuarioDTO;
import br.com.fiap.usuario.model.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioApi {

    private static final UsuarioMapper mapper = UsuarioMapper.INSTANCE;
    private final UsuarioCreateUseCase usuarioCreateUseCase;
    private final UsuarioGetByIdUseCase usuarioGetByIdUseCase;

    @Override
    public ResponseEntity<UsuarioDTO> cadastrarUsuario(CadastrarUsuarioDTO body) {
        final var useCaseInput = mapper.fromDTO(body);
        final var useCaseOutput = mapper.toDTO(usuarioCreateUseCase.execute(useCaseInput));

        var uri = URI.create("/usuarios/" + useCaseOutput.getId());

        return ResponseEntity.created(uri).body(useCaseOutput);
    }

    @Override
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(UUID id) {
        final var useCaseOutput = usuarioGetByIdUseCase.execute(id.toString());
        return ResponseEntity.ok(mapper.toDTO(useCaseOutput));
    }

}
