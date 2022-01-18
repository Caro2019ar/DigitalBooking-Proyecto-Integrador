package com.equipo2.Integrador.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(name = "Rol")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RolDTO {

    private Long id;
    private String nombre;

    public RolDTO(String nombre)
    {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "RolDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
