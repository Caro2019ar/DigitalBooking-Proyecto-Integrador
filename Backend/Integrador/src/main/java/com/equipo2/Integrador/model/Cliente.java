package com.equipo2.Integrador.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;


@Entity
public class Cliente extends Usuario {

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)   // El cascade es para poder borrar Clientes que tienen favoritos asociados
    @JoinTable( name = "favoritos",
            inverseJoinColumns = { @JoinColumn(name = "producto_id") } )     // Sin el 'inverseJoinColumns' pone el nombre de la columna en plural
    @JsonIgnoreProperties({ "descripcion", "ciudad", "categoria", "imagenes", "caracteristicas",
                            "latitud", "longitud", "direccion", "politica", "puntuaciones" })
    private List<Producto> productosFavoritos = new ArrayList<>();       // Uso List en lugar de Set para preservar el orden de inserción


    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        super(nombre, apellido, email, contrasena, rol, habilitado);
    }

    public Cliente(Long id, String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        super(id, nombre, apellido, email, contrasena, rol, habilitado);
    }

    public Cliente(String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado, List<Producto> productosFavoritos)
    {
        super(nombre, apellido, email, contrasena, rol, habilitado);
        if (productosFavoritos != null) this.productosFavoritos = productosFavoritos;
    }

    public Cliente(Long id, String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado, List<Producto> productosFavoritos)
    {
        super(id, nombre, apellido, email, contrasena, rol, habilitado);
        if (productosFavoritos != null) this.productosFavoritos = productosFavoritos;
    }

    public List<Producto> getProductosFavoritos()
    {
        return productosFavoritos;
    }

    public void setProductosFavoritos(List<Producto> productosFavoritos)
    {
        this.productosFavoritos = productosFavoritos;
    }

    public void agregarFavorito(Producto producto)
    {
        productosFavoritos.add(producto);
    }

    public void eliminarFavorito(Producto producto)
    {
        productosFavoritos.remove(producto);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", contrasena='" + getContrasena() + '\'' +
                ", rol=" + getRol() +
                ", habilitado=" + getHabilitado() +
                ", productosFavoritos=" + productosFavoritos +
                '}';
    }
}
