package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.UsuarioDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.exceptions.UnauthorizedAccessException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.IService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
public class UsuarioService implements IService<UsuarioDTO, Long> {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDTOModelMapper usuarioDTOModelMapper;
    private final RolRepository rolRepository;
    private final ReservaRepository reservaRepository;
    private final Logger logger = Logger.getLogger(UsuarioService.class);

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioDTOModelMapper usuarioDTOModelMapper,
                          RolRepository rolRepository, ReservaRepository reservaRepository)
    {
        this.usuarioRepository = usuarioRepository;
        this.usuarioDTOModelMapper = usuarioDTOModelMapper;
        this.rolRepository = rolRepository;
        this.reservaRepository = reservaRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public UsuarioDTO agregar(UsuarioDTO usuarioDTO) throws BadRequestException, ResourceConflictException
    {
        Usuario usuario = usuarioDTOModelMapper.toModel(usuarioDTO);

        if (usuario.getId() != null)
            throw new BadRequestException("El registro de usuarios no puede recibir un ID.");

        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent())
            throw new ResourceConflictException("El usuario con el email " + usuario.getEmail() + " ya existe.");

        if (usuario.getHabilitado() == null)
            throw new BadRequestException("El atributo 'habilitado' no puede ser nulo.");

        Rol rol = usuario.getRol();
        if (rol == null)
            throw new BadRequestException("El rol no puede ser nulo.");

        if (rol.getId() == null)
            throw new BadRequestException("El ID del rol no puede ser nulo.");

        Optional<Rol> optionalRol = rolRepository.findById(rol.getId());
        if (optionalRol.isEmpty())
            throw new BadRequestException("El rol con ID " + rol.getId() + " no existe.");

        // Esto es puramente para que rol no tenga propiedades null en el JSON
        usuario.setRol(optionalRol.get());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        Usuario usuarioSaved = usuarioRepository.save(usuario);
        logger.info("Se registró un usuario con ID " + usuarioSaved.getId() + ".");

        return usuarioDTOModelMapper.toDTO(usuarioSaved);
    }

    @Override
    public UsuarioDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isEmpty())
            throw new ResourceNotFoundException("El usuario con el ID " + id + " no existe.");

        return usuarioDTOModelMapper.toDTO(usuario.get());
    }

    public UsuarioDTO buscarPorEmail(String email) throws ResourceNotFoundException
    {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty())
            throw new ResourceNotFoundException("El usuario con el email " + email + " no existe.");

        return usuarioDTOModelMapper.toDTO(usuario.get());
    }

    @Override
    public List<UsuarioDTO> listar()
    {
        return usuarioDTOModelMapper.toDTOList(usuarioRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UsuarioDTO actualizar(UsuarioDTO usuarioDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Usuario usuario = usuarioDTOModelMapper.toModel(usuarioDTO);

        if (usuario.getId() == null)
            throw new BadRequestException("La actualización de usuarios requiere de un ID.");

        if (usuarioRepository.findById(usuario.getId()).isEmpty())
            throw new ResourceNotFoundException("El usuario con el ID " + usuario.getId() + " no existe.");

        if (usuario.getHabilitado() == null)
            throw new BadRequestException("El atributo 'habilitado' no puede ser nulo.");

        Rol rol = usuario.getRol();
        if (rol == null)
            throw new BadRequestException("El rol no puede ser nulo.");

        if (rol.getId() == null)
            throw new BadRequestException("El ID del rol no puede ser nulo.");

        Optional<Rol> optionalRol = rolRepository.findById(rol.getId());
        if (optionalRol.isEmpty())
            throw new BadRequestException("El rol con ID " + rol.getId() + " no existe.");

        Optional<Usuario> usuarioMismoEmail = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioMismoEmail.isPresent() && !usuarioMismoEmail.get().getId().equals(usuario.getId()))
            throw new ResourceConflictException("El usuario con el email " + usuario.getEmail() + " ya existe.");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        return usuarioDTOModelMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        if (usuarioRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("El usuario con el ID " + id + " no existe.");

        if (!reservaRepository.findByClienteId(id).isEmpty())
            throw new BadRequestException("El usuario con el ID " + id + " no puede ser eliminado porque tiene reservas asociadas.");

        usuarioRepository.deleteById(id);
        logger.info("Se eliminó el usuario con ID " + id + ".");
    }

    public UsuarioDTO validateUserLogin(UsuarioDTO usuarioDTO) throws UnauthorizedAccessException
    {
        Usuario usuario = usuarioDTOModelMapper.toModel(usuarioDTO);
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(usuario.getEmail());

        if (optionalUsuario.isEmpty())
            throw new UnauthorizedAccessException("El usuario con el email " + usuario.getEmail() + " no existe.");

        Usuario usuarioBD = optionalUsuario.get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(usuario.getContrasena(), usuarioBD.getContrasena()))
            throw new UnauthorizedAccessException("La contraseña ingresada es incorrecta.");

        if (!usuarioBD.getHabilitado())
            throw new UnauthorizedAccessException("Su cuenta aún no ha sido habilitada.\nPor favor, verifique su casilla de correo.");

        return usuarioDTOModelMapper.toDTO(usuarioBD);
    }
}