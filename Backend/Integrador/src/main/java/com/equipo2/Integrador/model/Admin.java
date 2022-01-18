package com.equipo2.Integrador.model;

import javax.persistence.Entity;


@Entity
public class Admin extends Usuario {

    public Admin() {
    }

    public Admin(String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        super(nombre, apellido, email, contrasena, rol, habilitado);
    }

    public Admin(Long id, String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        super(id, nombre, apellido, email, contrasena, rol, habilitado);
    }
}
