package com.devsenior.vetcare.repository;

import com.devsenior.vetcare.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    boolean existsByDuenoId(Long duenoId);
}
