package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.AdminDTO;
import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.impl.ClienteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class ClienteServiceTests {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProductoDTOModelMapper productoDTOModelMapper;

    private Cliente clienteAgregado;
    private Rol rolAgregado;
    private VerificationToken verificationTokenAgregado;
    private Ciudad ciudadAgregada;
    private Categoria categoriaAgregada;
    private Producto productoAgregado;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolAgregado = rolRepository.save(new Rol("ROLE_CUSTOMER"));
        clienteAgregado = clienteRepository.save(new Cliente(
                "Daniel",
                "Chaulet",
                "daniel_chaulet@gmail.com",
                new BCryptPasswordEncoder().encode("soydaniel"),
                rolAgregado,
                false
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        verificationTokenRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void agregarCliente() throws BadRequestException, ResourceConflictException
    {
        ClienteDTO clienteDTO = clienteService.agregar(new ClienteDTO(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                true
        ));
        Assertions.assertNotNull(clienteDTO.getId());
    }

    @Test
    public void agregarClienteEmailRepetido()
    {
        Assertions.assertThrows(ResourceConflictException.class, () ->
                clienteService.agregar(new ClienteDTO(
                        "Soy",
                        "Repetido",
                        "daniel_chaulet@gmail.com",
                        new BCryptPasswordEncoder().encode("soyrepetido"),
                        false
                )));
    }

    @Test
    public void buscarCliente() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow(() -> clienteService.buscar(clienteAgregado.getId()));
        ClienteDTO clienteBuscadoDTO = clienteService.buscar(clienteAgregado.getId());
        Assertions.assertNotNull(clienteBuscadoDTO);
        Assertions.assertEquals(clienteAgregado.getId(), clienteBuscadoDTO.getId());
    }

    @Test
    public void buscarClientePorEmail() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow(() -> clienteService.buscarPorEmail(clienteAgregado.getEmail()));
        ClienteDTO clienteBuscadoDTO = clienteService.buscarPorEmail(clienteAgregado.getEmail());
        Assertions.assertNotNull(clienteBuscadoDTO);
        Assertions.assertEquals(clienteAgregado.getId(), clienteBuscadoDTO.getId());
    }

    @Test
    public void buscarClienteInexistente() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> clienteService.buscar(9999L));
    }

    @Test
    public void listarTodos() throws BadRequestException, ResourceConflictException
    {
        clienteService.agregar(new ClienteDTO(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                false
        ));
        clienteService.agregar(new ClienteDTO(
                "Nuevo_2",
                "Cliente_2",
                "nuevo_2@cliente_2.com",
                "soynuevo_2",
                true
        ));
        List<ClienteDTO> clienteesDTO = clienteService.listar();
        Assertions.assertTrue(!clienteesDTO.isEmpty());
        Assertions.assertTrue(clienteesDTO.size() == 3);
    }

    @Test
    public void actualizarCliente() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        ClienteDTO clienteBuscadoDTO = clienteService.buscar(clienteAgregado.getId());
        Assertions.assertNotNull(clienteBuscadoDTO);
        Assertions.assertEquals("daniel_chaulet@gmail.com", clienteBuscadoDTO.getEmail());

        clienteBuscadoDTO.setEmail("mi_nuevo_email@gmail.com");
        clienteBuscadoDTO.setContrasena("nueva_contrasena");    // Si no queda en null y tira error
        ClienteDTO clienteActualizadoDTO = clienteService.actualizar(clienteBuscadoDTO);
        ClienteDTO clienteActualizadoBuscadoDTO = clienteService.buscar(clienteActualizadoDTO.getId());
        Assertions.assertNotNull(clienteActualizadoBuscadoDTO);
        Assertions.assertEquals("mi_nuevo_email@gmail.com", clienteActualizadoBuscadoDTO.getEmail());
    }

    @Test
    public void eliminarCliente() throws ResourceNotFoundException, BadRequestException {
        clienteService.eliminar(clienteAgregado.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> clienteService.buscar(clienteAgregado.getId()));
    }

    @Test
    public void habilitarCliente()
    {
        verificationTokenAgregado =  verificationTokenRepository.save(new VerificationToken("nuevo_token", clienteAgregado));

        Assertions.assertFalse(clienteAgregado.getHabilitado());
        clienteService.habilitarCliente(verificationTokenAgregado);
        Assertions.assertTrue(clienteAgregado.getHabilitado());
    }

    @Transactional  // Para que me traiga la colección de favoritos
    @Test
    public void agregarFavorito() throws ResourceNotFoundException, BadRequestException
    {
        ciudadAgregada = ciudadRepository.save(new Ciudad("Mendoza", "Argentina"));
        categoriaAgregada = categoriaRepository.save(new Categoria("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        productoAgregado = productoRepository.save(new Producto(
                "Producto-001",
                "Great hotel, the best of the best.",
                ciudadAgregada,
                categoriaAgregada,
                null,
                null,
                -32.888557,
                -68.850720,
                "Av. Rivadavia 1280",
                null
        ));

        Assertions.assertTrue(clienteAgregado.getProductosFavoritos().isEmpty());

        clienteService.agregarFavorito(clienteAgregado.getId(), productoDTOModelMapper.toDTO(productoAgregado));
        Cliente clienteBuscado = clienteRepository.findById(clienteAgregado.getId()).orElse(null);

        Assertions.assertTrue(clienteBuscado.getProductosFavoritos().size() == 1);
        Assertions.assertEquals(productoAgregado.getId(), clienteBuscado.getProductosFavoritos().get(0).getId());
    }

    @Transactional  // Para que me traiga la colección de favoritos
    @Test
    public void eliminarFavorito() throws ResourceNotFoundException, BadRequestException
    {
        ciudadAgregada = ciudadRepository.save(new Ciudad("Mendoza", "Argentina"));
        categoriaAgregada = categoriaRepository.save(new Categoria("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        productoAgregado = productoRepository.save(new Producto(
                "Producto-001",
                "Great hotel, the best of the best.",
                ciudadAgregada,
                categoriaAgregada,
                null,
                null,
                -32.888557,
                -68.850720,
                "Av. Rivadavia 1280",
                null
        ));

        Assertions.assertTrue(clienteAgregado.getProductosFavoritos().isEmpty());

        clienteService.agregarFavorito(clienteAgregado.getId(), productoDTOModelMapper.toDTO(productoAgregado));
        Cliente clienteBuscado = clienteRepository.findById(clienteAgregado.getId()).orElse(null);

        Assertions.assertTrue(clienteBuscado.getProductosFavoritos().size() == 1);
        Assertions.assertEquals(productoAgregado.getId(), clienteBuscado.getProductosFavoritos().get(0).getId());

        clienteService.eliminarFavorito(clienteAgregado.getId(), productoDTOModelMapper.toDTO(productoAgregado));
        clienteBuscado = clienteRepository.findById(clienteAgregado.getId()).orElse(null);

        Assertions.assertTrue(clienteBuscado.getProductosFavoritos().isEmpty());
    }
}