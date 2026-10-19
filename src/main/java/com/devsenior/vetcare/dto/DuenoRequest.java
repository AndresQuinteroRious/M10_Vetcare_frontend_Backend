package com.devsenior.vetcare.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DuenoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El apellido es obligatorio")
        String apellido,

        @NotBlank(message = "El documento es obligatorio")
        String documento,

        String telefono,

        @Email(message = "El email debe ser válido")
        String email
) {}
