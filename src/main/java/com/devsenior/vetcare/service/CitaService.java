package com.devsenior.vetcare.service;

import com.devsenior.vetcare.dto.AtencionRequest;
import com.devsenior.vetcare.dto.CitaRequest;
import com.devsenior.vetcare.dto.CitaResponse;

import java.util.List;

public interface CitaService {
    List<CitaResponse> listarTodos();
    List<CitaResponse> listarMias(String username);
    List<CitaResponse> listarPorMascota(Long mascotaId);
    CitaResponse buscarPorId(Long id);
    CitaResponse crear(CitaRequest request);
    CitaResponse actualizar(Long id, CitaRequest request);
    CitaResponse atender(Long id, AtencionRequest request, String username);
    CitaResponse cancelar(Long id);
    void eliminar(Long id);
}
