package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCase;
import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCaseOutput;
import br.com.fiap.reservarestaurante.infrastructure.mappers.UsuarioMapper;
import br.com.fiap.usuario.api.UsuarioApi;
import br.com.fiap.usuario.model.CadastrarUsuarioDTO;
import br.com.fiap.usuario.model.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioApi {

  private static final UsuarioMapper mapper = UsuarioMapper.INSTANCE;
  private final UsuarioCreateUseCase usuarioCreateUseCase;

  @Override
  public ResponseEntity<UsuarioDTO> cadastrarUsuario(CadastrarUsuarioDTO body) {
    final var useCaseInput = mapper.fromDTO(body);
    final var useCaseOutput = mapper.toDTO(usuarioCreateUseCase.execute(useCaseInput));

    var uri = URI.create("/usuarios/" + useCaseOutput.getId());

   return ResponseEntity.created(uri).body(useCaseOutput);
  //  return null;

  }

}
