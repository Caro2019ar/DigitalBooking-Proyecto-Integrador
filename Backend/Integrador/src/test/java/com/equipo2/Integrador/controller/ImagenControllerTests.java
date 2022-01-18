package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.DTO.ImagenDTO;
import com.equipo2.Integrador.DTO.ProductoDTO;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.model.Imagen;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.repository.CategoriaRepository;
import com.equipo2.Integrador.repository.CiudadRepository;
import com.equipo2.Integrador.repository.ImagenRepository;
import com.equipo2.Integrador.repository.ProductoRepository;
import com.equipo2.Integrador.service.impl.CategoriaService;
import com.equipo2.Integrador.service.impl.CiudadService;
import com.equipo2.Integrador.service.impl.ImagenService;
import com.equipo2.Integrador.service.impl.ProductoService;
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
import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@AutoConfigureMockMvc(addFilters = false)       // El addFilters es para desabilitar la seguridad de la API
@SpringBootTest
public class ImagenControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ImagenService imagenService;
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CiudadService ciudadService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CategoriaDTOModelMapper categoriaDTOModelMapper;
    @Autowired
    private CiudadDTOModelMapper ciudadDTOModelMapper;
    @Autowired
    private ProductoDTOModelMapper productoDTOModelMapper;

    private ImagenDTO imagenAgregadaDTO;
    private ProductoDTO producto;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        producto = productoService.agregar(new ProductoDTO(
                "Producto-001",
                "Great hotel, the best of the best.",
                ciudadDTOModelMapper.toModel(ciudadService.agregar(new CiudadDTO("Mendoza", "Argentina"))),
                categoriaDTOModelMapper.toModel(categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"))),
                null,
                null,
                -32.888557,
                -68.850720,
                "Av. Rivadavia 1280",
                null
        ));
        imagenAgregadaDTO = imagenService.agregar(new ImagenDTO("Imagen-001", "http://www.imagen001.com", productoDTOModelMapper.toModel(producto)));
    }

    @AfterEach
    public void cleanUp()
    {
        imagenRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void obtenerImagen() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/imagenes/"+imagenAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ImagenDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ImagenDTO.class);
        Assertions.assertEquals(imagenAgregadaDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerImagenInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/imagenes/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("La imagen con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        imagenService.agregar(new ImagenDTO("Imagen-002", "http://www.imagen002.com", productoDTOModelMapper.toModel(producto)));
        imagenService.agregar(new ImagenDTO("Imagen-003", "http://www.imagen003.com", productoDTOModelMapper.toModel(producto)));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/imagenes")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ImagenDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ImagenDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

}
