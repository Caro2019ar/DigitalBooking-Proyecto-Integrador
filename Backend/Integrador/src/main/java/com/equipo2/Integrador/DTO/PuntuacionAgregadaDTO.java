package com.equipo2.Integrador.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(name = "PuntuacionAgregada")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PuntuacionAgregadaDTO {

    private Long puntajeTotal;
    private Integer cantidadVotos;


    @Override
    public String toString() {
        return "PuntuacionAgregadaDTO{" +
                "puntajeTotal=" + puntajeTotal +
                ", cantidadVotos=" + cantidadVotos +
                '}';
    }
}
