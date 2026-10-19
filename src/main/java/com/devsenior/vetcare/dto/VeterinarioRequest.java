package com.devsenior.vetcare.dto;

import jakarta.validation.constraints.NotBlank;

public record VeterinarioRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String especialidad,

        @NotBlank(message = "La tarjeta profesional es obligatoria")
        String tarjetaProfesional
) {}
