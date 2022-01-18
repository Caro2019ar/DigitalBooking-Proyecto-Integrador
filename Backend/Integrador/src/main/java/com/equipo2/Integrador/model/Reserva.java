package com.equipo2.Integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name="RESERVAS")
public class Reserva {

    @Id
    //@SequenceGenerator(name = "reserva_sequence_generator", sequenceName = "reserva_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserva_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 31)
    private LocalTime horaInicio;

    @NotNull
    @Column(length = 63)
    private LocalDate fechaInicial;

    @NotNull
    @Column(length = 63)
    private LocalDate fechaFinal;

    @ManyToOne(fetch = FetchType.EAGER)   // Es el default
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)   // Es el default
    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(length = 127)
    private String ciudadOrigen;

    @Column(columnDefinition="TEXT")
    private String infoCovid;

    private Boolean vacunadoCovid;


    public Reserva() {
    }

    public Reserva(LocalTime horaInicio, LocalDate fechaInicial, LocalDate fechaFinal, Producto producto,
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

    public Reserva(Long id, LocalTime horaInicio, LocalDate fechaInicial, LocalDate fechaFinal, Producto producto,
                   Cliente cliente, String ciudadOrigen, String infoCovid, Boolean vacunadoCovid) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.producto = producto;
        this.cliente = cliente;
        this.ciudadOrigen = ciudadOrigen;
        this.infoCovid = infoCovid;
        this.vacunadoCovid = vacunadoCovid;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getHoraInicio()
    {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio)
    {
        this.horaInicio = horaInicio;
    }

    public LocalDate getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(LocalDate fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getCiudadOrigen()
    {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(String ciudadOrigen)
    {
        this.ciudadOrigen = ciudadOrigen;
    }

    public String getInfoCovid()
    {
        return infoCovid;
    }

    public void setInfoCovid(String infoCovid)
    {
        this.infoCovid = infoCovid;
    }

    public Boolean getVacunadoCovid()
    {
        return vacunadoCovid;
    }

    public void setVacunadoCovid(Boolean vacunadoCovid)
    {
        this.vacunadoCovid = vacunadoCovid;
    }

    @Override
    public String toString() {
        return "Reserva{" +
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

    // Compara ID's. Devuelve false si cualquiera de los dos ID's es nulo
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        if (id == null) return false;
        return id.equals(reserva.id);
    }
}
