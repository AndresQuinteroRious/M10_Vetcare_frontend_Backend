package com.devsenior.vetcare.repository;

import com.devsenior.vetcare.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByTarjetaProfesional(String tarjetaProfesional);
}
