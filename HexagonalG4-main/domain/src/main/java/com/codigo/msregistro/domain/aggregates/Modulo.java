package com.codigo.msregistro.domain.aggregates;


import com.fasterxml.jackson.annotation.JsonBackReference;  // Import necesario para evitar la recursión
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;  // Importar solo jakarta.persistence
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modulos")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;

    @NotNull
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EstadoModulo estado;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;


    private String prioridad;

    @ManyToMany
    @JoinTable(
            name = "modulo_usuarios",
            joinColumns = @JoinColumn(name = "modulo_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuarios = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id")
    @JsonBackReference  // Evitar la recursión infinita al serializar
    private Proyecto proyecto;

    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // Evitar la recursión al serializar
    private List<Tarea> tareas;  // Agregar este campo

    // Otros atributos y métodos
}
