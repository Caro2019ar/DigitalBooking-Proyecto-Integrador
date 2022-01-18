package com.equipo2.Integrador.DTO;

import com.equipo2.Integrador.model.Rol;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(name = "Usuario")
@JsonInclude(JsonInclude.Include.NON_NULL)      // Para evitar el 'contrasena: null' en el JSON
public class UsuarioDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasena;

    @Schema(hidden = true)
    private Rol rol;

    private Boolean habilitado;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.habilitado = habilitado;
    }

    public UsuarioDTO(Long id, String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.habilitado = habilitado;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "UsuarioDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + rol +
                ", habilitado=" + habilitado +
                '}';
    }
}