package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.model.Imagen;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.impl.*;
import com.equipo2.Integrador.util.JsonResponseError;
import com.equipo2.Integrador.util.JsonResponseMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@AutoConfigureMockMvc(addFilters = false)       // El addFilters es para desabilitar la seguridad de la API
@SpringBootTest
public class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private CiudadService ciudadService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private RolService rolService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private CategoriaDTOModelMapper categoriaDTOModelMapper;
    @Autowired
    private CiudadDTOModelMapper ciudadDTOModelMapper;
    @Autowired
    private ProductoDTOModelMapper productoDTOModelMapper;
    @Autowired
    private ClienteDTOModelMapper clienteDTOModelMapper;

    private ProductoDTO productoAgregadoDTO;
    private CiudadDTO ciudadAgregadaDTO;
    private CategoriaDTO categoriaAgregadaDTO;
    private ClienteDTO clienteAgregadoDTO;
    private ReservaDTO reservaAgregadaDTO;
    private ProductoDTO productoAgregadoDTO2;
    private ClienteDTO clienteAgregadoDTO2;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        ciudadAgregadaDTO = ciudadService.agregar(new CiudadDTO("Mendoza", "Argentina"));
        categoriaAgregadaDTO = categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        productoAgregadoDTO = productoService.agregar(new ProductoDTO(
                "Producto-001",
                "Great hotel, the best of the best.",
                ciudadDTOModelMapper.toModel(ciudadAgregadaDTO),
                categoriaDTOModelMapper.toModel(categoriaAgregadaDTO),
                null,
                null,
                -32.888557,
                -68.850720,
                "Av. Rivadavia 1280",
                null
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        reservaRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
        imagenRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void guardarProducto() throws Exception
    {
        ProductoDTO productoDTO = new ProductoDTO(
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
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ProductoDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ProductoDTO.class);
        List<ProductoDTO> lista = productoService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerProducto() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/"+productoAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ProductoDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ProductoDTO.class);
        Assertions.assertEquals(productoAgregadoDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerProductoInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("El producto con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodas() throws Exception
    {
        insertarProductos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(3, responseAsObject.size());
    }

    @Test
    public void buscarProductosPorCiudad() throws Exception
    {
        insertarProductos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar?ciudad="+productoAgregadoDTO.getCiudad().getNombre())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(2, responseAsObject.size());
    }

    @Test
    public void buscarProductosPorCategoria() throws Exception
    {
        insertarProductos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar?categoria="+productoAgregadoDTO.getCategoria().getTitulo())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(2, responseAsObject.size());
    }

    @Test
    public void buscarProductosPorCiudadYCategoria() throws Exception
    {
        insertarProductos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar?ciudad="+productoAgregadoDTO.getCiudad().getNombre()+
                                                                                         "&categoria="+productoAgregadoDTO.getCategoria().getTitulo())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(1, responseAsObject.size());
    }

    @Test
    public void buscarProductosPorFechas() throws Exception
    {
        insertarProductos();
        insertarClientes();
        insertarReserva();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar-con-fechas?inicio=2022-12-05&fin=2022-12-07")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(2, responseAsObject.size());
    }

    @Test
    public void buscarProductosPorCiudadYFechas() throws Exception
    {
        insertarProductos();
        insertarClientes();
        insertarReserva();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/buscar-con-fechas?ciudad="+productoAgregadoDTO.getCiudad().getNombre()+"&inicio=2022-12-05&fin=2022-12-07")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(1, responseAsObject.size());
    }

    @Test
    public void obtenerFechasNoDisponible() throws Exception
    {
        insertarProductos();
        insertarClientes();
        insertarReserva();
        insertarFavoritos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/"+productoAgregadoDTO.getId()+"/fechas-no-disponible")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<LocalDate> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<LocalDate>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(6, responseAsObject.size());
        Assertions.assertEquals(LocalDate.of(2022, 12, 3), responseAsObject.get(0));
        Assertions.assertEquals(LocalDate.of(2022, 12, 8), responseAsObject.get(5));
    }

    @Test
    public void esFavorito() throws Exception
    {
        insertarProductos();
        insertarClientes();
        insertarReserva();
        insertarFavoritos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/"+productoAgregadoDTO.getId()+"/es-favorito?usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<JsonResponseMessage>(){});
        Assertions.assertEquals("true", responseAsObject.getMessage());
    }

    @Test
    public void obtenerFavoritosPorCliente() throws Exception
    {
        insertarProductos();
        insertarClientes();
        insertarReserva();
        insertarFavoritos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/favoritos?usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ProductoDTO> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ProductoDTO>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(2, responseAsObject.size());
        Assertions.assertEquals(productoAgregadoDTO.getId(), responseAsObject.get(0).getId());
        Assertions.assertEquals(productoAgregadoDTO2.getId(), responseAsObject.get(1).getId());
    }

    @Test
    public void obtenerFavoritosIdPorCliente() throws Exception
    {
        insertarProductos();
        insertarClientes();
        insertarReserva();
        insertarFavoritos();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/productos/favoritos-id?usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<Long> responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<Long>>(){});
        Assertions.assertFalse(responseAsObject.isEmpty());
        Assertions.assertEquals(2, responseAsObject.size());
        Assertions.assertEquals(productoAgregadoDTO.getId(), responseAsObject.get(0));
        Assertions.assertEquals(productoAgregadoDTO2.getId(), responseAsObject.get(1));
    }

    @Test
    public void actualizarProducto() throws Exception
    {
        ProductoDTO productoDTO = productoService.buscar(productoAgregadoDTO.getId());
        Assertions.assertEquals("Producto-001", productoDTO.getNombre());

        productoDTO.setNombre("Nuevo_Nombre");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ProductoDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ProductoDTO.class);
        Assertions.assertEquals("Nuevo_Nombre", responseAsObject.getNombre());
    }

    @Test
    public void agregarImagenes() throws Exception
    {
        List<Imagen> listaImagenesAInsertar = new ArrayList<>();
        listaImagenesAInsertar.add(new Imagen("Imagen-002", "http://www.imagen002.com", null));
        listaImagenesAInsertar.add(new Imagen("Imagen-003", "http://www.imagen003.com", null));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/agregar-imagenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listaImagenesAInsertar)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ProductoDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ProductoDTO.class);
        Assertions.assertFalse(responseAsObject.getImagenes().isEmpty());
        Assertions.assertEquals(2, responseAsObject.getImagenes().size());
    }

    @Test
    public void eliminarImagenes() throws Exception
    {
        List<Imagen> listaImagenesAInsertar = new ArrayList<>();
        listaImagenesAInsertar.add(new Imagen("Imagen-002", "http://www.imagen002.com", null));
        listaImagenesAInsertar.add(new Imagen("Imagen-003", "http://www.imagen003.com", null));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/agregar-imagenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listaImagenesAInsertar)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ProductoDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ProductoDTO.class);
        Assertions.assertFalse(responseAsObject.getImagenes().isEmpty());
        Assertions.assertEquals(2, responseAsObject.getImagenes().size());

        List<Imagen> listaImagenesABorrar = new ArrayList<>();
        listaImagenesAInsertar.add(responseAsObject.getImagenes().get(0));
        listaImagenesAInsertar.add(responseAsObject.getImagenes().get(1));

        response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/eliminar-imagenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(listaImagenesABorrar)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ProductoDTO.class);
        Assertions.assertFalse(responseAsObject.getImagenes().isEmpty());
    }

    @Test
    public void agregarPuntuacion() throws Exception
    {
        insertarClientes();

        PuntuacionDTO puntuacionDTO = new PuntuacionDTO(
                null,
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                5
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/agregar-puntuacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(puntuacionDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<JsonResponseMessage>(){});
        Assertions.assertEquals("OK", responseAsObject.getMessage());
    }

    @Test
    public void actualizarPuntuacion() throws Exception
    {
        insertarClientes();

        PuntuacionDTO puntuacionDTO = new PuntuacionDTO(
                null,
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                5
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/agregar-puntuacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(puntuacionDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<JsonResponseMessage>(){});
        Assertions.assertEquals("OK", responseAsObject.getMessage());

        puntuacionDTO = new PuntuacionDTO(
                null,
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                3
        );

        response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/actualizar-puntuacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(puntuacionDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<JsonResponseMessage>(){});
        Assertions.assertEquals("OK", responseAsObject.getMessage());
    }

    @Test
    public void eliminarPuntuacion() throws Exception
    {
        insertarClientes();

        PuntuacionDTO puntuacionDTO = new PuntuacionDTO(
                null,
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                5
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/agregar-puntuacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(puntuacionDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<JsonResponseMessage>(){});
        Assertions.assertEquals("OK", responseAsObject.getMessage());

        response = mockMvc.perform(MockMvcRequestBuilders.put("/productos/"+productoAgregadoDTO.getId()+"/eliminar-puntuacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(puntuacionDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<JsonResponseMessage>(){});
        Assertions.assertEquals("OK", responseAsObject.getMessage());
    }

    @Test
    public void eliminarProducto() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/productos/"+productoAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("El producto con el ID "+productoAgregadoDTO.getId()+" fue eliminado correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> productoService.buscar(productoAgregadoDTO.getId()));
    }

    private void insertarProductos() throws Exception
    {
        productoAgregadoDTO2 = productoService.agregar(new ProductoDTO(
                "Producto-002",
                "Good hotel, pretty good.",
                ciudadDTOModelMapper.toModel(ciudadAgregadaDTO),
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
                categoriaDTOModelMapper.toModel(categoriaAgregadaDTO),
                null,
                null,
                -32.859130,
                -68.861314,
                "Bolivar 975",
                null
        ));
    }

    private void insertarReserva() throws BadRequestException, ResourceConflictException
    {
        reservaAgregadaDTO = reservaService.agregar(new ReservaDTO(
                LocalTime.of(9, 0),
                LocalDate.of(2022, 12, 3),
                LocalDate.of(2022, 12, 8),
                productoDTOModelMapper.toModel(productoAgregadoDTO),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                "Rosario",
                "Nunca tuve covid y no planeo tenerlo. Si me lo agarro, me la banco.",
                false
        ));
    }

    private void insertarFavoritos() throws ResourceNotFoundException, BadRequestException
    {
        clienteService.agregarFavorito(clienteAgregadoDTO.getId(), productoAgregadoDTO);
        clienteService.agregarFavorito(clienteAgregadoDTO.getId(), productoAgregadoDTO2);
    }

    private void insertarClientes() throws BadRequestException, ResourceConflictException
    {
        rolService.agregar(new RolDTO("ROLE_CUSTOMER"));
        clienteAgregadoDTO = clienteService.agregar(new ClienteDTO(
                "Ana Laura",
                "Ramírez García",
                "anaru@hotmail.com",
                "soyanaruyamolosgatos",
                true
        ));
        clienteAgregadoDTO2 = clienteService.agregar(new ClienteDTO(
                "Daniel",
                "Chaulet",
                "daniel_chaulet@gmail.com",
                "soydaniel",
                false
        ));
    }
}
