package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.AdminDTO;
import com.equipo2.Integrador.DTO.mapper.AdminDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Admin;
import com.equipo2.Integrador.model.Usuario;
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
public class AdminService implements IService<AdminDTO, Long> {

    private final AdminRepository adminRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdminDTOModelMapper adminDTOModelMapper;
    private final RolRepository rolRepository;
    private final Logger logger = Logger.getLogger(AdminService.class);

    @Autowired
    public AdminService(AdminRepository adminRepository, UsuarioRepository usuarioRepository,
                        AdminDTOModelMapper adminDTOModelMapper, RolRepository rolRepository)
    {
        this.adminRepository = adminRepository;
        this.usuarioRepository = usuarioRepository;
        this.adminDTOModelMapper = adminDTOModelMapper;
        this.rolRepository = rolRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminDTO agregar(AdminDTO adminDTO) throws BadRequestException, ResourceConflictException
    {
        Admin admin = adminDTOModelMapper.toModel(adminDTO);

        if (admin.getId() != null)
            throw new BadRequestException("El registro de admins no puede recibir un ID.");

        if (admin.getHabilitado() == null)
            throw new BadRequestException("El atributo 'habilitado' no puede ser nulo.");

        if (usuarioRepository.findByEmail(admin.getEmail()).isPresent())
            throw new ResourceConflictException("El usuario con el email " + admin.getEmail() + " ya existe.");

        admin.setRol(rolRepository.findByNombre("ROLE_ADMIN").orElse(null));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        admin.setContrasena(passwordEncoder.encode(admin.getContrasena()));

        Admin adminSaved = adminRepository.save(admin);
        logger.info("Se registró un admin con ID " + adminSaved.getId() + ".");

        return adminDTOModelMapper.toDTO(adminSaved);
    }

    @Override
    public AdminDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Admin> admin = adminRepository.findById(id);

        if (admin.isEmpty())
            throw new ResourceNotFoundException("El admin con el ID " + id + " no existe.");

        return adminDTOModelMapper.toDTO(admin.get());
    }

    public AdminDTO buscarPorEmail(String email) throws ResourceNotFoundException
    {
        Optional<Admin> admin = adminRepository.findByEmail(email);

        if (admin.isEmpty())
            throw new ResourceNotFoundException("El admin con el email " + email + " no existe.");

        return adminDTOModelMapper.toDTO(admin.get());
    }

    @Override
    public List<AdminDTO> listar()
    {
        return adminDTOModelMapper.toDTOList(adminRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminDTO actualizar(AdminDTO adminDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException {
        Admin admin = adminDTOModelMapper.toModel(adminDTO);

        if (admin.getId() == null)
            throw new BadRequestException("La actualización de admins requiere de un ID.");

        Optional<Admin> adminBuscado = adminRepository.findById(admin.getId());
        if (adminBuscado.isEmpty())
            throw new ResourceNotFoundException("El admin con el ID " + admin.getId() + " no existe.");

        Optional<Usuario> usuarioMismoEmail = usuarioRepository.findByEmail(admin.getEmail());
        if (usuarioMismoEmail.isPresent() && !usuarioMismoEmail.get().getId().equals(admin.getId()))
            throw new ResourceConflictException("El usuario con el email " + admin.getEmail() + " ya existe.");

        if (admin.getHabilitado() == null)
            admin.setHabilitado(adminBuscado.get().getHabilitado());

        admin.setRol(rolRepository.findByNombre("ROLE_ADMIN").orElse(null));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        admin.setContrasena(passwordEncoder.encode(admin.getContrasena()));

        return adminDTOModelMapper.toDTO(adminRepository.save(admin));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        if (adminRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("El admin con el ID " + id + " no existe.");

        adminRepository.deleteById(id);
        logger.info("Se eliminó el admin con ID " + id + ".");
    }
}