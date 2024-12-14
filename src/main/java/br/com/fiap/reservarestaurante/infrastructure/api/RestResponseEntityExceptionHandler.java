package br.com.fiap.reservarestaurante.infrastructure.api;

import br.com.fiap.reservarestaurante.application.exceptions.AvaliacaoException;
import br.com.fiap.reservarestaurante.application.exceptions.DominioException;
import java.util.HashMap;
import java.util.Map;

import br.com.fiap.reservarestaurante.application.exceptions.ReservaException;
import br.com.fiap.reservarestaurante.application.exceptions.RestauranteException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private final Map<String, String> errors = new HashMap<>();

  @ExceptionHandler(AvaliacaoException.class)
  public ResponseEntity<DominioException> avaliacaoException(AvaliacaoException ex) {
    this.limparErrors();
    errors.put("erro", ex.getMessage());
    return ResponseEntity.status(ex.getStatus()).body(new DominioException(ex.getStatus(), errors));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    this.limparErrors();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return ResponseEntity.status(status).body(errors);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    this.limparErrors();
    errors.put(ex.getParameterName(), ex.getMessage());
    return ResponseEntity.badRequest().body(new DominioException(HttpStatus.BAD_REQUEST, errors));
  }

  @ExceptionHandler(RestauranteException.class)
  public ResponseEntity<DominioException> restauranteException(RestauranteException ex) {
    this.limparErrors();
    errors.put("erro", ex.getMessage());
    return ResponseEntity.status(ex.getStatus()).body(new DominioException(ex.getStatus(), errors));
  }

  private void limparErrors() {
    errors.clear();
  }

  @ExceptionHandler(ReservaException.class)
  public ResponseEntity<DominioException> reservaException(ReservaException ex) {
    this.limparErrors();
    errors.put("erro", ex.getMessage());
    return ResponseEntity.status(ex.getStatus()).body(new DominioException(ex.getStatus(), errors));
  }
}
