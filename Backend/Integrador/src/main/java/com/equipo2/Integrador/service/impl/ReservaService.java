package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.DTO.mapper.ReservaDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;

import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.model.Reserva;
import com.equipo2.Integrador.repository.ClienteRepository;
import com.equipo2.Integrador.repository.ProductoRepository;
import com.equipo2.Integrador.repository.ReservaRepository;
import com.equipo2.Integrador.service.IService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ReservaService implements IService<ReservaDTO, Long> {

    private final ReservaRepository reservaRepository;
    private final ReservaDTOModelMapper reservaDTOModelMapper;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(ReservaService.class);

    @Autowired
    public ReservaService(ReservaRepository reservaRepository, ReservaDTOModelMapper reservaDTOModelMapper,
                          ClienteRepository clienteRepository, ProductoRepository productoRepository)
    {
        this.reservaRepository = reservaRepository;
        this.reservaDTOModelMapper = reservaDTOModelMapper;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReservaDTO agregar(ReservaDTO reservaDTO) throws BadRequestException
    {
        // Hago estos chequeos primero para poder setear el cliente en la reserva previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = reservaDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        reservaDTO.setCliente(optionalCliente.get());

        Reserva reserva = reservaDTOModelMapper.toModel(reservaDTO);

        if (reserva.getId() != null)
            throw new BadRequestException("El registro de reservas no puede recibir un ID.");

        if (reserva.getFechaInicial() == null)
            throw new BadRequestException("La fecha inicial no puede ser nula.");

        if (reserva.getFechaFinal() == null)
            throw new BadRequestException("La fecha final no puede ser nula.");

        if (reserva.getHoraInicio() == null)
            throw new BadRequestException("La hora no puede ser nula.");

        if (reserva.getFechaInicial().isBefore(LocalDate.now()))
            throw new BadRequestException("La fecha inicial no puede ser anterior a la fecha actual.");

        if (reserva.getFechaInicial().isAfter(reserva.getFechaFinal()))
            throw new BadRequestException("La fecha inicial no puede ser posterior a la fecha final.");

        Producto producto = reserva.getProducto();
        if (producto == null)
            throw new BadRequestException("El producto no puede ser nulo.");

        if (producto.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        Optional<Producto> optionalProducto = productoRepository.findById(producto.getId());
        if (optionalProducto.isEmpty())
            throw new BadRequestException("El producto con ID " + producto.getId() + " no existe.");

        if (reservaRepository.findByProductoAndFechas(optionalProducto.get(), reserva.getFechaInicial(), reserva.getFechaFinal()).isPresent())
            throw new BadRequestException("El producto no se encuentra disponible en el rango de fechas seleccionado.");

        // Esto es puramente para que producto/cliente no tengan propiedades null en el JSON
        reserva.setProducto(optionalProducto.get());
        reserva.setCliente(optionalCliente.get());

        Reserva reservaSaved = reservaRepository.save(reserva);
        logger.info("Se registró una reserva con ID " + reservaSaved.getId() + ".");

        return reservaDTOModelMapper.toDTO(reserva);
    }

    @Override
    public ReservaDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Reserva> optionalReserva = reservaRepository.findById(id);

        if (optionalReserva.isEmpty())
            throw new ResourceNotFoundException("La reserva con el ID " + id + " no existe.");

        return reservaDTOModelMapper.toDTO(optionalReserva.get());
    }

    public List<ReservaDTO> buscarPorProductoIdYOClienteId(Long productoId, Long clienteId) throws ResourceNotFoundException
    {
        if (productoId != null && productoRepository.findById(productoId).isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + productoId + " no existe.");

        if (clienteId != null && clienteRepository.findById(clienteId).isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + clienteId + " no existe.");

        return reservaDTOModelMapper.toDTOList(reservaRepository.findByOptionalProductoIdAndOptionalClienteId(productoId, clienteId));
    }

    @Override
    public List<ReservaDTO> listar()
    {
        return reservaDTOModelMapper.toDTOList(reservaRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReservaDTO actualizar(ReservaDTO reservaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        // Hago estos chequeos primero para poder setear el cliente en la reserva previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = reservaDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        reservaDTO.setCliente(optionalCliente.get());

        Reserva reserva = reservaDTOModelMapper.toModel(reservaDTO);

        if (reserva.getId() == null)
            throw new BadRequestException("La actualización de reservas requiere de un ID.");

        if (reservaRepository.findById(reserva.getId()).isEmpty())
            throw new ResourceNotFoundException("La reserva con el ID " + reserva.getId() + " no existe.");

        if (reserva.getFechaInicial() == null)
            throw new BadRequestException("La fecha inicial no puede ser nula.");

        if (reserva.getFechaFinal() == null)
            throw new BadRequestException("La fecha final no puede ser nula.");

        if (reserva.getHoraInicio() == null)
            throw new BadRequestException("La hora no puede ser nula.");

        if (reserva.getFechaInicial().isBefore(LocalDate.now()))
            throw new BadRequestException("La fecha inicial no puede ser anterior a la fecha actual.");

        if (reserva.getFechaInicial().isAfter(reserva.getFechaFinal()))
            throw new BadRequestException("La fecha inicial no puede ser posterior a la fecha final.");

        Producto producto = reserva.getProducto();
        if (producto == null)
            throw new BadRequestException("El producto no puede ser nulo.");

        if (producto.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        Optional<Producto> optionalProducto = productoRepository.findById(producto.getId());
        if (optionalProducto.isEmpty())
            throw new BadRequestException("El producto con ID " + producto.getId() + " no existe.");

        return reservaDTOModelMapper.toDTO(reservaRepository.save(reserva));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        if (reservaRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("La reserva con el ID " + id + " no existe.");

        reservaRepository.deleteById(id);
        logger.info("Se eliminó la reserva con ID " + id + ".");
    }
}
