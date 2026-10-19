package com.devsenior.vetcare.service;

import com.devsenior.vetcare.dto.DuenoRequest;
import com.devsenior.vetcare.dto.DuenoResponse;
import java.util.List;

//public interface DuenoService {
//    List<Dueno> listarTodos();
//}

public interface DuenoService {
    List<DuenoResponse> listarTodos();
    DuenoResponse buscarPorId(Long id);
    DuenoResponse crear(DuenoRequest request);
    DuenoResponse actualizar(Long id, DuenoRequest request);
    void eliminar(Long id);
}
