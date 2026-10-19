package com.devsenior.vetcare.service.impl;

import com.devsenior.vetcare.dto.DuenoRequest;
import com.devsenior.vetcare.dto.DuenoResponse;
import com.devsenior.vetcare.exception.RecursoNoEncontradoException;
import com.devsenior.vetcare.exception.ReglaNegocioException;
import com.devsenior.vetcare.model.Dueno;
import com.devsenior.vetcare.repository.DuenoRepository;
import com.devsenior.vetcare.repository.MascotaRepository;
import com.devsenior.vetcare.service.DuenoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DuenoServiceImpl implements DuenoService {

    private final DuenoRepository duenoRepository;
    private final MascotaRepository mascotaRepository;

    public DuenoServiceImpl(DuenoRepository duenoRepository,
                            MascotaRepository mascotaRepository) {
        this.duenoRepository = duenoRepository;
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public List<DuenoResponse> listarTodos() {
        return duenoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DuenoResponse buscarPorId(Long id) {
        Dueno dueno = duenoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un dueño con id " + id));
        return toResponse(dueno);
    }

    @Override
    public DuenoResponse crear(DuenoRequest request) {
        Dueno dueno = toEntity(request);
        Dueno guardado = duenoRepository.save(dueno);
        return toResponse(guardado);
    }

    @Override
    public DuenoResponse actualizar(Long id, DuenoRequest request) {
        Dueno dueno = duenoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un dueño con id " + id));

        dueno.setNombre(request.nombre());
        dueno.setApellido(request.apellido());
        dueno.setDocumento(request.documento());
        dueno.setTelefono(request.telefono());
        dueno.setEmail(request.email());

        Dueno actualizado = duenoRepository.save(dueno);
        return toResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!duenoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe un dueño con id " + id);
        }
        // Borrar el dueño borraria en cascada sus mascotas y su historia clinica
        if (mascotaRepository.existsByDuenoId(id)) {
            throw new ReglaNegocioException("No se puede eliminar: el dueño tiene mascotas registradas");
        }
        duenoRepository.deleteById(id);
    }

    // --- Conversión DTO <-> entidad (privados, solo los usa este servicio) ---

    private Dueno toEntity(DuenoRequest request) {
        Dueno dueno = new Dueno();
        dueno.setNombre(request.nombre());
        dueno.setApellido(request.apellido());
        dueno.setDocumento(request.documento());
        dueno.setTelefono(request.telefono());
        dueno.setEmail(request.email());
        return dueno;
    }

    private DuenoResponse toResponse(Dueno dueno) {
        return new DuenoResponse(
                dueno.getId(),
                dueno.getNombre(),
                dueno.getApellido(),
                dueno.getDocumento(),
                dueno.getTelefono(),
                dueno.getEmail()
        );
    }
}
