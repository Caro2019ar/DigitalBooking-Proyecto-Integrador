package com.equipo2.Integrador.model;

import javax.persistence.*;


@Entity
@Table(name="POLITICAS")
public class Politica {

    @Id
    //@SequenceGenerator(name = "politica_sequence_generator", sequenceName = "politica_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "politica_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String normas;

    @Column(columnDefinition="TEXT")
    private String saludYSeguridad;

    @Column(columnDefinition="TEXT")
    private String cancelacion;


    public Politica() {
    }

    public Politica(String normas, String saludYSeguridad, String cancelacion) {
        this.normas = normas;
        this.saludYSeguridad = saludYSeguridad;
        this.cancelacion = cancelacion;
    }

    public Politica(Long id, String normas, String saludYSeguridad, String cancelacion) {
        this.id = id;
        this.normas = normas;
        this.saludYSeguridad = saludYSeguridad;
        this.cancelacion = cancelacion;
    }

    public Long getId() {
        return id;
    }

    public String getNormas() {
        return normas;
    }

    public String getSaludYSeguridad() {
        return saludYSeguridad;
    }

    public String getCancelacion() {
        return cancelacion;
    }

    public void setNormas(String normas) {
        this.normas = normas;
    }

    public void setSaludYSeguridad(String saludYSeguridad) {
        this.saludYSeguridad = saludYSeguridad;
    }

    public void setCancelacion(String cancelacion) {
        this.cancelacion = cancelacion;
    }

    @Override
    public String toString() {
        return "Politica{" +
                "id=" + id +
                ", normas='" + normas + '\'' +
                ", saludYSeguridad='" + saludYSeguridad + '\'' +
                ", cancelacion='" + cancelacion + '\'' +
                '}';
    }
}
