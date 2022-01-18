package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.PuntuacionDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.impl.PuntuacionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class PuntuacionServiceTests {

    @Autowired
    private PuntuacionService puntuacionService;
    @Autowired
    private PuntuacionRepository puntuacionRepository;
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
    private Puntuacion puntuacionAgregada;

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
        clienteAgregado = clienteRepository.save(new Cliente(
                "Ana Laura",
                "Ramírez García",
                "anaru@hotmail.com",
                "soyanaruyamolosgatos",
                rolAgregado,
                true
        ));
        puntuacionAgregada = puntuacionRepository.save(new Puntuacion(
                productoAgregado,
                clienteAgregado,
                4
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        puntuacionRepository.deleteAll();
        productoRepository.deleteAll();
        clienteRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void agregarPuntuacion() throws BadRequestException, ResourceConflictException
    {
        // Necesito un nuevo cliente o producto para poder agregar una puntuación
        Cliente clienteAgregado1 = clienteRepository.save(new Cliente(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                rolAgregado,
                false
        ));
        PuntuacionDTO puntuacionDTO = puntuacionService.agregar(new PuntuacionDTO(
                productoAgregado,
                clienteAgregado1,
                1
        ));

        Assertions.assertNotNull(puntuacionDTO.getId());
    }

    @Test
    public void buscarPuntuacion() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> puntuacionService.buscar(puntuacionAgregada.getId()) );
        PuntuacionDTO puntuacionBuscadaDTO = puntuacionService.buscar(puntuacionAgregada.getId());
        Assertions.assertNotNull(puntuacionBuscadaDTO);
        Assertions.assertEquals(puntuacionAgregada.getId(), puntuacionBuscadaDTO.getId());
    }

    @Test
    public void buscarPuntuacionInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> puntuacionService.buscar(9999L));
    }

    @Test
    public void listarTodas()
    {
        insertarPuntuaciones();

        List<PuntuacionDTO> puntuacionesDTO = puntuacionService.listar();
        Assertions.assertTrue(!puntuacionesDTO.isEmpty());
        Assertions.assertTrue(puntuacionesDTO.size() == 3);
    }

    @Test
    public void buscarPuntuacionesPorProducto() throws ResourceNotFoundException
    {
        insertarPuntuaciones();

        List<PuntuacionDTO> puntuacionesDTO = puntuacionService.buscarPorProductoIdYOClienteId(productoAgregado.getId(), null);
        Assertions.assertTrue(!puntuacionesDTO.isEmpty());
        Assertions.assertTrue(puntuacionesDTO.size() == 2);
    }

    @Test
    public void buscarPuntuacionesPorUsuario() throws ResourceNotFoundException
    {
        insertarPuntuaciones();

        List<PuntuacionDTO> puntuacionesDTO = puntuacionService.buscarPorProductoIdYOClienteId(null, clienteAgregado.getId());
        Assertions.assertTrue(!puntuacionesDTO.isEmpty());
        Assertions.assertTrue(puntuacionesDTO.size() == 2);
    }

    @Test
    public void buscarPuntuacionesPorProductoYUsuario() throws ResourceNotFoundException
    {
        insertarPuntuaciones();

        List<PuntuacionDTO> puntuacionesDTO = puntuacionService.buscarPorProductoIdYOClienteId(productoAgregado.getId(), clienteAgregado.getId());
        Assertions.assertTrue(!puntuacionesDTO.isEmpty());
        Assertions.assertTrue(puntuacionesDTO.size() == 1);
    }

    @Test
    public void actualizarPuntuacion() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        PuntuacionDTO puntuacionBuscadaDTO = puntuacionService.buscar(puntuacionAgregada.getId());
        Assertions.assertNotNull(puntuacionBuscadaDTO);
        Assertions.assertEquals(4, puntuacionBuscadaDTO.getPuntos());
        Assertions.assertEquals(puntuacionAgregada.getPuntos(), puntuacionBuscadaDTO.getPuntos());

        puntuacionBuscadaDTO.setPuntos(3);
        PuntuacionDTO puntuacionActualizadaDTO = puntuacionService.actualizar(puntuacionBuscadaDTO);
        PuntuacionDTO puntuacionActualizadaBuscadaDTO = puntuacionService.buscar(puntuacionActualizadaDTO.getId());
        Assertions.assertNotNull(puntuacionActualizadaBuscadaDTO);
        Assertions.assertEquals(3, puntuacionActualizadaBuscadaDTO.getPuntos());
        Assertions.assertEquals(puntuacionBuscadaDTO.getPuntos(), puntuacionActualizadaBuscadaDTO.getPuntos());
    }

    @Test
    public void eliminarPuntuacion() throws ResourceNotFoundException, BadRequestException
    {
        puntuacionService.eliminar(puntuacionAgregada.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> puntuacionService.buscar(puntuacionAgregada.getId()));
    }

    private void insertarPuntuaciones()
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
        puntuacionRepository.save(new Puntuacion(
                productoAgregado,
                clienteAgregado1,
                2
        ));
        puntuacionRepository.save(new Puntuacion(
                productoAgregado1,
                clienteAgregado,
                3
        ));
    }
}