package com.devsenior.vetcare.controller;

import com.devsenior.vetcare.dto.AuthResponse;
import com.devsenior.vetcare.dto.LoginRequest;
import com.devsenior.vetcare.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Solo login es publico. El registro publico se elimino:
// cualquiera podia crearse ADMIN mandando "rol":"ADMIN" desde Postman.
// Las cuentas ahora las crea el ADMIN en /api/usuarios.
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
