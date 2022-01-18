package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query
    public List<Reserva> findByProductoId(Long id);

    @Query
    public List<Reserva> findByClienteId(Long id);

    @Query("SELECT r FROM Reserva r WHERE (:productoId IS NULL OR r.producto.id = :productoId) AND (:clienteId IS NULL OR r.cliente.id = :clienteId)")
    public List<Reserva> findByOptionalProductoIdAndOptionalClienteId(Long productoId, Long clienteId);

    // Query para saber si para el producto dado existe alguna reserva con la que entre en conflicto
    @Query( "SELECT 1 " +
            "FROM Reserva r " +
            "INNER JOIN Producto p " +
            "ON r.producto.id = p.id " +
            "WHERE (p = :producto AND :inicio <= r.fechaFinal AND r.fechaInicial <= :fin)" )
    public Optional<Reserva> findByProductoAndFechas(Producto producto, LocalDate inicio, LocalDate fin);
}