package com.devsenior.vetcare.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

// Ajusta una base de datos que viene de la version anterior de VetCare.
// ddl-auto=update agrega columnas nuevas, pero NO modifica restricciones
// ni datos viejos. En un proyecto real esto lo haria Flyway o Liquibase.
@Component
@Order(1)
public class MigracionEsquema implements CommandLineRunner {

    private final JdbcTemplate jdbc;

    public MigracionEsquema(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        actualizarRestriccionDeRoles();

        // Las citas viejas tenian estado como texto libre y no tenian hora
        jdbc.update("UPDATE citas SET estado = 'PROGRAMADA' WHERE estado IS NULL "
                + "OR estado NOT IN ('PROGRAMADA', 'ATENDIDA', 'CANCELADA')");
        jdbc.update("UPDATE citas SET hora = '09:00' WHERE hora IS NULL");
    }

    // Hibernate creo un CHECK que solo acepta 'ADMIN' y 'USER'.
    // Si no lo cambiamos, guardar un VETERINARIO falla en PostgreSQL.
    private void actualizarRestriccionDeRoles() {
        List<String> definicion = jdbc.queryForList(
                "SELECT pg_get_constraintdef(oid) FROM pg_constraint WHERE conname = 'usuarios_rol_check'",
                String.class);

        boolean yaEstaActualizada = !definicion.isEmpty() && definicion.get(0).contains("VETERINARIO");
        if (yaEstaActualizada) {
            return;
        }

        jdbc.execute("ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_rol_check");
        jdbc.update("UPDATE usuarios SET rol = 'RECEPCIONISTA' WHERE rol = 'USER'");
        jdbc.execute("ALTER TABLE usuarios ADD CONSTRAINT usuarios_rol_check "
                + "CHECK (rol IN ('ADMIN', 'RECEPCIONISTA', 'VETERINARIO'))");
    }
}
