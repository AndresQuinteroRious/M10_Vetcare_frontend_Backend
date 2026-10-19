package com.devsenior.vetcare.dto;

public record VeterinarioResponse(
        Long id,
        String nombre,
        String especialidad,
        String tarjetaProfesional
) {}
