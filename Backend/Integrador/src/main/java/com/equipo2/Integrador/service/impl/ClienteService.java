package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.ProductoDTO;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
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
public class ClienteService implements IService<ClienteDTO, Long> {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteDTOModelMapper clienteDTOModelMapper;
    private final RolRepository rolRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final ReservaRepository reservaRepository;
    private final ProductoRepository productoRepository;
    private final Logger logger = Logger.getLogger(ClienteService.class);

    @Autowired
    public ClienteService(ClienteRepository clienteRepository, UsuarioRepository usuarioRepository, ClienteDTOModelMapper clienteDTOModelMapper,
                          RolRepository rolRepository, VerificationTokenRepository verificationTokenRepository,
                          ReservaRepository reservaRepository, ProductoRepository productoRepository)
    {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.clienteDTOModelMapper = clienteDTOModelMapper;
        this.rolRepository = rolRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.reservaRepository = reservaRepository;
        this.productoRepository = productoRepository;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ClienteDTO agregar(ClienteDTO clienteDTO) throws BadRequestException, ResourceConflictException
    {
        Cliente cliente = clienteDTOModelMapper.toModel(clienteDTO);

        if (cliente.getId() != null)
            throw new BadRequestException("El registro de clientes no puede recibir un ID.");

        if (cliente.getHabilitado() == null)
            throw new BadRequestException("El atributo 'habilitado' no puede ser nulo.");

        if (usuarioRepository.findByEmail(cliente.getEmail()).isPresent())
            throw new ResourceConflictException("El usuario con el email " + cliente.getEmail() + " ya existe.");

        cliente.setRol(rolRepository.findByNombre("ROLE_CUSTOMER").orElse(null));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));

        Cliente clienteSaved = clienteRepository.save(cliente);
        logger.info("Se registró un cliente con ID " + clienteSaved.getId() + ".");

        return clienteDTOModelMapper.toDTO(clienteSaved);
    }

    @Transactional(rollbackFor = Exception.class)
    public ClienteDTO registrarClienteInhabilitado(ClienteDTO clienteDTO) throws BadRequestException, ResourceConflictException
    {
        Cliente cliente = clienteDTOModelMapper.toModel(clienteDTO);

        if (cliente.getId() != null)
            throw new BadRequestException("El registro de clientes no puede recibir un ID.");

        if (usuarioRepository.findByEmail(cliente.getEmail()).isPresent())
            throw new ResourceConflictException("El usuario con el email " + cliente.getEmail() + " ya existe.");

        cliente.setRol(rolRepository.findByNombre("ROLE_CUSTOMER").orElse(null));
        cliente.setHabilitado(false);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));

        Cliente clienteSaved = clienteRepository.save(cliente);
        logger.info("Se registró un cliente inhabilitado con ID " + clienteSaved.getId() + ".");

        return clienteDTOModelMapper.toDTO(clienteSaved);
    }

    @Override
    public ClienteDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Cliente> cliente = clienteRepository.findById(id);

        if (cliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        return clienteDTOModelMapper.toDTO(cliente.get());
    }

    public ClienteDTO buscarPorEmail(String email) throws ResourceNotFoundException
    {
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);

        if ( cliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el email " + email + " no existe.");

        return  clienteDTOModelMapper.toDTO( cliente.get());
    }

    @Override
    public List<ClienteDTO> listar()
    {
        return  clienteDTOModelMapper.toDTOList(clienteRepository.findAll());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ClienteDTO actualizar(ClienteDTO clienteDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        Cliente cliente = clienteDTOModelMapper.toModel(clienteDTO);

        if (cliente.getId() == null)
            throw new BadRequestException("La actualización de clientes requiere de un ID.");

        Optional<Cliente> clienteBuscado = clienteRepository.findById(cliente.getId());
        if (clienteBuscado.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + cliente.getId() + " no existe.");

        Optional<Usuario> usuarioMismoEmail = usuarioRepository.findByEmail(cliente.getEmail());
        if (usuarioMismoEmail.isPresent() && !usuarioMismoEmail.get().getId().equals(cliente.getId()))
            throw new ResourceConflictException("El usuario con el email " + cliente.getEmail() + " ya existe.");

        if (cliente.getHabilitado() == null)
            cliente.setHabilitado(clienteBuscado.get().getHabilitado());

        cliente.setRol(rolRepository.findByNombre("ROLE_CUSTOMER").orElse(null));
        cliente.setProductosFavoritos(clienteBuscado.get().getProductosFavoritos());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasena(passwordEncoder.encode(cliente.getContrasena()));

        return clienteDTOModelMapper.toDTO(clienteRepository.save(cliente));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        if (clienteRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        if (!reservaRepository.findByClienteId(id).isEmpty())
            throw new BadRequestException("El cliente con el ID " + id + " no puede ser eliminado porque tiene reservas asociadas.");

        clienteRepository.deleteById(id);
        logger.info("Se eliminó el cliente con ID " + id + ".");
    }

    /*public ClienteDTO getCliente(String verificationToken)
    {
        return clienteDTOModelMapper.toDTO(verificationTokenRepository.findByToken(verificationToken).getCliente());
    }*/

    @Transactional(rollbackFor = Exception.class)
    public ClienteDTO habilitarCliente(VerificationToken verificationToken)
    {
        Cliente cliente = verificationToken.getCliente();
        cliente.setHabilitado(true);
        return clienteDTOModelMapper.toDTO(clienteRepository.save(cliente));
    }

    @Transactional(rollbackFor = Exception.class)
    public void agregarFavorito(Long id, ProductoDTO productoDTO) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        if (productoDTO.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        Optional<Producto> optionalProducto = productoRepository.findById(productoDTO.getId());
        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + productoDTO.getId() + " no existe.");

        // Chequeo si el cliente ya tiene como favorito a este producto
        Optional<Producto> productoMatch = optionalCliente.get().getProductosFavoritos().stream().filter( p -> p.getId().equals(productoDTO.getId()) ).findFirst();
        System.out.println(optionalCliente.get().getProductosFavoritos());
        if (productoMatch.isPresent())
            throw new BadRequestException("El cliente ya tiene como favorito a este producto.");

        optionalCliente.get().agregarFavorito(optionalProducto.get());

        //No es necesario el save por ser el método Transactional
        //clienteRepository.save(optionalCliente.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminarFavorito(Long id, ProductoDTO productoDTO) throws ResourceNotFoundException, BadRequestException
    {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        if (productoDTO.getId() == null)
            throw new BadRequestException("El ID del producto no puede ser nulo.");

        Optional<Producto> optionalProducto = productoRepository.findById(productoDTO.getId());
        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + productoDTO.getId() + " no existe.");

        // Chequeo si el cliente tiene como favorito a este producto
        Optional<Producto> productoMatch = optionalCliente.get().getProductosFavoritos().stream().filter( p -> p.getId().equals(productoDTO.getId()) ).findFirst();
        if (productoMatch.isEmpty())
            throw new BadRequestException("El cliente no tiene como favorito a este producto.");

        optionalCliente.get().eliminarFavorito(optionalProducto.get());

        //No es necesario el save por ser el método Transactional
        //clienteRepository.save(optionalCliente.get());
    }
}