package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.PoliticaDTO;
import com.equipo2.Integrador.DTO.mapper.PoliticaDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Politica;
import com.equipo2.Integrador.repository.PoliticaRepository;
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
public class PoliticaService implements IService<PoliticaDTO, Long> {

    private final PoliticaRepository politicaRepository;
    private final PoliticaDTOModelMapper politicaDTOModelMapper;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(PoliticaService.class);

    @Autowired
    public PoliticaService(PoliticaRepository politicaRepository, PoliticaDTOModelMapper politicaDTOModelMapper, ProductoRepository productoRepository)
    {
        this.politicaRepository = politicaRepository;
        this.politicaDTOModelMapper = politicaDTOModelMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PoliticaDTO agregar(PoliticaDTO politicaDTO) throws BadRequestException, ResourceConflictException
    {
        Politica politica = politicaDTOModelMapper.toModel(politicaDTO);

        if (politica.getId() != null)
            throw new BadRequestException("El registro de políticas no puede recibir un ID.");

        Politica politicaSaved = politicaRepository.save(politica);
        logger.info("Se registró una politica con ID " + politicaSaved.getId() + ".");

        return politicaDTOModelMapper.toDTO(politicaSaved);
    }

    @Override
    public PoliticaDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Politica> optionalPolitica = politicaRepository.findById(id);

        if (optionalPolitica.isEmpty())
            throw new ResourceNotFoundException("La política con el ID " + id + " no existe.");

        return politicaDTOModelMapper.toDTO(optionalPolitica.get());
    }

    @Override
    public List<PoliticaDTO> listar()
    {
        return politicaDTOModelMapper.toDTOList(politicaRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PoliticaDTO actualizar(PoliticaDTO politicaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Politica politica = politicaDTOModelMapper.toModel(politicaDTO);

        if (politica.getId() == null)
            throw new BadRequestException("La actualización de políticas requiere de un ID.");

        if (politicaRepository.findById(politica.getId()).isEmpty())
            throw new ResourceNotFoundException("La política con el ID " + politica.getId() + " no existe.");

        return politicaDTOModelMapper.toDTO(politicaRepository.save(politica));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Politica> politica = politicaRepository.findById(id);
        if (politica.isEmpty())
            throw new ResourceNotFoundException("La política con el ID " + id + " no existe.");

        politicaRepository.deleteById(id);
        logger.info("Se eliminó la política con ID " + id + ".");
    }
}