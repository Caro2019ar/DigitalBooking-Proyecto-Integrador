package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {

    @Query
    public Optional<Rol> findByNombre(String nombre);
}