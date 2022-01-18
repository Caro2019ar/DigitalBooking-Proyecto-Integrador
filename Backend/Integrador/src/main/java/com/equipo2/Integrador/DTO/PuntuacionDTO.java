package com.equipo2.Integrador.DTO;

import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Producto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(name = "Puntuacion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PuntuacionDTO {

    private Long id;

    @Schema(hidden = true)
    @JsonIgnoreProperties({ "descripcion", "ciudad", "categoria", "imagenes", "caracteristicas",
                            "latitud", "longitud", "direccion", "politica", "puntuaciones" })
    private Producto producto;

    @Schema(hidden = true)
    //@JsonIgnoreProperties("puntuaciones")    // Sólo es necesario si la relación es bidireccional
    @JsonIgnoreProperties({ "nombre", "apellido", "productosFavoritos", "contrasena", "rol", "habilitado", "enabled",
                            "password", "authorities", "username", "credentialsNonExpired", "accountNonExpired", "accountNonLocked" })
    private Cliente cliente;

    private Integer puntos;


    public PuntuacionDTO(Producto producto, Cliente cliente, Integer puntos) {
        this.producto = producto;
        this.cliente = cliente;
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "PuntuacionDTO{" +
                "id=" + id +
                ", producto=" + producto +
                ", cliente=" + cliente +
                ", puntos=" + puntos +
                '}';
    }
}
