package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Puntuacion;
import com.equipo2.Integrador.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface PuntuacionRepository extends JpaRepository<Puntuacion, Long> {

    @Query("SELECT p FROM Puntuacion p WHERE (:productoId IS NULL OR p.producto.id = :productoId) AND (:clienteId IS NULL OR p.cliente.id = :clienteId)")
    public List<Puntuacion> findByOptionalProductoIdAndOptionalClienteId(Long productoId, Long clienteId);
}