package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    Optional<Caracteristica> findByNombre(String nombre);

    @Query("SELECT c.id FROM Caracteristica c")
    List<Long> getAllIDs();
}
