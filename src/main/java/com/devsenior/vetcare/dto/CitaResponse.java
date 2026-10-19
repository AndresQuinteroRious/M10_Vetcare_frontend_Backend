package com.devsenior.vetcare.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record CitaResponse(
        Long id,
        LocalDate fecha,
        LocalTime hora,
        String motivo,
        String estado,
        Double costo,
        String notas,
        Long mascotaId,
        String mascotaNombre,
        String mascotaEspecie,
        String duenoNombre,
        Long veterinarioId,
        String veterinarioNombre
) {}
