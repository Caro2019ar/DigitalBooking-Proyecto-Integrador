package com.equipo2.Integrador.repository;

import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Usuario;
import com.equipo2.Integrador.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@SuppressWarnings("unused")
@Repository
@Transactional(readOnly = true)
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query
    public VerificationToken findByToken(String token);

    @Query
    public VerificationToken findByCliente(Cliente cliente);
}