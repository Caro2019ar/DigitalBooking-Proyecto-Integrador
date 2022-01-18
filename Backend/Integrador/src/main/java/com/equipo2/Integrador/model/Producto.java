package com.equipo2.Integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name="PRODUCTOS")
public class Producto {

    @Id
    //@SequenceGenerator(name = "producto_sequence_generator", sequenceName = "producto_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 127)
    private String nombre;

    @Column(columnDefinition="TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)   // Es el default
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ciudad_id")
    //@JsonIgnoreProperties("productos")    // Sólo es necesario si la relación es bidireccional
    private Ciudad ciudad;

    @ManyToOne(fetch = FetchType.EAGER)   // Es el default
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    //@JsonIgnoreProperties("productos")    // Sólo es necesario si la relación es bidireccional
    private Categoria categoria;

    // Esta relación la hago bidireccional porque es más eficiente para Hibernate en los add/remove (realiza una sola query en lugar de dos)
    // El orphanRemoval sirve para eliminar las imagenes de la BD al borrarlas mediante el controlador de productos
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // El allowSetters es para evitar un error del ObjectMapper en caso de dejar la anotación habilitada
    @JsonIgnoreProperties(value = "producto", allowSetters = true)
    private List<Imagen> imagenes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "productos_x_caracteristicas",
                inverseJoinColumns = { @JoinColumn(name = "caracteristica_id") } )     // Sin el 'inverseJoinColumns' pone el nombre de la columna en plural
    private Set<Caracteristica> caracteristicas = new HashSet<>();

    private Double latitud;

    private Double longitud;

    @Column(length = 127)
    private String direccion;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Politica politica;

    // El orphanRemoval sirve para eliminar las imagenes de la BD al borrarlas mediante el controlador de productos
    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // El allowSetters es para evitar un error del ObjectMapper en caso de dejar la anotación habilitada
    @JsonIgnoreProperties(value = "producto", allowSetters = true)
    private List<Puntuacion> puntuaciones = new ArrayList<>();


    public Producto() {
    }

    // Sin puntuaciones y sin ID
    public Producto(String nombre, String descripcion, Ciudad ciudad, Categoria categoria, List<Imagen> imagenes,
                    Set<Caracteristica> caracteristicas, Double latitud, Double longitud, String direccion,
                    Politica politica) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.categoria = categoria;
        if (imagenes != null) this.imagenes = imagenes;
        if (caracteristicas != null) this.caracteristicas = caracteristicas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.politica = politica;
    }

    // Sin puntuaciones y con ID
    public Producto(Long id, String nombre, String descripcion, Ciudad ciudad, Categoria categoria, List<Imagen> imagenes,
                    Set<Caracteristica> caracteristicas, Double latitud, Double longitud, String direccion,
                    Politica politica) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.categoria = categoria;
        if (imagenes != null) this.imagenes = imagenes;
        if (caracteristicas != null) this.caracteristicas = caracteristicas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.politica = politica;
    }

    // Con puntuaciones y sin ID
    public Producto(String nombre, String descripcion, Ciudad ciudad, Categoria categoria, List<Imagen> imagenes,
                    Set<Caracteristica> caracteristicas, Double latitud, Double longitud, String direccion,
                    Politica politica, List<Puntuacion> puntuaciones) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.categoria = categoria;
        if (imagenes != null) this.imagenes = imagenes;
        if (caracteristicas != null) this.caracteristicas = caracteristicas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.politica = politica;
        if (puntuaciones != null) this.puntuaciones = puntuaciones;
    }

    // Con puntuaciones y con ID
    public Producto(Long id, String nombre, String descripcion, Ciudad ciudad, Categoria categoria, List<Imagen> imagenes,
                    Set<Caracteristica> caracteristicas, Double latitud, Double longitud, String direccion,
                    Politica politica, List<Puntuacion> puntuaciones) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.categoria = categoria;
        if (imagenes != null) this.imagenes = imagenes;
        if (caracteristicas != null) this.caracteristicas = caracteristicas;
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.politica = politica;
        if (puntuaciones != null) this.puntuaciones = puntuaciones;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public Set<Caracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public Double getLatitud() {
        return latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public String getDireccion()
    {
        return direccion;
    }

    public Politica getPolitica()
    {
        return politica;
    }

    public List<Puntuacion> getPuntuaciones() {
        return puntuaciones;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setImagenes(List<Imagen> imagenes)
    {
        // Para evitar error cuando se cambia la lista de imagenes en el PUT
        this.imagenes.clear();
        if (imagenes != null)
        {
            for ( Imagen imagen : imagenes )
            {
                addImagen(imagen);
            }
        }
    }

    public void addImagen(Imagen imagen)
    {
        this.imagenes.add(imagen);
        imagen.setProducto(this);
    }

    public void removeImagen(Imagen imagen)
    {
        this.imagenes.remove(imagen);
        imagen.setProducto(null);
    }

    public void setCaracteristicas(Set<Caracteristica> caracteristicas)
    {
        this.caracteristicas = caracteristicas;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public void setDireccion(String direccion)
    {
        this.direccion = direccion;
    }

    public void setPolitica(Politica politica)
    {
        this.politica = politica;
    }

    public void setPuntuaciones(List<Puntuacion> puntuaciones)
    {
        // Para evitar error cuando se cambia la lista de imagenes en el PUT
        this.puntuaciones.clear();
        if (puntuaciones != null)
        {
            for ( Puntuacion puntuacion : puntuaciones )
            {
                addPuntuacion(puntuacion);
            }
        }
    }

    public void addPuntuacion(Puntuacion puntuacion)
    {
        this.puntuaciones.add(puntuacion);
        puntuacion.setProducto(this);
    }

    public void removePuntuacion(Puntuacion puntuacion)
    {
        this.puntuaciones.remove(puntuacion);
        puntuacion.setProducto(null);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", ciudad=" + ciudad +
                ", categoria=" + categoria +
                ", imagenes=" + imagenes +
                ", caracteristicas=" + caracteristicas +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", direccion='" + direccion + '\'' +
                ", politica=" + politica +
                '}';
    }

    // Compara ID's. Devuelve false si cualquiera de los dos ID's es nulo
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        if (id == null) return false;
        return id.equals(producto.id);
    }

    // Código que usé para probar la diferencia entre las relaciones One-to-Many unidireccionales y las bidireccionales
    /*
    public void addImagen(Imagen imagen)
    {
        imagenes.add(imagen);
        imagen.setProducto(this);
    }
    */
}
