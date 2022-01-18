package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.impl.*;
import com.equipo2.Integrador.util.JsonResponseError;
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
import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@AutoConfigureMockMvc(addFilters = false)       // El addFilters es para desabilitar la seguridad de la API
@SpringBootTest
public class PuntuacionControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PuntuacionService puntuacionService;
    @Autowired
    private PuntuacionRepository puntuacionRepository;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CiudadService ciudadService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private RolService rolService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private CategoriaDTOModelMapper categoriaDTOModelMapper;
    @Autowired
    private CiudadDTOModelMapper ciudadDTOModelMapper;
    @Autowired
    private ProductoDTOModelMapper productoDTOModelMapper;
    @Autowired
    private ClienteDTOModelMapper clienteDTOModelMapper;

    private PuntuacionDTO puntuacionAgregadaDTO;
    private ProductoDTO productoAgregadoDTO;
    private ClienteDTO clienteAgregadoDTO;
    private CiudadDTO ciudadAgregadaDTO;
    private CategoriaDTO categoriaAgregadaDTO;


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
        rolService.agregar(new RolDTO("ROLE_CUSTOMER"));
        clienteAgregadoDTO = clienteService.agregar(new ClienteDTO(
                "Ana Laura",
                "Ramírez García",
                "anaru@hotmail.com",
                "soyanaruyamolosgatos",
                true
        ));
        puntuacionAgregadaDTO = puntuacionService.agregar(new PuntuacionDTO(
                productoDTOModelMapper.toModel(productoAgregadoDTO),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                4
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        puntuacionRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void obtenerPuntuacion() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/puntuaciones/"+puntuacionAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        PuntuacionDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), PuntuacionDTO.class);
        Assertions.assertEquals(puntuacionAgregadaDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerPuntuacionInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/puntuaciones/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("La puntuación con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        insertarPuntuaciones();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/puntuaciones")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<PuntuacionDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<PuntuacionDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void buscarPuntuacionesPorProducto() throws Exception
    {
        insertarPuntuaciones();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/puntuaciones/buscar?producto="+productoAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<PuntuacionDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<PuntuacionDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(2, responseAsObjet.size());
    }

    @Test
    public void buscarPuntuacionesPorUsuario() throws Exception
    {
        insertarPuntuaciones();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/puntuaciones/buscar?usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<PuntuacionDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<PuntuacionDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(2, responseAsObjet.size());
    }

    @Test
    public void buscarPuntuacionesPorProductoYUsuario() throws Exception
    {
        insertarPuntuaciones();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/puntuaciones/buscar?producto="+productoAgregadoDTO.getId()+"&usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<PuntuacionDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<PuntuacionDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(1, responseAsObjet.size());
    }

    private void insertarPuntuaciones() throws Exception
    {
        ProductoDTO productoAgregadoDTO1 = productoService.agregar(new ProductoDTO(
                "Producto-002",
                "Good hotel, pretty good.",
                ciudadDTOModelMapper.toModel(ciudadAgregadaDTO),
                categoriaDTOModelMapper.toModel(categoriaAgregadaDTO),
                null,
                null,
                -0.204205,
                -78.487900,
                "Paraguay 243",
                null
        ));
        ClienteDTO clienteAgregadoDTO1 = clienteService.agregar(new ClienteDTO(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                false
        ));
        puntuacionService.agregar(new PuntuacionDTO(
                productoDTOModelMapper.toModel(productoAgregadoDTO1),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                2
        ));
        puntuacionService.agregar(new PuntuacionDTO(
                productoDTOModelMapper.toModel(productoAgregadoDTO),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO1),
                3
        ));
    }
}
