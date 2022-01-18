package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@SuppressWarnings("unused")
@Transactional(readOnly = true)
@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {


}
