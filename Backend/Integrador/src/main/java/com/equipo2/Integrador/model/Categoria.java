package com.equipo2.Integrador.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Builder
@Entity
@Table(name="CATEGORIAS")
public class Categoria {

    @Id
    //@SequenceGenerator(name = "categoria_sequence_generator", sequenceName = "categoria_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 127)
    private String titulo;

    @Column(length = 255)
    private String descripcion;

    @Column(name = "url_imagen", length = 255)
    private String urlImagen;

    public Categoria() {
    }

    public Categoria(String titulo, String descripcion, String urlImagen) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public Categoria(Long id, String titulo, String descripcion, String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                '}';
    }

    // Compara ID's. Devuelve false si cualquiera de los dos ID's es nulo
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        if (id == null) return false;
        return id.equals(categoria.id);
    }
}
