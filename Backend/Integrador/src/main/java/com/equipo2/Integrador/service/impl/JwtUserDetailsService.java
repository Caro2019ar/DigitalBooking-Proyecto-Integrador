package com.equipo2.Integrador.service.impl;

import java.util.Optional;

import com.equipo2.Integrador.DTO.mapper.UsuarioDTOModelMapper;
import com.equipo2.Integrador.model.Usuario;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public JwtUserDetailsService(UsuarioRepository usuarioRepository)
    {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty())
            throw new UsernameNotFoundException("El usuario con el email " + email + " no existe.");

        return usuario.get();
    }
}