package com.devsenior.vetcare.service;

import com.devsenior.vetcare.dto.VeterinarioRequest;
import com.devsenior.vetcare.dto.VeterinarioResponse;
import java.util.List;

public interface VeterinarioService {
    List<VeterinarioResponse> listarTodos();
    VeterinarioResponse buscarPorId(Long id);
    VeterinarioResponse crear(VeterinarioRequest request);
    VeterinarioResponse actualizar(Long id, VeterinarioRequest request);
    void eliminar(Long id);
}
