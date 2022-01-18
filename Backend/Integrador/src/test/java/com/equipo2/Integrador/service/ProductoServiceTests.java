package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.ProductoDTO;
import com.equipo2.Integrador.DTO.PuntuacionDTO;
import com.equipo2.Integrador.DTO.mapper.ImagenDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.impl.CategoriaService;
import com.equipo2.Integrador.service.impl.CiudadService;
import com.equipo2.Integrador.service.impl.ProductoService;
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
public class ProductoServiceTests {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ImagenDTOModelMapper imagenDTOModelMapper;

    private Ciudad ciudadAgregada;
    private Categoria categoriaAgregada;
    private Producto productoAgregado;
    private Rol rolAgregado;
    private Cliente clienteAgregado;

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
    }

    @AfterEach
    public void cleanUp()
    {
        reservaRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void agregarProducto() throws BadRequestException, ResourceConflictException
    {
        ProductoDTO productoDTO = productoService.agregar(new ProductoDTO(
                "Producto-002",
                "Good hotel, pretty good.",
                ciudadRepository.save(new Ciudad("Buenos Aires", "Argentina")),
                categoriaRepository.save(new Categoria("Hotel de Playa", "Hoteles cercanos a balnearios", "http://www.hotel-playa.com")),
                null,
                null,
                -0.204205,
                -78.487900,
                "Paraguay 243",
                null
        ));
        Assertions.assertNotNull(productoDTO.getId());
    }

    @Test
    public void buscarProducto() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> productoService.buscar(productoAgregado.getId()) );
        ProductoDTO productoBuscadoDTO = productoService.buscar(productoAgregado.getId());
        Assertions.assertNotNull(productoBuscadoDTO);
        Assertions.assertEquals(productoAgregado.getId(), productoBuscadoDTO.getId());
    }

    @Test
    public void buscarProductoInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productoService.buscar(9999L));
    }

    @Test
    public void listarTodos() throws BadRequestException, ResourceConflictException
    {
        insertarProductos();

        List<ProductoDTO> productos = productoService.listar();
        Assertions.assertTrue(!productos.isEmpty());
        Assertions.assertTrue(productos.size() == 3);
    }

    // La notación @Transactional es para que no tire error al inicializar la lazy collection de Imágenes
    @Transactional
    @Test
    public void actualizarProducto() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        ProductoDTO productoBuscadoDTO = productoService.buscar(productoAgregado.getId());
        Assertions.assertNotNull(productoBuscadoDTO);
        Assertions.assertEquals("Mendoza", productoBuscadoDTO.getCiudad().getNombre());
        Assertions.assertEquals(productoAgregado.getCiudad(), productoBuscadoDTO.getCiudad());

        productoBuscadoDTO.setCiudad(ciudadRepository.save(new Ciudad("Trelew", "Argentina")));
        ProductoDTO productoActualizado = productoService.actualizar(productoBuscadoDTO);
        ProductoDTO productoActualizadoBuscado = productoService.buscar(productoActualizado.getId());
        Assertions.assertNotNull(productoActualizadoBuscado);
        Assertions.assertEquals("Trelew", productoActualizadoBuscado.getCiudad().getNombre());
        Assertions.assertEquals(productoBuscadoDTO.getCiudad(), productoActualizadoBuscado.getCiudad());
    }

    @Test
    public void eliminarProducto() throws ResourceNotFoundException, BadRequestException
    {
        productoService.eliminar(productoAgregado.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productoService.buscar(productoAgregado.getId()));
    }

    @Transactional
    @Test
    public void agregarImagenes() throws ResourceNotFoundException, BadRequestException
    {
        ProductoDTO productoBuscadoDTO = productoService.buscar(productoAgregado.getId());
        Assertions.assertNotNull(productoBuscadoDTO);
        Assertions.assertTrue(productoBuscadoDTO.getImagenes().isEmpty());

        List<Imagen> listaImagenesAInsertar = new ArrayList<>();
        listaImagenesAInsertar.add(new Imagen("Imagen-002", "http://www.imagen002.com", null));
        listaImagenesAInsertar.add(new Imagen("Imagen-003", "http://www.imagen003.com", null));

        ProductoDTO productoBuscadoConImagenes = productoService.agregarImagenes(productoBuscadoDTO.getId(), imagenDTOModelMapper.toDTOList(listaImagenesAInsertar));
        Assertions.assertFalse(productoBuscadoConImagenes.getImagenes().isEmpty());
        Assertions.assertTrue(productoBuscadoConImagenes.getImagenes().size() == 2);
        Assertions.assertEquals("Imagen-002", productoBuscadoConImagenes.getImagenes().get(0).getTitulo());
        Assertions.assertEquals("Imagen-003", productoBuscadoConImagenes.getImagenes().get(1).getTitulo());
    }

    @Transactional
    @Test
    public void eliminarImagenes() throws ResourceNotFoundException, BadRequestException
    {
        /* Agrego imágenes */

        ProductoDTO productoBuscadoDTO = productoService.buscar(productoAgregado.getId());
        Assertions.assertNotNull(productoBuscadoDTO);
        Assertions.assertTrue(productoBuscadoDTO.getImagenes().isEmpty());

        List<Imagen> listaImagenesAInsertar = new ArrayList<>();
        listaImagenesAInsertar.add(new Imagen("Imagen-002", "http://www.imagen002.com", null));
        listaImagenesAInsertar.add(new Imagen("Imagen-003", "http://www.imagen003.com", null));

        ProductoDTO productoBuscadoConImagenes = productoService.agregarImagenes(productoBuscadoDTO.getId(), imagenDTOModelMapper.toDTOList(listaImagenesAInsertar));
        Assertions.assertFalse(productoBuscadoConImagenes.getImagenes().isEmpty());
        Assertions.assertTrue(productoBuscadoConImagenes.getImagenes().size() == 2);
        Assertions.assertEquals("Imagen-002", productoBuscadoConImagenes.getImagenes().get(0).getTitulo());
        Assertions.assertEquals("Imagen-003", productoBuscadoConImagenes.getImagenes().get(1).getTitulo());


        /* Elimino imágenes */

        Imagen imagenABorrar = productoBuscadoConImagenes.getImagenes().get(0);
        Imagen imagenAMantener = productoBuscadoConImagenes.getImagenes().get(1);
        List<Imagen> listaImagenesABorrar = new ArrayList<>();
        listaImagenesABorrar.add(imagenABorrar);

        ProductoDTO productoBuscadoSinImagenes = productoService.eliminarImagenes(productoBuscadoDTO.getId(), imagenDTOModelMapper.toDTOList(listaImagenesABorrar));
        Assertions.assertTrue(productoBuscadoSinImagenes.getImagenes().size() == 1);
        Assertions.assertTrue(productoBuscadoSinImagenes.getImagenes().contains(imagenAMantener));
        Assertions.assertFalse(productoBuscadoSinImagenes.getImagenes().contains(imagenABorrar));
    }

    @Test
    public void buscarPorCiudadYOCategoria() throws BadRequestException
    {
        insertarProductos();

        List<ProductoDTO> productosPorCiudad = productoService.buscarPorCiudadYOCategoria("Mendoza", null);
        Assertions.assertTrue(productosPorCiudad.size() == 2);

        List<ProductoDTO> productosPorCategoria = productoService.buscarPorCiudadYOCategoria(null, "Hotel Urbano");
        Assertions.assertTrue(productosPorCategoria.size() == 2);

        List<ProductoDTO> productosPorCiudadYCategoria = productoService.buscarPorCiudadYOCategoria("Mendoza", "Hotel Urbano");
        Assertions.assertTrue(productosPorCiudadYCategoria.size() == 1);
        Assertions.assertEquals(productoAgregado.getId(), productosPorCiudadYCategoria.get(0).getId());
    }

    @Test
    public void buscarPorCiudadYFechas() throws BadRequestException
    {
        reservaRepository.save(new Reserva(
                LocalTime.of(9, 0),
                LocalDate.of(2021, 12, 3),
                LocalDate.of(2021, 12, 8),
                productoAgregado,
                clienteAgregado,
                "Rosario",
                "Nunca tuve covid y no planeo tenerlo. Si me lo agarro, me la banco.",
                false
        ));
        reservaRepository.save(new Reserva(
                LocalTime.of(14, 0),
                LocalDate.of(2021, 12, 12),
                LocalDate.of(2021, 12, 19),
                productoAgregado,
                clienteAgregado,
                "Santiago de Chile",
                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                false
        ));
        Producto productoNuevo = productoRepository.save(new Producto(
                "Producto-002",
                "Good hotel, pretty good.",
                ciudadAgregada,
                categoriaRepository.save(new Categoria("Hotel de Playa", "Hoteles cercanos a balnearios", "http://www.hotel-playa.com")),
                null,
                null,
                -0.204205,
                -78.487900,
                "Paraguay 243",
                null
        ));
        reservaRepository.save(new Reserva(
                LocalTime.of(14, 0),
                LocalDate.of(2021, 12, 7),
                LocalDate.of(2021, 12, 16),
                productoNuevo,
                clienteAgregado,
                "Pilar",
                "Tuve covid una vez. No la pasé tan mal, pero ahora estoy más tranquila porque ya estoy vacunada.",
                true
        ));

        List<ProductoDTO> productosPorCiudadYFechas = productoService.buscarPorCiudadYFechas("Mendoza", LocalDate.of(2021, 12, 2), LocalDate.of(2021, 12, 20));
        Assertions.assertTrue(productosPorCiudadYFechas.isEmpty());

        productosPorCiudadYFechas = productoService.buscarPorCiudadYFechas("Mendoza", LocalDate.of(2021, 12, 10), LocalDate.of(2021, 12, 12));
        Assertions.assertTrue(productosPorCiudadYFechas.isEmpty());

        productosPorCiudadYFechas = productoService.buscarPorCiudadYFechas("Mendoza", LocalDate.of(2021, 12, 4), LocalDate.of(2021, 12, 6));
        Assertions.assertTrue(productosPorCiudadYFechas.size() == 1);
        Assertions.assertEquals(productoNuevo.getId(), productosPorCiudadYFechas.get(0).getId());

        productosPorCiudadYFechas = productoService.buscarPorCiudadYFechas("Mendoza", LocalDate.of(2021, 12, 9), LocalDate.of(2021, 12, 10));
        Assertions.assertTrue(productosPorCiudadYFechas.size() == 1);
        Assertions.assertEquals(productoAgregado.getId(), productosPorCiudadYFechas.get(0).getId());

        productosPorCiudadYFechas = productoService.buscarPorCiudadYFechas("Mendoza", LocalDate.of(2021, 12, 1), LocalDate.of(2021, 12, 2));
        Assertions.assertTrue(productosPorCiudadYFechas.size() == 2);
    }

    @Test
    public void listarFechasNoDisponibleOrdenado() throws ResourceNotFoundException
    {
        reservaRepository.save(new Reserva(
                LocalTime.of(9, 0),
                LocalDate.of(2021, 12, 3),
                LocalDate.of(2021, 12, 8),
                productoAgregado,
                clienteAgregado,
                "Rosario",
                "Nunca tuve covid y no planeo tenerlo. Si me lo agarro, me la banco.",
                false
        ));
        reservaRepository.save(new Reserva(
                LocalTime.of(14, 0),
                LocalDate.of(2021, 12, 12),
                LocalDate.of(2021, 12, 19),
                productoAgregado,
                clienteAgregado,
                "Santiago de Chile",
                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                false
        ));

        List<LocalDate> listaFechas = productoService.listarFechasNoDisponibleOrdenado(productoAgregado.getId());
        Assertions.assertTrue(!listaFechas.isEmpty());
        Assertions.assertTrue(listaFechas.size() == 14);
        Assertions.assertEquals(LocalDate.of(2021, 12, 3), listaFechas.get(0));
        Assertions.assertEquals(LocalDate.of(2021, 12, 19), listaFechas.get(listaFechas.size()-1));
    }

    @Transactional
    @Test
    public void esFavoritoDelUsuario() throws BadRequestException, ResourceNotFoundException
    {
        insertarProductos();
        clienteAgregado.agregarFavorito(productoRepository.findAll().get(0));
        clienteAgregado.agregarFavorito(productoRepository.findAll().get(1));

        Assertions.assertTrue(productoService.esFavoritoDelUsuario(productoRepository.findAll().get(0).getId(), clienteAgregado.getId()));
        Assertions.assertTrue(productoService.esFavoritoDelUsuario(productoRepository.findAll().get(1).getId(), clienteAgregado.getId()));
        Assertions.assertFalse(productoService.esFavoritoDelUsuario(productoRepository.findAll().get(2).getId(), clienteAgregado.getId()));
    }

    @Transactional
    @Test
    public void obtenerFavoritosByUsuario() throws BadRequestException, ResourceNotFoundException
    {
        insertarProductos();
        clienteAgregado.agregarFavorito(productoRepository.findAll().get(0));
        clienteAgregado.agregarFavorito(productoRepository.findAll().get(1));

        List<ProductoDTO> productos = productoService.obtenerFavoritosByUsuario(clienteAgregado.getId());
        Assertions.assertTrue(!productos.isEmpty());
        Assertions.assertTrue(productos.size() == 2);
    }

    @Transactional
    @Test
    public void obtenerFavoritosIdByUsuario() throws BadRequestException, ResourceNotFoundException
    {
        insertarProductos();
        clienteAgregado.agregarFavorito(productoRepository.findAll().get(0));
        clienteAgregado.agregarFavorito(productoRepository.findAll().get(1));

        List<Long> ids = productoService.obtenerFavoritosIdByUsuario(clienteAgregado.getId());
        Assertions.assertTrue(!ids.isEmpty());
        Assertions.assertTrue(ids.size() == 2);
    }

    @Transactional
    @Test
    public void agregarPuntuacion() throws ResourceNotFoundException, BadRequestException
    {
        Producto productoBuscado = productoRepository.findById(productoAgregado.getId()).orElse(null);
        Assertions.assertNotNull(productoBuscado);
        Assertions.assertTrue(productoBuscado.getPuntuaciones().isEmpty());

        PuntuacionDTO puntuacionDTO = new PuntuacionDTO(null, clienteAgregado, 3);
        productoService.agregarPuntuacion(productoBuscado.getId(), puntuacionDTO);

        Producto productoBuscadoConPuntuaciones = productoRepository.findById(productoBuscado.getId()).orElse(null);
        Assertions.assertFalse(productoBuscadoConPuntuaciones.getPuntuaciones().isEmpty());
        Assertions.assertTrue(productoBuscadoConPuntuaciones.getPuntuaciones().size() == 1);
        Assertions.assertEquals(productoAgregado, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getProducto());
        Assertions.assertEquals(3, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getPuntos());
    }

    @Transactional
    @Test
    public void actualizarPuntuacion() throws ResourceNotFoundException, BadRequestException
    {
        Producto productoBuscado = productoRepository.findById(productoAgregado.getId()).orElse(null);
        Assertions.assertNotNull(productoBuscado);
        Assertions.assertTrue(productoBuscado.getPuntuaciones().isEmpty());

        PuntuacionDTO puntuacionDTO = new PuntuacionDTO(null, clienteAgregado, 3);
        productoService.agregarPuntuacion(productoBuscado.getId(), puntuacionDTO);

        Producto productoBuscadoConPuntuaciones = productoRepository.findById(productoBuscado.getId()).orElse(null);
        Assertions.assertFalse(productoBuscadoConPuntuaciones.getPuntuaciones().isEmpty());
        Assertions.assertTrue(productoBuscadoConPuntuaciones.getPuntuaciones().size() == 1);
        Assertions.assertEquals(productoAgregado, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getProducto());
        Assertions.assertEquals(3, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getPuntos());

        puntuacionDTO = new PuntuacionDTO(null, clienteAgregado, 1);
        productoService.actualizarPuntuacion(productoBuscado.getId(), puntuacionDTO);

        productoBuscadoConPuntuaciones = productoRepository.findById(productoBuscado.getId()).orElse(null);
        Assertions.assertFalse(productoBuscadoConPuntuaciones.getPuntuaciones().isEmpty());
        Assertions.assertTrue(productoBuscadoConPuntuaciones.getPuntuaciones().size() == 1);
        Assertions.assertEquals(productoAgregado, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getProducto());
        Assertions.assertEquals(1, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getPuntos());
    }

    @Transactional
    @Test
    public void eliminarPuntuacion() throws ResourceNotFoundException, BadRequestException
    {
        Producto productoBuscado = productoRepository.findById(productoAgregado.getId()).orElse(null);
        Assertions.assertNotNull(productoBuscado);
        Assertions.assertTrue(productoBuscado.getPuntuaciones().isEmpty());

        PuntuacionDTO puntuacionDTO = new PuntuacionDTO(null, clienteAgregado, 3);
        productoService.agregarPuntuacion(productoBuscado.getId(), puntuacionDTO);

        Producto productoBuscadoConPuntuaciones = productoRepository.findById(productoBuscado.getId()).orElse(null);
        Assertions.assertFalse(productoBuscadoConPuntuaciones.getPuntuaciones().isEmpty());
        Assertions.assertTrue(productoBuscadoConPuntuaciones.getPuntuaciones().size() == 1);
        Assertions.assertEquals(productoAgregado, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getProducto());
        Assertions.assertEquals(3, productoBuscadoConPuntuaciones.getPuntuaciones().get(0).getPuntos());

        puntuacionDTO = new PuntuacionDTO(null, clienteAgregado, 1);
        productoService.eliminarPuntuacion(productoBuscado.getId(), puntuacionDTO);

        productoBuscadoConPuntuaciones = productoRepository.findById(productoBuscado.getId()).orElse(null);
        Assertions.assertTrue(productoBuscadoConPuntuaciones.getPuntuaciones().isEmpty());
    }

    private void insertarProductos() throws BadRequestException
    {
        productoService.agregar(new ProductoDTO(
                "Producto-002",
                "Good hotel, pretty good.",
                ciudadAgregada,
                categoriaRepository.save(new Categoria("Hotel de Playa", "Hoteles cercanos a balnearios", "http://www.hotel-playa.com")),
                null,
                null,
                -0.204205,
                -78.487900,
                "Paraguay 243",
                null
        ));
        productoService.agregar(new ProductoDTO(
                "Producto-003",
                "Bad hotel, very bad.",
                ciudadRepository.save(new Ciudad("Montevideo", "Uruguay")),
                categoriaAgregada,
                null,
                null,
                -32.859130,
                -68.861314,
                "Bolivar 975",
                null
        ));
    }
}