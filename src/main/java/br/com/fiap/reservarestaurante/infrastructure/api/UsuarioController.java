package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.usecases.usuario.create.UsuarioCreateUseCase;
import br.com.fiap.reservarestaurante.infrastructure.mappers.UsuarioMapper;
import br.com.fiap.usuario.api.UsuarioApi;
import br.com.fiap.usuario.model.CadastrarUsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuarioApi {

  private static final UsuarioMapper mapper = UsuarioMapper.INSTANCE;
  private final UsuarioCreateUseCase usuarioCreateUseCase;


  @GetMapping("/hola")
  public ResponseEntity<String> hola() {
    return ResponseEntity.ok("hola");
  }

  @Override
  public ResponseEntity<Void> cadastrarUsuario(CadastrarUsuarioDTO body) {
    final var useCaseInput = mapper.fromDTO(body);
    final var useCaseOutput = mapper.toDTO(usuarioCreateUseCase.execute(useCaseInput));
    System.out.println("cadastrarUsuario");

    return ResponseEntity.ok().build();
  }


}
