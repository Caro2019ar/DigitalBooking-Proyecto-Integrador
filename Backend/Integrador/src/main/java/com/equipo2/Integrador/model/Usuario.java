package com.equipo2.Integrador.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)     // Es el default
@Table(name="USUARIOS")
public class Usuario implements UserDetails {

    @Id
    //@SequenceGenerator(name = "usuario_sequence_generator", sequenceName = "usuario_sequence", allocationSize = 1)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_sequence_generator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 63)
    private String nombre;

    @Column(length = 63)
    private String apellido;

    @NotNull
    @Column(length = 63)
    private String email;

    @NotNull
    @Column(length = 127)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @NotNull
    private Boolean habilitado;


    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.habilitado = habilitado;
    }

    public Usuario(Long id, String nombre, String apellido, String email, String contrasena, Rol rol, Boolean habilitado)
    {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.habilitado = habilitado;
    }

    // @JsonIgnore
    // @JsonDeserialize(contentAs = SimpleGrantedAuthority.class)     // Para que jackson pueda deserializarlo (no puede deserializar clases abstractas/interfaces)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(rol.getNombre());
        return Collections.singletonList(grantedAuthority);
        // Jackson no puede deserializar la lista de arriba, hay que usar una distinta
        /* ArrayList<SimpleGrantedAuthority> l = new ArrayList<SimpleGrantedAuthority>();
        l.add(grantedAuthority);
        return l; */
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return habilitado;
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

    public void setHabilitado(Boolean habilitado)
    {
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", rol=" + rol +
                ", habilitado=" + habilitado +
                '}';
    }
}