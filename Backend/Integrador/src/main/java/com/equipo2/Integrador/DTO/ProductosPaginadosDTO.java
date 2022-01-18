package com.equipo2.Integrador.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Schema(name = "ProductosPaginados")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductosPaginadosDTO {

    private Integer resultadosTotales;
    private final Integer resultadosPorPagina = 6;
    private List<ProductoDTO> productos = new ArrayList<>();


    @Override
    public String toString() {
        return "ProductosPaginadosDTO{" +
                "resultadosTotales=" + resultadosTotales +
                ", resultadosPorPagina=" + resultadosPorPagina +
                ", productos=" + productos +
                '}';
    }
}
