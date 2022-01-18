package com.equipo2.Integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="IMAGENES")
public class Imagen {

    @Id
    //@SequenceGenerator(name = "imagen_sequence_generator", sequenceName = "imagen_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imagen_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 127)
    private String titulo;

    @Column(length = 255)
    private String url;

    @ManyToOne(fetch = FetchType.EAGER)   // Es el default
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    @JsonIgnoreProperties({ "descripcion", "ciudad", "categoria", "imagenes", "caracteristicas",
                            "latitud", "longitud", "direccion", "politica", "puntuaciones" })
    private Producto producto;

    public Imagen() {
    }

    public Imagen(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
        this.producto = null;
    }

    public Imagen(String titulo, String url, Producto producto) {
        this.titulo = titulo;
        this.url = url;
        this.producto = producto;
    }

    public Imagen(Long id, String titulo, String url, Producto producto) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "Imagen{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    // Compara ID's. Devuelve false si cualquiera de los dos ID's es nulo
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Imagen imagen = (Imagen) o;
        if (id == null) return false;
        return id.equals(imagen.id);
    }
}
