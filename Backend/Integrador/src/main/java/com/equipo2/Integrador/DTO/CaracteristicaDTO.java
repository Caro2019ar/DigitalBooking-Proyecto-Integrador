package com.equipo2.Integrador.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(name = "Caracteristica")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CaracteristicaDTO {

    private Long id;
    private String nombre;
    private String icono;

    public CaracteristicaDTO(String nombre, String icono)
    {
        this.nombre = nombre;
        this.icono = icono;
    }

    @Override
    public String toString() {
        return "CaracteristicaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", icono='" + icono + '\'' +
                '}';
    }
}
