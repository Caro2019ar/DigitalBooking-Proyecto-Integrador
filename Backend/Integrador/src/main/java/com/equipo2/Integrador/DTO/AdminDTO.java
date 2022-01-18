package com.equipo2.Integrador.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Schema(name = "Admin")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)      // Para evitar el 'contrasena: null' en el JSON
public class AdminDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;
    private Boolean habilitado;


    public AdminDTO() {
    }

    public AdminDTO(String nombre, String apellido, String email, String contrasena, Boolean habilitado)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.habilitado = habilitado;
    }

    public AdminDTO(Long id, String nombre, String apellido, String email, String contrasena, Boolean habilitado)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "AdminDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", habilitado=" + habilitado +
                '}';
    }
}