package com.equipo2.Integrador.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="VERIFICATION_TOKENS")
public class VerificationToken {

    // Tiempo de expiración del token en minutos
    private static final int EXPIRACION = 60 * 24;
    //private static final int EXPIRACION = 1;

    @Id
    //@SequenceGenerator(name = "verification_token_sequence_generator", sequenceName = "verification_token_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)  // Esto es para poder borrar Clientes que tienen tokens asociados
    private Cliente cliente;

    private LocalDateTime fechaExpiracion;


    public VerificationToken() {
    }

    public VerificationToken(String token, Cliente cliente)
    {
        this.token = token;
        this.cliente = cliente;
        this.fechaExpiracion = calcularFechaExpiracion(EXPIRACION);
    }

    public VerificationToken(Long id, String token, Cliente cliente) {
        this.id = id;
        this.token = token;
        this.cliente = cliente;
        this.fechaExpiracion = calcularFechaExpiracion(EXPIRACION);
    }

    private LocalDateTime calcularFechaExpiracion(int expiryTimeInMinutes)
    {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    public Boolean estaExpirado()
    {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCliente(Usuario usuario) {
        this.cliente = cliente;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", cliente=" + cliente +
                ", fechaExpiracion=" + fechaExpiracion +
                '}';
    }
}