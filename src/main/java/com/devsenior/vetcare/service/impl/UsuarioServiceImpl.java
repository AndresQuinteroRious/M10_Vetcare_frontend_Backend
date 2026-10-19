package com.devsenior.vetcare.service.impl;

import com.devsenior.vetcare.dto.UsuarioRequest;
import com.devsenior.vetcare.dto.UsuarioResponse;
import com.devsenior.vetcare.exception.RecursoNoEncontradoException;
import com.devsenior.vetcare.exception.ReglaNegocioException;
import com.devsenior.vetcare.model.Rol;
import com.devsenior.vetcare.model.Usuario;
import com.devsenior.vetcare.model.Veterinario;
import com.devsenior.vetcare.repository.UsuarioRepository;
import com.devsenior.vetcare.repository.VeterinarioRepository;
import com.devsenior.vetcare.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              VeterinarioRepository veterinarioRepository,
                              PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        List<Veterinario> veterinarios = veterinarioRepository.findAll();
        return usuarioRepository.findAll().stream()
                .map(u -> toResponse(u, veterinarios))
                .toList();
    }

    @Override
    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.findByUsername(request.username()).isPresent()) {
            throw new ReglaNegocioException("El username " + request.username() + " ya esta registrado");
        }

        Veterinario veterinario = null;
        if (request.rol() == Rol.VETERINARIO) {
            if (request.veterinarioId() == null) {
                throw new ReglaNegocioException("Una cuenta de veterinario debe indicar veterinarioId");
            }
            veterinario = veterinarioRepository.findById(request.veterinarioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe un veterinario con id " + request.veterinarioId()));
            if (veterinario.getUsuario() != null) {
                throw new ReglaNegocioException("Ese veterinario ya tiene una cuenta");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setNombre(request.nombre());
        usuario.setRol(request.rol());
        usuarioRepository.save(usuario);

        if (veterinario != null) {
            veterinario.setUsuario(usuario);
            veterinarioRepository.save(veterinario);
        }

        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), usuario.getNombre(),
                usuario.getRol().name(), veterinario == null ? null : veterinario.getId());
    }

    private UsuarioResponse toResponse(Usuario usuario, List<Veterinario> veterinarios) {
        Long veterinarioId = veterinarios.stream()
                .filter(v -> v.getUsuario() != null && v.getUsuario().getId().equals(usuario.getId()))
                .map(Veterinario::getId)
                .findFirst()
                .orElse(null);
        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), usuario.getNombre(),
                usuario.getRol().name(), veterinarioId);
    }
}
