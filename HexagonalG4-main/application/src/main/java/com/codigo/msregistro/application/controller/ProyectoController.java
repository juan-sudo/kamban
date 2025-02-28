package com.codigo.msregistro.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.codigo.msregistro.application.services.ProyectoService;
import com.codigo.msregistro.domain.aggregates.Proyecto;

@RestController
@RequestMapping("/api/proyectos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProyectoController {

    private final ProyectoService proyectoService;

    // Inyección de dependencias a través del constructor
    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    // Obtener todos los proyectos (no eliminados)
    @GetMapping
    public ResponseEntity<List<Proyecto>> getProyectos() {
        List<Proyecto> proyectos = proyectoService.getAllProyectos();
        if (proyectos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Retorna 204 si no hay proyectos
        }
        return new ResponseEntity<>(proyectos, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Proyecto>> getProyectosSin() {
        List<Proyecto> proyectos = proyectoService.getAllProyectos();

        // Filtrar proyectos que tienen al menos un módulo
        List<Proyecto> proyectosConModulos = proyectos.stream()
                .filter(proyecto -> proyecto.getModulos() != null && !proyecto.getModulos().isEmpty())
                .collect(Collectors.toList());

        if (proyectosConModulos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);  // Retorna 204 si no hay proyectos con módulos
        }
        return new ResponseEntity<>(proyectosConModulos, HttpStatus.OK);
    }


    // Obtener todos los proyectos que están en la papelera
    @GetMapping("/papelera")
    public ResponseEntity<List<Proyecto>> getProyectosEnPapelera() {
        List<Proyecto> proyectosEnPapelera = proyectoService.getProyectosEnPapelera();
        if (proyectosEnPapelera.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(proyectosEnPapelera, HttpStatus.OK);
    }

    // Obtener un proyecto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Proyecto> getProyectoById(@PathVariable Long id) {
        Optional<Proyecto> proyecto = proyectoService.getProyectoById(id);
        return proyecto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Crear un nuevo proyecto
    @PostMapping
    public ResponseEntity<Proyecto> createProyecto(@RequestBody Proyecto proyecto) {
        Proyecto nuevoProyecto = proyectoService.createProyecto(proyecto);
        return new ResponseEntity<>(nuevoProyecto, HttpStatus.CREATED);
    }

    // Mover un proyecto a la papelera (eliminación lógica)
    @PatchMapping("/{id}/eliminar")
    public ResponseEntity<Void> eliminarProyecto(@PathVariable Long id) {
        boolean eliminado = proyectoService.eliminarProyecto(id);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Restaurar un proyecto desde la papelera
    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurarProyecto(@PathVariable Long id) {
        boolean restaurado = proyectoService.restaurarProyecto(id);
        if (restaurado) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
