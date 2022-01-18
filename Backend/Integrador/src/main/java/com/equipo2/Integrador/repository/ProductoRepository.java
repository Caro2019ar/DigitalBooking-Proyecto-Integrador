package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query
    public List<Producto> findByCategoria(Categoria categoria);

    @Query
    public List<Producto> findByCiudad(Ciudad ciudad);

    // Query reemplazada por la siguiente (mucho más simple jaja)
    /*@Query( "SELECT p " +
            "FROM Producto p " +
            "LEFT JOIN FETCH p.caracteristicas " +
            "WHERE EXISTS ( " +
                "SELECT 1 " +
                "FROM p.caracteristicas c " +
                "WHERE c.id = :caracteristicaID " +
            ")" )
    public List<Producto> findByCaracteristica(Long caracteristicaID);*/

    @Query
    public List<Producto> findByCaracteristicasId(Long caracteristicaID);

    @Query("SELECT p FROM Producto p WHERE (:ciudad IS NULL OR p.ciudad.nombre = :ciudad) AND (:categoria IS NULL OR p.categoria.titulo = :categoria)")
    public List<Producto> findByOptionalCiudadAndOptionalCategoria(String ciudad, String categoria);

    @Query( "SELECT p " +
            "FROM Producto p " +
            "WHERE (:ciudad IS NULL OR p.ciudad.nombre = :ciudad) " +
            "AND NOT EXISTS ( " +
                "SELECT 1 " +
                "FROM Producto d " +
                "INNER JOIN Reserva r " +
                "ON d.id = r.producto.id " +
                "WHERE (p.id = d.id AND :inicio <= r.fechaFinal AND r.fechaInicial <= :fin) " +
            ")" )
    public List<Producto> findByOptionalCiudadAndFechas(String ciudad, LocalDate inicio, LocalDate fin);

    /*@Query( "SELECT d " +
            "FROM Producto d " +
            "INNER JOIN Reserva r " +
            "ON d.id = r.producto.id " +
            "WHERE (d.ciudad.nombre = :ciudad AND :inicio <= r.fechaFinal AND r.fechaInicial <= :fin)" )
    public List<Producto> findByCiudadAndFechas2(String ciudad, LocalDate inicio, LocalDate fin);*/

    @Query( "SELECT pr " +
            "FROM Producto pr " +
            "INNER JOIN Puntuacion pu " +
            "ON pr.id = pu.producto.id " +
            "INNER JOIN Cliente c " +
            "ON pu.cliente.id = c.id " +
            "WHERE (c.id = :id)"
            )
    public List<Producto> findByPuntuacionesCliente(Long id);
}
