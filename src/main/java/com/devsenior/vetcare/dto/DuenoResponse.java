package com.devsenior.vetcare.dto;

public record DuenoResponse(
        Long id,
        String nombre,
        String apellido,
        String documento,
        String telefono,
        String email
) {}
