package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.ImagenDTO;
import com.equipo2.Integrador.DTO.mapper.ImagenDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Imagen;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.repository.ImagenRepository;
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
public class ImagenService implements IService<ImagenDTO, Long> {

    private final ImagenRepository imagenRepository;
    private final ImagenDTOModelMapper imagenDTOModelMapper;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(ImagenService.class);

    @Autowired
    public ImagenService(ImagenRepository imagenRepository, ImagenDTOModelMapper imagenDTOModelMapper, ProductoRepository productoRepository)
    {
        this.imagenRepository = imagenRepository;
        this.imagenDTOModelMapper = imagenDTOModelMapper;
        this.productoRepository = productoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ImagenDTO agregar(ImagenDTO imagenDTO) throws BadRequestException
    {
        Imagen imagen = imagenDTOModelMapper.toModel(imagenDTO);

        if (imagen.getId() != null)
            throw new BadRequestException("El registro de imágenes no puede recibir un ID.");

        Producto producto = imagen.getProducto();
        if (producto == null)
            throw new BadRequestException("El producto no puede ser nulo.");

        if (producto.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        if (productoRepository.findById(producto.getId()).isEmpty())
            throw new BadRequestException("El producto con ID " + producto.getId() + " no existe.");

        Imagen imagenSaved = imagenRepository.save(imagen);
        logger.info("Se registró una imagen con ID " + imagenSaved.getId() + ".");

        return imagenDTOModelMapper.toDTO(imagenSaved);
    }

    @Override
    public ImagenDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Imagen> optionalImagen = imagenRepository.findById(id);

        if (optionalImagen.isEmpty())
            throw new ResourceNotFoundException("La imagen con el ID " + id + " no existe.");

        return imagenDTOModelMapper.toDTO(optionalImagen.get());
    }

    @Override
    public List<ImagenDTO> listar()
    {
        return imagenDTOModelMapper.toDTOList(imagenRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ImagenDTO actualizar(ImagenDTO imagenDTO) throws BadRequestException, ResourceNotFoundException
    {
        Imagen imagen = imagenDTOModelMapper.toModel(imagenDTO);

        if (imagen.getId() == null)
            throw new BadRequestException("La actualización de imágenes requiere de un ID.");

        if (imagenRepository.findById(imagen.getId()).isEmpty())
            throw new ResourceNotFoundException("La imagen con el ID " + imagen.getId() + " no existe.");

        Long productoID = imagen.getProducto().getId();
        if (productoID != null && productoRepository.findById(productoID).isEmpty())
            throw new BadRequestException("El producto con ID " + productoID + " no existe.");

        return imagenDTOModelMapper.toDTO(imagenRepository.save(imagen));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Imagen> imagen = imagenRepository.findById(id);
        if (imagen.isPresent())
        {
            imagenRepository.deleteById(id);
            logger.info("Se eliminó la imagen con ID " + id + ".");
        }
        else
            throw new ResourceNotFoundException("La imagen con el ID " + id + " no existe.");
    }
}
