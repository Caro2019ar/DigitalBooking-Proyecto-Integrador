package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.repository.CiudadRepository;
import com.equipo2.Integrador.repository.ProductoRepository;
import com.equipo2.Integrador.service.IService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
public class CiudadService implements IService<CiudadDTO, Long> {

    private final CiudadRepository ciudadRepository;
    private final CiudadDTOModelMapper ciudadDTOModelMapper;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(CiudadService.class);

    @Autowired
    public CiudadService(CiudadRepository ciudadRepository, CiudadDTOModelMapper ciudadDTOModelMapper, ProductoRepository productoRepository)
    {
        this.ciudadRepository = ciudadRepository;
        this.ciudadDTOModelMapper = ciudadDTOModelMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CiudadDTO agregar(CiudadDTO ciudadDTO) throws BadRequestException, ResourceConflictException
    {
        Ciudad ciudad = ciudadDTOModelMapper.toModel(ciudadDTO);

        if (ciudad.getId() != null)
            throw new BadRequestException("El registro de ciudades no puede recibir un ID.");

        if (ciudadRepository.findByNombre(ciudad.getNombre()).isPresent())
            throw new ResourceConflictException("La ciudad con el nombre '" + ciudad.getNombre() + "' ya existe.");

        Ciudad ciudadSaved = ciudadRepository.save(ciudad);
        logger.info("Se registró una ciudad con ID " + ciudadSaved.getId() + ".");

        return ciudadDTOModelMapper.toDTO(ciudadSaved);
    }

    @Override
    public CiudadDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Ciudad> optionalCiudad = ciudadRepository.findById(id);

        if (optionalCiudad.isEmpty())
            throw new ResourceNotFoundException("La ciudad con el ID " + id + " no existe.");

        return ciudadDTOModelMapper.toDTO(optionalCiudad.get());
    }

    @Override
    public List<CiudadDTO> listar()
    {
        return ciudadDTOModelMapper.toDTOList(ciudadRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CiudadDTO actualizar(CiudadDTO ciudadDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Ciudad ciudad = ciudadDTOModelMapper.toModel(ciudadDTO);

        if (ciudad.getId() == null)
            throw new BadRequestException("La actualización de ciudades requiere de un ID.");

        if (ciudadRepository.findById(ciudad.getId()).isEmpty())
            throw new ResourceNotFoundException("La ciudad con el ID " + ciudad.getId() + " no existe.");

        Optional<Ciudad> ciudadMismoNombre = ciudadRepository.findByNombre(ciudad.getNombre());
        if (ciudadMismoNombre.isPresent() && !ciudadMismoNombre.get().getId().equals(ciudad.getId()))
            throw new ResourceConflictException("La ciudad con el nombre '" + ciudad.getNombre() + "' ya existe.");

        return ciudadDTOModelMapper.toDTO(ciudadRepository.save(ciudad));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Ciudad> ciudad = ciudadRepository.findById(id);
        if (ciudad.isPresent())
        {
            if (productoRepository.findByCiudad(ciudad.get()).isEmpty())
            {
                ciudadRepository.deleteById(id);
                logger.info("Se eliminó la ciudad con ID " + id + ".");
            }
            else
                throw new BadRequestException("La ciudad con el ID " + id + " no puede ser eliminada porque tiene productos asociados.");
        }
        else
            throw new ResourceNotFoundException("La ciudad con el ID " + id + " no existe.");
    }
}
