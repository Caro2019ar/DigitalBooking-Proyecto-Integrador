package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.PuntuacionDTO;
import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.DTO.mapper.PuntuacionDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.model.Puntuacion;
import com.equipo2.Integrador.model.Reserva;
import com.equipo2.Integrador.repository.ClienteRepository;
import com.equipo2.Integrador.repository.PuntuacionRepository;
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
public class PuntuacionService implements IService<PuntuacionDTO, Long> {

    private final PuntuacionRepository puntuacionRepository;
    private final PuntuacionDTOModelMapper puntuacionDTOModelMapper;
    private final ProductoRepository productoRepository;
    private final ClienteRepository clienteRepository;
    private final Logger logger = Logger.getLogger(PuntuacionService.class);

    @Autowired
    public PuntuacionService(PuntuacionRepository puntuacionRepository, PuntuacionDTOModelMapper puntuacionDTOModelMapper,
                             ProductoRepository productoRepository, ClienteRepository clienteRepository)
    {
        this.puntuacionRepository = puntuacionRepository;
        this.puntuacionDTOModelMapper = puntuacionDTOModelMapper;
        this.productoRepository = productoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PuntuacionDTO agregar(PuntuacionDTO puntuacionDTO) throws BadRequestException, ResourceConflictException
    {
        // Hago estos chequeos primero para poder setear el cliente en la puntuación previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = puntuacionDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        puntuacionDTO.setCliente(optionalCliente.get());

        Puntuacion puntuacion = puntuacionDTOModelMapper.toModel(puntuacionDTO);

        if (puntuacion.getId() != null)
            throw new BadRequestException("El registro de puntuaciones no puede recibir un ID.");

        Producto producto = puntuacion.getProducto();
        if (producto == null)
            throw new BadRequestException("El producto no puede ser nulo.");

        if (producto.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        Optional<Producto> optionalProducto = productoRepository.findById(producto.getId());
        if (optionalProducto.isEmpty())
            throw new BadRequestException("El producto con ID " + producto.getId() + " no existe.");

        if (puntuacion.getPuntos() < 1 || puntuacion.getPuntos() > 5)
            throw new BadRequestException("La valoración debe ser un número entero entre 1 y 5.");

        // Chequeo si el cliente ya tiene una puntuación registrada en este producto
        Optional<Puntuacion> puntuacionMatch = optionalProducto.get().getPuntuaciones().stream().filter( p -> p.getCliente().getId().equals(optionalCliente.get().getId()) ).findFirst();
        if (puntuacionMatch.isPresent())
            throw new BadRequestException("El cliente ya tiene una votación registrada en este producto.");

        // Esto es puramente para que producto/cliente no tengan propiedades null en el JSON
        puntuacion.setProducto(optionalProducto.get());
        puntuacion.setCliente(optionalCliente.get());

        Puntuacion puntuacionSaved = puntuacionRepository.save(puntuacion);
        logger.info("Se registró una puntuación con ID " + puntuacionSaved.getId() + ".");

        return puntuacionDTOModelMapper.toDTO(puntuacionSaved);
    }

    @Override
    public PuntuacionDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Puntuacion> optionalPuntuacion = puntuacionRepository.findById(id);

        if (optionalPuntuacion.isEmpty())
            throw new ResourceNotFoundException("La puntuación con el ID " + id + " no existe.");

        return puntuacionDTOModelMapper.toDTO(optionalPuntuacion.get());
    }

    public List<PuntuacionDTO> buscarPorProductoIdYOClienteId(Long productoId, Long clienteId) throws ResourceNotFoundException
    {
        if (productoId != null && productoRepository.findById(productoId).isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + productoId + " no existe.");

        if (clienteId != null && clienteRepository.findById(clienteId).isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + clienteId + " no existe.");

        return puntuacionDTOModelMapper.toDTOList(puntuacionRepository.findByOptionalProductoIdAndOptionalClienteId(productoId, clienteId));
    }

    @Override
    public List<PuntuacionDTO> listar()
    {
        return puntuacionDTOModelMapper.toDTOList(puntuacionRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PuntuacionDTO actualizar(PuntuacionDTO puntuacionDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        // Hago estos chequeos primero para poder setear el cliente en la puntuación previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = puntuacionDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        puntuacionDTO.setCliente(optionalCliente.get());

        Puntuacion puntuacion = puntuacionDTOModelMapper.toModel(puntuacionDTO);

        if (puntuacion.getId() == null)
            throw new BadRequestException("La actualización de puntuaciones requiere de un ID.");

        if (puntuacionRepository.findById(puntuacion.getId()).isEmpty())
            throw new ResourceNotFoundException("La puntuación con el ID " + puntuacion.getId() + " no existe.");

        Producto producto = puntuacion.getProducto();
        if (producto == null)
            throw new BadRequestException("El producto no puede ser nulo.");

        if (producto.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        Optional<Producto> optionalProducto = productoRepository.findById(producto.getId());
        if (optionalProducto.isEmpty())
            throw new BadRequestException("El producto con ID " + producto.getId() + " no existe.");

        if (puntuacion.getPuntos() < 1 || puntuacion.getPuntos() > 5)
            throw new BadRequestException("La valoración debe ser un número entero entre 1 y 5.");

        // Chequeo si el cliente tiene una puntuación registrada en este producto para poder actualizarla
        Optional<Puntuacion> puntuacionMatch = optionalProducto.get().getPuntuaciones().stream().filter( p -> p.getCliente().getId().equals(optionalCliente.get().getId()) ).findFirst();
        if (puntuacionMatch.isEmpty())
            throw new BadRequestException("El cliente no tiene una votación registrada en este producto.");

        // Esto es puramente para que producto/cliente no tengan propiedades null en el JSON
        puntuacion.setProducto(optionalProducto.get());
        puntuacion.setCliente(optionalCliente.get());

        return puntuacionDTOModelMapper.toDTO(puntuacionRepository.save(puntuacion));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Puntuacion> puntuacion = puntuacionRepository.findById(id);
        if (puntuacion.isEmpty())
            throw new ResourceNotFoundException("La puntuación con el ID " + id + " no existe.");

        puntuacionRepository.deleteById(id);
        logger.info("Se eliminó la puntuación con ID " + id + ".");
    }
}