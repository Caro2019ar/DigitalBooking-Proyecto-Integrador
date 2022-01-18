package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.model.Reserva;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.impl.CategoriaService;
import com.equipo2.Integrador.service.impl.CiudadService;
import com.equipo2.Integrador.service.impl.ReservaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class ReservaServiceTests {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    private Ciudad ciudadAgregada;
    private Categoria categoriaAgregada;
    private Producto productoAgregado;
    private Rol rolAgregado;
    private Cliente clienteAgregado;
    private Reserva reservaAgregada;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
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

        rolAgregado = rolRepository.save(new Rol("ROLE_TEST"));
        clienteAgregado = clienteRepository.save(new Cliente("Ana Laura", "Ramírez García", "anaru@hotmail.com", "soyanaruyamolosgatos", rolAgregado, true));

        reservaAgregada = reservaRepository.save(new Reserva(
                LocalTime.of(9, 0),
                LocalDate.of(2022, 12, 3),
                LocalDate.of(2022, 12, 8),
                productoAgregado,
                clienteAgregado,
                "Rosario",
                "Nunca tuve covid y no planeo tenerlo. Si me lo agarro, me la banco.",
                false
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        reservaRepository.deleteAll();
        productoRepository.deleteAll();
        clienteRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void agregarReserva() throws BadRequestException, ResourceConflictException
    {
        ReservaDTO reservaDTO = reservaService.agregar(new ReservaDTO(
                LocalTime.of(15, 0),
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 14),
                productoAgregado,
                clienteAgregado,
                "Santiago de Chile",
                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                false
        ));
        Assertions.assertNotNull(reservaDTO.getId());
    }

    @Test
    public void buscarReserva() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> reservaService.buscar(reservaAgregada.getId()) );
        ReservaDTO reservaBuscadaDTO = reservaService.buscar(reservaAgregada.getId());
        Assertions.assertNotNull(reservaBuscadaDTO);
        Assertions.assertEquals(reservaAgregada.getId(), reservaBuscadaDTO.getId());
    }

    @Test
    public void buscarReservaInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reservaService.buscar(9999L));
    }

    @Test
    public void buscarPorProductoId() throws ResourceNotFoundException, BadRequestException
    {
        insertarReservas();

        Assertions.assertDoesNotThrow( () -> reservaService.buscarPorProductoIdYOClienteId(productoAgregado.getId(), null));
        List<ReservaDTO> reservaBuscadaDTO = reservaService.buscarPorProductoIdYOClienteId(productoAgregado.getId(), null);
        Assertions.assertTrue(!reservaBuscadaDTO.isEmpty());
        Assertions.assertTrue(reservaBuscadaDTO.size() == 2);
    }

    @Test
    public void buscarPorUsuarioId() throws ResourceNotFoundException, BadRequestException
    {
        insertarReservas();

        Assertions.assertDoesNotThrow( () -> reservaService.buscarPorProductoIdYOClienteId(null, clienteAgregado.getId()));
        List<ReservaDTO> reservaBuscadaDTO = reservaService.buscarPorProductoIdYOClienteId(null, clienteAgregado.getId());
        Assertions.assertTrue(!reservaBuscadaDTO.isEmpty());
        Assertions.assertTrue(reservaBuscadaDTO.size() == 2);
    }

    @Test
    public void buscarPorProductoIdYUsuarioId() throws ResourceNotFoundException, BadRequestException
    {
        insertarReservas();

        Assertions.assertDoesNotThrow( () -> reservaService.buscarPorProductoIdYOClienteId(productoAgregado.getId(), clienteAgregado.getId()));
        List<ReservaDTO> reservaBuscadaDTO = reservaService.buscarPorProductoIdYOClienteId(productoAgregado.getId(), clienteAgregado.getId());
        Assertions.assertTrue(!reservaBuscadaDTO.isEmpty());
        Assertions.assertTrue(reservaBuscadaDTO.size() == 1);
    }

    @Test
    public void listarTodas() throws BadRequestException, ResourceConflictException
    {
        insertarReservas();

        List<ReservaDTO> reservas = reservaService.listar();
        Assertions.assertTrue(!reservas.isEmpty());
        Assertions.assertTrue(reservas.size() == 3);
    }

    @Test
    public void actualizarReserva() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        ReservaDTO reservaBuscadaDTO = reservaService.buscar(reservaAgregada.getId());
        Assertions.assertNotNull(reservaBuscadaDTO);
        Assertions.assertEquals(LocalDate.of(2022, 12, 8), reservaBuscadaDTO.getFechaFinal());
        Assertions.assertEquals(reservaAgregada.getFechaFinal(), reservaBuscadaDTO.getFechaFinal());

        reservaBuscadaDTO.setFechaFinal(LocalDate.of(2022, 12, 15));
        ReservaDTO reservaActualizadaDTO = reservaService.actualizar(reservaBuscadaDTO);
        ReservaDTO reservaActualizadaBuscadaDTO = reservaService.buscar(reservaActualizadaDTO.getId());
        Assertions.assertNotNull(reservaActualizadaBuscadaDTO);
        Assertions.assertEquals(LocalDate.of(2022, 12, 15), reservaActualizadaBuscadaDTO.getFechaFinal());
        Assertions.assertEquals(reservaBuscadaDTO.getFechaFinal(), reservaActualizadaBuscadaDTO.getFechaFinal());
    }

    @Test
    public void eliminarReserva() throws ResourceNotFoundException, BadRequestException
    {
        reservaService.eliminar(reservaAgregada.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reservaService.buscar(reservaAgregada.getId()));
    }

    private void insertarReservas() throws BadRequestException
    {
        Producto productoAgregado1 = productoRepository.save(new Producto(
                "Producto-002",
                "Good hotel, pretty good.",
                ciudadAgregada,
                categoriaAgregada,
                null,
                null,
                -0.204205,
                -78.487900,
                "Paraguay 243",
                null
        ));
        Cliente clienteAgregado1 = clienteRepository.save(new Cliente(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                rolAgregado,
                false
        ));

        reservaService.agregar(new ReservaDTO(
                LocalTime.of(18, 0),
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 14),
                productoAgregado1,
                clienteAgregado,
                "Santiago de Chile",
                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                false
        ));
        reservaService.agregar(new ReservaDTO(
                LocalTime.of(23, 0),
                LocalDate.of(2022, 12, 21),
                LocalDate.of(2022, 12, 27),
                productoAgregado,
                clienteAgregado1,
                "Pilar",
                "Tuve covid una vez. No la pasé tan mal, pero ahora estoy más tranquila porque ya estoy vacunada.",
                true
        ));
    }
}