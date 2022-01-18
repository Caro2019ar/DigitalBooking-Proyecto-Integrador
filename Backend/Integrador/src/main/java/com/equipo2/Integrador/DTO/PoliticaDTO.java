package com.equipo2.Integrador.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(name = "Politica")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PoliticaDTO {

    private Long id;
    private String normas;
    private String saludYSeguridad;
    private String cancelacion;


    public PoliticaDTO(String normas, String saludYSeguridad, String cancelacion)
    {
        this.normas = normas;
        this.saludYSeguridad = saludYSeguridad;
        this.cancelacion = cancelacion;
    }

    @Override
    public String toString() {
        return "PoliticaDTO{" +
                "id=" + id +
                ", normas='" + normas + '\'' +
                ", saludYSeguridad='" + saludYSeguridad + '\'' +
                ", cancelacion='" + cancelacion + '\'' +
                '}';
    }
}
