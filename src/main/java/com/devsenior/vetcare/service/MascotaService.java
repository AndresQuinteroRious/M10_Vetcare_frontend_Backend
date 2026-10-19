package com.devsenior.vetcare.service;

import com.devsenior.vetcare.dto.MascotaRequest;
import com.devsenior.vetcare.dto.MascotaResponse;
import java.util.List;

public interface MascotaService {
    List<MascotaResponse> listarTodos();
    MascotaResponse buscarPorId(Long id);
    MascotaResponse crear(MascotaRequest request);
    MascotaResponse actualizar(Long id, MascotaRequest request);
    void eliminar(Long id);
}
