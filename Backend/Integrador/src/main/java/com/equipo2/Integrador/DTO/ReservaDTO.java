package com.equipo2.Integrador.DTO;

import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Producto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalTime;


@Schema(name = "Reserva")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservaDTO {

    private Long id;

    @Schema(hidden = true)
    private LocalTime horaInicio;

    @Schema(hidden = true)
    private LocalDate fechaInicial;

    @Schema(hidden = true)
    private LocalDate fechaFinal;

    @Schema(hidden = true)
    //@JsonIgnoreProperties("reservas")    // Sólo es necesario si la relación es bidireccional
    @JsonIgnoreProperties({ "descripcion", "caracteristicas", "latitud", "longitud", "politica", "puntuaciones" })
    private Producto producto;

    @Schema(hidden = true)
    //@JsonIgnoreProperties("reservas")    // Sólo es necesario si la relación es bidireccional
    @JsonIgnoreProperties({ "contrasena", "rol", "habilitado", "productosFavoritos", "enabled", "password", "authorities",
                            "username", "credentialsNonExpired", "accountNonExpired", "accountNonLocked" })
    private Cliente cliente;

    private String ciudadOrigen;
    private String infoCovid;
    private Boolean vacunadoCovid;

    public ReservaDTO(LocalTime horaInicio, LocalDate fechaInicial, LocalDate fechaFinal, Producto producto,
                      Cliente cliente, String ciudadOrigen, String infoCovid, Boolean vacunadoCovid) {
        this.horaInicio = horaInicio;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.producto = producto;
        this.cliente = cliente;
        this.ciudadOrigen = ciudadOrigen;
        this.infoCovid = infoCovid;
        this.vacunadoCovid = vacunadoCovid;
    }

    @Override
    public String toString() {
        return "ReservaDTO{" +
                "id=" + id +
                ", horaInicio=" + horaInicio +
                ", fechaInicial=" + fechaInicial +
                ", fechaFinal=" + fechaFinal +
                ", producto=" + producto +
                ", cliente=" + cliente +
                ", ciudadOrigen='" + ciudadOrigen + '\'' +
                ", infoCovid='" + infoCovid + '\'' +
                ", vacunadoCovid=" + vacunadoCovid +
                '}';
    }
}
