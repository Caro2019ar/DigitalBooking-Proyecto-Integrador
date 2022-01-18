package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.RolDTO;
import com.equipo2.Integrador.DTO.mapper.RolDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.UsuarioRepository;
import com.equipo2.Integrador.service.IService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
public class RolService implements IService<RolDTO, Long> {

    private final RolRepository rolRepository;
    private final RolDTOModelMapper rolDTOModelMapper;
    private final UsuarioRepository usuarioRepository;
    private final Logger logger = Logger.getLogger(RolService.class);

    @Autowired
    public RolService(RolRepository rolRepository, RolDTOModelMapper rolDTOModelMapper, UsuarioRepository usuarioRepository)
    {
        this.rolRepository = rolRepository;
        this.rolDTOModelMapper = rolDTOModelMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RolDTO agregar(RolDTO rolDTO) throws BadRequestException, ResourceConflictException
    {
        Rol rol = rolDTOModelMapper.toModel(rolDTO);

        if (rol.getId() != null)
            throw new BadRequestException("El registro de roles no puede recibir un ID.");

        if (rolRepository.findByNombre(rol.getNombre()).isPresent())
            throw new ResourceConflictException("El rol con el nombre '" + rol.getNombre() + "' ya existe.");

        Rol rolSaved = rolRepository.save(rol);
        logger.info("Se registró un rol con ID " + rolSaved.getId() + ".");

        return rolDTOModelMapper.toDTO(rolSaved);
    }

    @Override
    public RolDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Rol> optionalRol = rolRepository.findById(id);

        if (optionalRol.isEmpty())
            throw new ResourceNotFoundException("El rol con el ID " + id + " no existe.");

        return rolDTOModelMapper.toDTO(optionalRol.get());
    }

    @Override
    public List<RolDTO> listar()
    {
        return rolDTOModelMapper.toDTOList(rolRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RolDTO actualizar(RolDTO rolDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Rol rol = rolDTOModelMapper.toModel(rolDTO);

        if (rol.getId() == null)
            throw new BadRequestException("La actualización de roles requiere de un ID.");

        if (rolRepository.findById(rol.getId()).isEmpty())
            throw new ResourceNotFoundException("El rol con el ID " + rol.getId() + " no existe.");

        Optional<Rol> rolMismoNombre = rolRepository.findByNombre(rol.getNombre());
        if (rolMismoNombre.isPresent() && !rolMismoNombre.get().getId().equals(rol.getId()))
            throw new ResourceConflictException("El rol con el nombre '" + rol.getNombre() + "' ya existe.");

        return rolDTOModelMapper.toDTO(rolRepository.save(rol));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Rol> rol = rolRepository.findById(id);
        if (rol.isPresent())
        {
            if (usuarioRepository.findByRol(rol.get()).isEmpty())
            {
                rolRepository.deleteById(id);
                logger.info("Se eliminó el rol con ID " + id + ".");
            }
            else
                throw new BadRequestException("El rol con el ID " + id + " no puede ser eliminado porque tiene usuarios asociados.");
        }
        else
            throw new ResourceNotFoundException("El rol con el ID " + id + " no existe.");
    }
}
