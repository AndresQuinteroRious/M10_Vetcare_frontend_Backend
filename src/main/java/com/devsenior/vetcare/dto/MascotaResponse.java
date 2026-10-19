package com.devsenior.vetcare.dto;

public record MascotaResponse(
        Long id,
        String nombre,
        String especie,
        String raza,
        Integer edad,
        Long duenoId,
        String dueno,
        String foto
) {}
