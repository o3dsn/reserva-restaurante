package br.com.fiap.reservarestaurante.infrastructure.persistence.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.fiap.reservarestaurante.infrastructure.persistence.entities.UsuarioJPAEntity;
import br.com.fiap.reservarestaurante.utils.UsuarioHelper;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class UsuarioJPARepositoryTest {

  AutoCloseable openMocks;

  @Mock private UsuarioJPARepository usuarioJPARepository;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Nested
  class Criar {

    @Test
    void devePermitirCriarUsuario() {

      var usuario = UsuarioHelper.gerarUsuarioJPAEntityNovo();
      UsuarioJPARepository usuarioJPARepositoryMock = mock(UsuarioJPARepository.class);
      when(usuarioJPARepositoryMock.save(any(UsuarioJPAEntity.class))).thenReturn(usuario);
      var usuarioArmazenado = usuarioJPARepositoryMock.save(usuario);

      verify(usuarioJPARepositoryMock, times(1)).save(usuario);
      assertThat(usuarioArmazenado)
          .isInstanceOf(UsuarioJPAEntity.class)
          .isNotNull()
          .isEqualTo(usuario);
      assertThat(usuario).isNotNull();
      Assertions.assertEquals(usuarioArmazenado, usuario);
    }
  }

  @Nested
  class Buscar {
    @Test
    void devePermitirBuscarTodosUsuarios() {

      var usuario1 = UsuarioHelper.gerarUsuarioJPAEntityNovo();
      var usuario2 = UsuarioHelper.gerarUsuarioJPAEntityNovo();

      var listOfUsuario = Arrays.asList(usuario1, usuario2);
      when(usuarioJPARepository.findAll(any(Pageable.class)))
          .thenReturn((Page<UsuarioJPAEntity>) new PageImpl<>(listOfUsuario));
      var resultadoPaginadoUsuario = usuarioJPARepository.findAll(Pageable.unpaged());

      assertThat(resultadoPaginadoUsuario).hasSize(2).containsExactlyInAnyOrder(usuario1, usuario2);
      verify(usuarioJPARepository, times(1)).findAll(Pageable.unpaged());
    }

    @Test
    void devePermitirBuscarUsuarioPorId() {
      var id = "b0a41850-23cf-4f5e-94f8-2bc22b6ed30a";

      var usuario = UsuarioHelper.gerarUsuarioJPAEntityNovo();

      when(usuarioJPARepository.findById(String.valueOf(any(String.class))))
          .thenReturn(Optional.of(usuario));

      var usuarioOptional = usuarioJPARepository.findById(id);

      assertThat(usuarioOptional).isPresent().containsSame(usuario);
      assertThat(usuarioOptional.get().getId()).isEqualTo(usuario.getId());
      verify(usuarioJPARepository, times(1)).findById(id);
    }
  }
}
