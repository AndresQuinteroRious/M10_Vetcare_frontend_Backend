package com.devsenior.vetcare.dto;

public record UsuarioResponse(
        Long id,
        String username,
        String nombre,
        String rol,
        Long veterinarioId
) {}
