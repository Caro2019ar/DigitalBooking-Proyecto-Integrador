package com.equipo2.Integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
@Table(name="PUNTUACIONES")
public class Puntuacion {

    @Id
    //@SequenceGenerator(name = "puntuacion_sequence_generator", sequenceName = "puntuacion_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puntuacion_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  // Esto es para poder borrar Productos que tienen puntuaciones asociadas
    @JsonIgnoreProperties({ "descripcion", "ciudad", "categoria", "imagenes", "caracteristicas",
                            "latitud", "longitud", "direccion", "politica", "puntuaciones" })
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  // Esto es para poder borrar Clientes que tienen puntuaciones asociadas
    @JsonIgnoreProperties({ "nombre", "apellido", "contrasena", "rol", "habilitado", "productosFavoritos" })
    private Cliente cliente;

    private Integer puntos;


    public Puntuacion() {
    }

    public Puntuacion(Producto producto, Cliente cliente, Integer puntos)
    {
        this.producto = producto;
        this.cliente = cliente;
        this.puntos = puntos;
    }

    public Puntuacion(Long id, Producto producto, Cliente cliente, Integer puntos)
    {
        this.id = id;
        this.producto = producto;
        this.cliente = cliente;
        this.puntos = puntos;
    }

    public Long getId() {
        return id;
    }

    public Producto getProducto() {
        return producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    @Override
    public String toString() {
        return "Puntuacion{" +
                "id=" + id +
                ", producto=" + producto +
                ", cliente=" + cliente +
                ", puntos=" + puntos +
                '}';
    }
}