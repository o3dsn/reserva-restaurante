package br.com.fiap.reservarestaurante.application.exceptions;

import java.util.Map;
import org.springframework.http.HttpStatus;

public record DominioException (HttpStatus status, Map<String, String> erros) {}
