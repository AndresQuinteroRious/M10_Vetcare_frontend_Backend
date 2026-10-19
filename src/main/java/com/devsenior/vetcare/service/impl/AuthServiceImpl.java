package com.devsenior.vetcare.service.impl;

import com.devsenior.vetcare.dto.AuthResponse;
import com.devsenior.vetcare.dto.LoginRequest;
import com.devsenior.vetcare.model.Usuario;
import com.devsenior.vetcare.repository.UsuarioRepository;
import com.devsenior.vetcare.service.AuthService;
import com.devsenior.vetcare.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UsuarioRepository usuarioRepository,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        // 1. Verifica username + password (lanza BadCredentialsException -> 401)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        // 2. Si paso, busca el usuario para leer su rol y su nombre
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException("Usuario no encontrado"));

        // 3. Genera el token: sub = username, claims = rol y nombre
        String token = jwtService.generarToken(usuario);
        return new AuthResponse(token);
    }
}
