package com.devsenior.vetcare.dto;

import com.devsenior.vetcare.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Solo el ADMIN crea cuentas. Si el rol es VETERINARIO,
// veterinarioId dice a que veterinario pertenece esta cuenta.
public record UsuarioRequest(
        @NotBlank(message = "El username es obligatorio")
        String username,

        @NotBlank(message = "El password es obligatorio")
        @Size(min = 6, message = "El password debe tener al menos 6 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotNull(message = "El rol es obligatorio")
        Rol rol,

        Long veterinarioId
) {}
