package com.devsenior.vetcare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

// Lo que registra el veterinario al atender una cita
public record AtencionRequest(
        @NotBlank(message = "Las notas de la atencion son obligatorias")
        String notas,

        @NotNull(message = "El costo es obligatorio")
        @PositiveOrZero(message = "El costo no puede ser negativo")
        Double costo
) {}
