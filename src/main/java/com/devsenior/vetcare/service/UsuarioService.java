package com.devsenior.vetcare.service;

import com.devsenior.vetcare.dto.UsuarioRequest;
import com.devsenior.vetcare.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponse> listarTodos();
    UsuarioResponse crear(UsuarioRequest request);
}
