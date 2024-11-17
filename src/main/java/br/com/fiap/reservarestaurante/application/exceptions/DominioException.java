package br.com.fiap.reservarestaurante.application.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record DominioException (HttpStatus status, Map<String, String> erros) {}
