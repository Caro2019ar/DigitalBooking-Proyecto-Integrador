package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SuppressWarnings("unused")
@Repository
@Transactional(readOnly = true)
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query
    public Optional<Cliente> findByEmail(String email);

    /*@Query("DELETE FROM Cliente.productosFavoritos f WHERE (:productoId = f.id)")
    public void deleteFavorito(Long productoId);*/
}