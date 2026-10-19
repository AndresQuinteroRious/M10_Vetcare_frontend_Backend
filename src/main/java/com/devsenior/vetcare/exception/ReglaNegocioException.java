package com.devsenior.vetcare.exception;

// Se lanza cuando la peticion es valida pero el negocio no la permite
// (ej: atender una cita cancelada, borrar un dueño con mascotas). Responde 409.
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
