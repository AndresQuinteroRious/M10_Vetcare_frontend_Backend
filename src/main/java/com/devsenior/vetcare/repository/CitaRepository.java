package com.devsenior.vetcare.repository;

import com.devsenior.vetcare.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findAllByOrderByFechaAscHoraAsc();

    // "Mis citas": las del veterinario cuya cuenta tiene este username
    List<Cita> findByVeterinarioUsuarioUsernameOrderByFechaAscHoraAsc(String username);

    // Historia clinica: de la mas reciente a la mas antigua
    List<Cita> findByMascotaIdOrderByFechaDescHoraDesc(Long mascotaId);

    boolean existsByMascotaId(Long mascotaId);

    boolean existsByVeterinarioId(Long veterinarioId);
}
