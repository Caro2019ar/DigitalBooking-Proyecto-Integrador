package com.equipo2.Integrador.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Schema(name = "Ciudad")
@Getter
@Setter
public class CiudadDTO {

    private Long id;
    private String nombre;
    private String pais;

    public CiudadDTO() {
    }

    public CiudadDTO(String nombre, String pais) {
        this.nombre = nombre;
        this.pais = pais;
    }

    public CiudadDTO(Long id, String nombre, String pais) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
    }

    @Override
    public String toString() {
        return "CiudadDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}
