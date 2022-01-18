package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.DTO.mapper.CaracteristicaDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.repository.CaracteristicaRepository;
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
public class CaracteristicaService implements IService<CaracteristicaDTO, Long> {

    private final CaracteristicaRepository caracteristicaRepository;
    private final CaracteristicaDTOModelMapper caracteristicaDTOModelMapper;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(CaracteristicaService.class);

    @Autowired
    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository, CaracteristicaDTOModelMapper caracteristicaDTOModelMapper,
                                 ProductoRepository productoRepository)
    {
        this.caracteristicaRepository = caracteristicaRepository;
        this.caracteristicaDTOModelMapper = caracteristicaDTOModelMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CaracteristicaDTO agregar(CaracteristicaDTO caracteristicaDTO) throws BadRequestException, ResourceConflictException
    {
        Caracteristica caracteristica = caracteristicaDTOModelMapper.toModel(caracteristicaDTO);

        if (caracteristica.getId() != null)
            throw new BadRequestException("El registro de características no puede recibir un ID.");

        if (caracteristicaRepository.findByNombre(caracteristica.getNombre()).isPresent())
            throw new ResourceConflictException("La característica con el nombre '" + caracteristica.getNombre() + "' ya existe.");

        Caracteristica caracteristicaSaved = caracteristicaRepository.save(caracteristica);
        logger.info("Se registró una característica con ID " + caracteristicaSaved.getId() + ".");

        return caracteristicaDTOModelMapper.toDTO(caracteristicaSaved);
    }

    @Override
    public CaracteristicaDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Caracteristica> optionalCaracteristica = caracteristicaRepository.findById(id);

        if (optionalCaracteristica.isEmpty())
            throw new ResourceNotFoundException("La característica con el ID " + id + " no existe.");

        return caracteristicaDTOModelMapper.toDTO(optionalCaracteristica.get());
    }

    @Override
    public List<CaracteristicaDTO> listar()
    {
        return caracteristicaDTOModelMapper.toDTOList(caracteristicaRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CaracteristicaDTO actualizar(CaracteristicaDTO caracteristicaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Caracteristica caracteristica = caracteristicaDTOModelMapper.toModel(caracteristicaDTO);

        if (caracteristica.getId() == null)
            throw new BadRequestException("La actualización de características requiere de un ID.");

        if (caracteristicaRepository.findById(caracteristica.getId()).isEmpty())
            throw new ResourceNotFoundException("La característica con el ID " + caracteristica.getId() + " no existe.");

        Optional<Caracteristica> caracteristicaMismoNombre = caracteristicaRepository.findByNombre(caracteristica.getNombre());
        if (caracteristicaMismoNombre.isPresent() && !caracteristicaMismoNombre.get().getId().equals(caracteristica.getId()))
            throw new ResourceConflictException("La característica con el nombre '" + caracteristica.getNombre() + "' ya existe.");

        return caracteristicaDTOModelMapper.toDTO(caracteristicaRepository.save(caracteristica));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Caracteristica> caracteristica = caracteristicaRepository.findById(id);
        if (caracteristica.isPresent())
        {
            if (productoRepository.findByCaracteristicasId(id).isEmpty())
            {
                caracteristicaRepository.deleteById(id);
                logger.info("Se eliminó la característica con ID " + id + ".");
            }
            else
                throw new BadRequestException("La característica con el ID " + id + " no puede ser eliminada porque tiene productos asociados.");
        }
        else
            throw new ResourceNotFoundException("La característica con el ID " + id + " no existe.");
    }
}