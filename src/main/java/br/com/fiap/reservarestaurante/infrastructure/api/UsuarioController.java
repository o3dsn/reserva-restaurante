package br.com.fiap.reservarestaurante.infrastructure.api;


import br.com.fiap.reservarestaurante.infrastructure.mappers.UsuarioMapper;
import br.com.fiap.usuario.api.UsuariosApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController implements UsuariosApi {

  private static final UsuarioMapper mapper = UsuarioMapper.INSTANCE;

//  private final UsuarioCreateUseCase usuarioCreateUseCase;


  @GetMapping("/hola")
  public ResponseEntity<String> hola() {
    return ResponseEntity.ok("hola");
  }
}
