package com.equipo2.Integrador.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CARACTERISTICAS")
public class Caracteristica {

    @Id
    //@SequenceGenerator(name = "caracteristica_sequence_generator", sequenceName = "caracteristica_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caracteristica_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 127)
    private String nombre;

    @Column(length = 127)
    private String icono;

    public Caracteristica() {

    }

    public Caracteristica(String nombre, String icono) {
        this.nombre = nombre;
        this.icono = icono;
    }

    public Caracteristica(Long id, String nombre, String icono) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    @Override
    public String toString() {
        return "Caracteristica{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", icono='" + icono + '\'' +
                '}';
    }

    // Compara ID's. Devuelve false si cualquiera de los dos ID's es nulo
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caracteristica caracteristica = (Caracteristica) o;
        if (id == null) return false;
        return id.equals(caracteristica.id);
    }
}
