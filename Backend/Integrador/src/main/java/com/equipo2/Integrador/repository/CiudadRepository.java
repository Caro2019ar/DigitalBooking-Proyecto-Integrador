package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {

    Optional<Ciudad> findByNombre(String nombre);
}