package com.devsenior.vetcare.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        int codigo,
        String mensaje,
        LocalDateTime fecha
) {}
