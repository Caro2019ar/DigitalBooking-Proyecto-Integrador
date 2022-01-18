package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.repository.CategoriaRepository;
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
public class CategoriaService implements IService<CategoriaDTO, Long> {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaDTOModelMapper categoriaDTOModelMapper;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(CategoriaService.class);

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaDTOModelMapper categoriaDTOModelMapper, ProductoRepository productoRepository)
    {
        this.categoriaRepository = categoriaRepository;
        this.categoriaDTOModelMapper = categoriaDTOModelMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CategoriaDTO agregar(CategoriaDTO categoriaDTO) throws BadRequestException, ResourceConflictException
    {
        Categoria categoria = categoriaDTOModelMapper.toModel(categoriaDTO);

        if (categoria.getId() != null)
            throw new BadRequestException("El registro de categorías no puede recibir un ID.");

        if (categoriaRepository.findByTitulo(categoria.getTitulo()).isPresent())
            throw new ResourceConflictException("La categoría con el título '" + categoria.getTitulo() + "' ya existe.");

        Categoria categoriaSaved = categoriaRepository.save(categoria);
        logger.info("Se registró una categoría con ID " + categoriaSaved.getId() + ".");

        return categoriaDTOModelMapper.toDTO(categoriaSaved);
    }

    @Override
    public CategoriaDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);

        if (optionalCategoria.isEmpty())
            throw new ResourceNotFoundException("La categoría con el ID " + id + " no existe.");

        return categoriaDTOModelMapper.toDTO(optionalCategoria.get());
    }

    @Override
    public List<CategoriaDTO> listar()
    {
        return categoriaDTOModelMapper.toDTOList(categoriaRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CategoriaDTO actualizar(CategoriaDTO categoriaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Categoria categoria = categoriaDTOModelMapper.toModel(categoriaDTO);

        if (categoria.getId() == null)
            throw new BadRequestException("La actualización de categorías requiere de un ID.");

        if (categoriaRepository.findById(categoria.getId()).isEmpty())
            throw new ResourceNotFoundException("La categoría con el ID " + categoria.getId() + " no existe.");

        Optional<Categoria> categoriaMismoTitulo = categoriaRepository.findByTitulo(categoria.getTitulo());
        if (categoriaMismoTitulo.isPresent() && !categoriaMismoTitulo.get().getId().equals(categoria.getId()))
            throw new ResourceConflictException("La categoría con el título '" + categoria.getTitulo() + "' ya existe.");

        return categoriaDTOModelMapper.toDTO(categoriaRepository.save(categoria));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        if (categoria.isPresent())
        {
            if (productoRepository.findByCategoria(categoria.get()).isEmpty())
            {
                categoriaRepository.deleteById(id);
                logger.info("Se eliminó la categoría con ID " + id + ".");
            }
            else
                throw new BadRequestException("La categoría con el ID " + id + " no puede ser eliminada porque tiene productos asociados.");
        }
        else
            throw new ResourceNotFoundException("La categoría con el ID " + id + " no existe.");
    }
}
