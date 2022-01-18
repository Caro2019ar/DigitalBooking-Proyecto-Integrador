package com.equipo2.Integrador.DTO;


import com.equipo2.Integrador.model.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Schema(name = "Producto")
@Getter
@Setter
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;

    @Schema(hidden = true)
    private Ciudad ciudad;

    @Schema(hidden = true)
    private Categoria categoria;

    @Schema(hidden = true)
    @JsonIgnoreProperties(value = "producto")
    private List<Imagen> imagenes = new ArrayList<>();

    @Schema(hidden = true)
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    private Double latitud;
    private Double longitud;
    private String direccion;

    @Schema(hidden = true)
    private Politica politica;

    //@JsonIgnoreProperties(value = "producto")
    //private List<Puntuacion> puntuaciones;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PuntuacionAgregadaDTO valoracion = new PuntuacionAgregadaDTO(0L, 0);


    public ProductoDTO() {
    }

    public ProductoDTO(String nombre, String descripcion, Ciudad ciudad, Categoria categoria, List<Imagen> imagenes,
                       Set<Caracteristica> caracteristicas, Double latitud, Double longitud, String direccion,
                       Politica politica)
    {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.categoria = categoria;
        if (imagenes != null) this.imagenes = imagenes;
        if (caracteristicas != null) this.caracteristicas = caracteristicas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.politica = politica;
    }

    public ProductoDTO(Long id, String nombre, String descripcion, Ciudad ciudad, Categoria categoria, List<Imagen> imagenes,
                       Set<Caracteristica> caracteristicas, Double latitud, Double longitud, String direccion,
                       Politica politica)
    {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.categoria = categoria;
        if (imagenes != null) this.imagenes = imagenes;
        if (caracteristicas != null) this.caracteristicas = caracteristicas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.politica = politica;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ciudad=" + ciudad +
                ", categoria=" + categoria +
                ", imagenes=" + imagenes +
                ", caracteristicas=" + caracteristicas +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", direccion='" + direccion + '\'' +
                ", politica=" + politica +
                ", valoracion=" + valoracion +
                '}';
    }
}