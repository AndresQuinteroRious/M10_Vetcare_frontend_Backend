package com.devsenior.vetcare.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MascotaRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "La especie es obligatoria")
        String especie,

        String raza,
        Integer edad,

        @NotNull(message = "El id del dueño es obligatorio")
        Long duenoId
) {}
