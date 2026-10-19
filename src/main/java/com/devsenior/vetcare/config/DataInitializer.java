package com.devsenior.vetcare.config;

import com.devsenior.vetcare.model.*;
import com.devsenior.vetcare.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Datos de prueba para que TODOS arranquen con la misma clinica.
// Cada bloque solo siembra lo que falta: no duplica ni pisa datos existentes.
// Usuarios sembrados (clave de todos: vetcare123):
//   admin       -> ADMIN          (Carolina Rojas)
//   recepcion   -> RECEPCIONISTA  (Juliana Pérez)
//   dra.gomez   -> VETERINARIO    (Laura Gómez)
//   dr.ruiz     -> VETERINARIO    (Andrés Ruiz)
@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private static final String CLAVE_DEMO = "vetcare123";

    private final DuenoRepository duenoRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final CitaRepository citaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(DuenoRepository duenoRepository,
                           MascotaRepository mascotaRepository,
                           UsuarioRepository usuarioRepository,
                           VeterinarioRepository veterinarioRepository,
                           CitaRepository citaRepository,
                           PasswordEncoder passwordEncoder) {
        this.duenoRepository = duenoRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.citaRepository = citaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        sembrarDuenosYMascotas();
        Usuario cuentaGomez = sembrarUsuario("dra.gomez", "Laura Gómez", Rol.VETERINARIO);
        Usuario cuentaRuiz = sembrarUsuario("dr.ruiz", "Andrés Ruiz", Rol.VETERINARIO);
        sembrarUsuario("admin", "Carolina Rojas", Rol.ADMIN);
        sembrarUsuario("recepcion", "Juliana Pérez", Rol.RECEPCIONISTA);

        Veterinario gomez = sembrarVeterinario("Laura Gómez", "Medicina general", "TP-1001", cuentaGomez);
        Veterinario ruiz = sembrarVeterinario("Andrés Ruiz", "Cirugía", "TP-1002", cuentaRuiz);
        Veterinario mejia = sembrarVeterinario("Paula Mejía", "Dermatología", "TP-1003", null);

        List<Mascota> mascotas = mascotaRepository.findAll();
        if (mascotas.isEmpty()) {
            return;
        }
        List<Cita> citas = citaRepository.findAll();
        boolean hayHistoria = citas.stream()
                .anyMatch(c -> c.getEstado() == EstadoCita.ATENDIDA);
        if (!hayHistoria) {
            sembrarHistoria(mascotas, gomez, ruiz, mejia);
        }
        // Agenda de hoy: si ningun veterinario con cuenta tiene citas hoy, se siembra
        boolean hayAgendaHoy = citas.stream()
                .anyMatch(c -> LocalDate.now().equals(c.getFecha())
                        && c.getVeterinario() != null
                        && c.getVeterinario().getUsuario() != null);
        if (!hayAgendaHoy) {
            sembrarAgendaDeHoy(mascotas, gomez, ruiz, mejia);
        }
    }

    private void sembrarDuenosYMascotas() {
        if (mascotaRepository.count() > 0) {
            return;
        }
        Dueno ana = dueno("Ana", "Torres", "1000000001", "3001234567", "ana.torres@example.com");
        Dueno luis = dueno("Luis", "Pérez", "1000000002", "3007654321", "luis.perez@example.com");
        Dueno carlos = dueno("Carlos", "Rincón", "1000000003", "3105550101", "carlos.rincon@example.com");
        Dueno marta = dueno("Marta", "Díaz", "1000000004", "3205550202", "marta.diaz@example.com");

        mascota("Firulais", "Perro", "Criollo", 4, ana);
        mascota("Michi", "Gato", "Siamés", 2, luis);
        mascota("Rocky", "Perro", "Labrador", 6, ana);
        mascota("Luna", "Gato", "Persa", 3, carlos);
        mascota("Tambor", "Conejo", "Cabeza de león", 1, marta);
        mascota("Max", "Perro", "Golden retriever", 8, carlos);
    }

    private Usuario sembrarUsuario(String username, String nombre, Rol rol) {
        return usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(CLAVE_DEMO));
            usuario.setNombre(nombre);
            usuario.setRol(rol);
            return usuarioRepository.save(usuario);
        });
    }

    private Veterinario sembrarVeterinario(String nombre, String especialidad,
                                           String tarjeta, Usuario cuenta) {
        Veterinario vet = veterinarioRepository.findByTarjetaProfesional(tarjeta).orElseGet(() -> {
            Veterinario nuevo = new Veterinario();
            nuevo.setNombre(nombre);
            nuevo.setEspecialidad(especialidad);
            nuevo.setTarjetaProfesional(tarjeta);
            return nuevo;
        });
        if (vet.getUsuario() == null && cuenta != null) {
            vet.setUsuario(cuenta);
        }
        return veterinarioRepository.save(vet);
    }

    // Citas de dias anteriores: le dan historia clinica a las mascotas
    private void sembrarHistoria(List<Mascota> m, Veterinario gomez, Veterinario ruiz, Veterinario mejia) {
        LocalDate hoy = LocalDate.now();
        atendida(cita(hoy.minusDays(20), "10:00", "Vacunación anual", m.get(0), gomez),
                "Vacuna polivalente aplicada. Sin reacciones. Próximo refuerzo en un año.", 85000.0);
        atendida(cita(hoy.minusDays(12), "15:00", "Control de peso", m.get(1 % m.size()), gomez),
                "Sobrepeso leve. Se recomienda dieta light y más juego.", 60000.0);
        atendida(cita(hoy.minusDays(5), "09:30", "Cojera pata trasera", m.get(2 % m.size()), ruiz),
                "Esguince leve. Reposo 10 días y antiinflamatorio.", 120000.0);
        atendida(cita(hoy.minusDays(1), "11:00", "Picazón en la piel", m.get(3 % m.size()), mejia),
                "Dermatitis alérgica. Champú medicado dos veces por semana.", 95000.0);
        Cita cancelada = cita(hoy.minusDays(1), "16:00", "Baño medicado", m.get(4 % m.size()), mejia);
        cancelada.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cancelada);
        cita(hoy.plusDays(1), "08:30", "Control postoperatorio", m.get(2 % m.size()), ruiz);
    }

    // La agenda del dia: lo primero que ve recepcion y cada veterinario
    private void sembrarAgendaDeHoy(List<Mascota> m, Veterinario gomez, Veterinario ruiz, Veterinario mejia) {
        LocalDate hoy = LocalDate.now();
        cita(hoy, "08:30", "Consulta general", m.get(0), gomez);
        cita(hoy, "09:30", "Vacunación", m.get(1 % m.size()), gomez);
        cita(hoy, "10:00", "Revisión de herida", m.get(5 % m.size()), ruiz);
        cita(hoy, "11:30", "Desparasitación", m.get(4 % m.size()), gomez);
        cita(hoy, "14:00", "Limpieza dental", m.get(3 % m.size()), ruiz);
        cita(hoy, "15:30", "Control de piel", m.get(2 % m.size()), mejia);
    }

    // --- Ayudantes ---

    private Dueno dueno(String nombre, String apellido, String documento, String telefono, String email) {
        Dueno d = new Dueno();
        d.setNombre(nombre);
        d.setApellido(apellido);
        d.setDocumento(documento);
        d.setTelefono(telefono);
        d.setEmail(email);
        return duenoRepository.save(d);
    }

    private void mascota(String nombre, String especie, String raza, int edad, Dueno dueno) {
        Mascota m = new Mascota();
        m.setNombre(nombre);
        m.setEspecie(especie);
        m.setRaza(raza);
        m.setEdad(edad);
        m.setDueno(dueno);
        mascotaRepository.save(m);
    }

    private Cita cita(LocalDate fecha, String hora, String motivo, Mascota mascota, Veterinario vet) {
        Cita c = new Cita();
        c.setFecha(fecha);
        c.setHora(LocalTime.parse(hora));
        c.setMotivo(motivo);
        c.setEstado(EstadoCita.PROGRAMADA);
        c.setMascota(mascota);
        c.setVeterinario(vet);
        return citaRepository.save(c);
    }

    private void atendida(Cita c, String notas, Double costo) {
        c.setNotas(notas);
        c.setCosto(costo);
        c.setEstado(EstadoCita.ATENDIDA);
        citaRepository.save(c);
    }
}
