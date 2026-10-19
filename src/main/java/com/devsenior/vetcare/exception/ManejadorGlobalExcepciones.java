package com.devsenior.vetcare.exception;

import com.devsenior.vetcare.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

// Cada error del negocio responde con su codigo HTTP honesto
// y siempre con el mismo formato: { codigo, mensaje, fecha }
@RestControllerAdvice
public class ManejadorGlobalExcepciones {

    // 400: fallo una validacion de @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .orElse("Error de validacion");
        return responder(HttpStatus.BAD_REQUEST, mensaje);
    }

    // 400: el JSON llego mal armado (ej: un rol o una fecha que no existe)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex) {
        return responder(HttpStatus.BAD_REQUEST, "El cuerpo de la peticion tiene un formato invalido");
    }

    // 401: usuario o contraseña incorrectos en el login
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> credenciales(AuthenticationException ex) {
        return responder(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
    }

    // 403: autenticado, pero su rol no le permite esta accion
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accesoDenegado(AccessDeniedException ex) {
        return responder(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta accion");
    }

    // 404: recurso no encontrado
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException ex) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409: la peticion es valida, pero el negocio no la permite
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaNegocio(ReglaNegocioException ex) {
        return responder(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 409: dato repetido que la base de datos exige unico (documento, email, tarjeta...)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> datoDuplicado(DataIntegrityViolationException ex) {
        return responder(HttpStatus.CONFLICT,
                "Ya existe un registro con esos datos (documento, email o tarjeta repetidos)");
    }

    private ResponseEntity<ErrorResponse> responder(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado)
                .body(new ErrorResponse(estado.value(), mensaje, LocalDateTime.now()));
    }
}
