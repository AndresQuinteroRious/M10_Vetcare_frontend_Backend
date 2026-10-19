package com.devsenior.vetcare.service.impl;

import com.devsenior.vetcare.dto.VeterinarioRequest;
import com.devsenior.vetcare.dto.VeterinarioResponse;
import com.devsenior.vetcare.exception.RecursoNoEncontradoException;
import com.devsenior.vetcare.exception.ReglaNegocioException;
import com.devsenior.vetcare.model.Veterinario;
import com.devsenior.vetcare.repository.CitaRepository;
import com.devsenior.vetcare.repository.VeterinarioRepository;
import com.devsenior.vetcare.service.VeterinarioService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final CitaRepository citaRepository;

    public VeterinarioServiceImpl(VeterinarioRepository veterinarioRepository,
                                  CitaRepository citaRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    public List<VeterinarioResponse> listarTodos() {
        return veterinarioRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public VeterinarioResponse buscarPorId(Long id) {
        Veterinario vet = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un veterinario con id " + id));
        return toResponse(vet);
    }

    @Override
    public VeterinarioResponse crear(VeterinarioRequest request) {
        Veterinario vet = new Veterinario();
        vet.setNombre(request.nombre());
        vet.setEspecialidad(request.especialidad());
        vet.setTarjetaProfesional(request.tarjetaProfesional());

        Veterinario guardado = veterinarioRepository.save(vet);
        return toResponse(guardado);
    }

    @Override
    public VeterinarioResponse actualizar(Long id, VeterinarioRequest request) {
        Veterinario vet = veterinarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un veterinario con id " + id));

        vet.setNombre(request.nombre());
        vet.setEspecialidad(request.especialidad());
        vet.setTarjetaProfesional(request.tarjetaProfesional());

        Veterinario actualizado = veterinarioRepository.save(vet);
        return toResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!veterinarioRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("No existe un veterinario con id " + id);
        }
        if (citaRepository.existsByVeterinarioId(id)) {
            throw new ReglaNegocioException("No se puede eliminar: el veterinario tiene citas registradas");
        }
        veterinarioRepository.deleteById(id);
    }

    private VeterinarioResponse toResponse(Veterinario vet) {
        return new VeterinarioResponse(
                vet.getId(), vet.getNombre(), vet.getEspecialidad(), vet.getTarjetaProfesional()
        );
    }
}
