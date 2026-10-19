package com.devsenior.vetcare.controller;

import com.devsenior.vetcare.dto.AtencionRequest;
import com.devsenior.vetcare.dto.CitaRequest;
import com.devsenior.vetcare.dto.CitaResponse;
import com.devsenior.vetcare.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    // Agenda completa: administracion y recepcion
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @GetMapping
    public ResponseEntity<List<CitaResponse>> listarTodos() {
        return ResponseEntity.ok(citaService.listarTodos());
    }

    // "Mi agenda": el username sale del token, no de la URL
    @PreAuthorize("hasRole('VETERINARIO')")
    @GetMapping("/mias")
    public ResponseEntity<List<CitaResponse>> listarMias(Authentication autenticacion) {
        return ResponseEntity.ok(citaService.listarMias(autenticacion.getName()));
    }

    // Historia clinica de una mascota: cualquier usuario autenticado
    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<List<CitaResponse>> listarPorMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(citaService.listarPorMascota(mascotaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @PostMapping
    public ResponseEntity<CitaResponse> crear(@Valid @RequestBody CitaRequest request) {
        CitaResponse creada = citaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @PutMapping("/{id}")
    public ResponseEntity<CitaResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaRequest request) {
        return ResponseEntity.ok(citaService.actualizar(id, request));
    }

    @PreAuthorize("hasRole('VETERINARIO')")
    @PatchMapping("/{id}/atender")
    public ResponseEntity<CitaResponse> atender(
            @PathVariable Long id,
            @Valid @RequestBody AtencionRequest request,
            Authentication autenticacion) {
        return ResponseEntity.ok(citaService.atender(id, request, autenticacion.getName()));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponse> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelar(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
