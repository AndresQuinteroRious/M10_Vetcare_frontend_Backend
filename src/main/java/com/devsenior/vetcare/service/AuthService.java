package com.devsenior.vetcare.service;

import com.devsenior.vetcare.dto.AuthResponse;
import com.devsenior.vetcare.dto.LoginRequest;

// El registro publico se elimino: las cuentas las crea el ADMIN (UsuarioService)
public interface AuthService {
    AuthResponse login(LoginRequest request);
}
