package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.repository.CategoriaRepository;
import com.equipo2.Integrador.service.impl.CategoriaService;
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
public class CategoriaControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private CategoriaDTO categoriaAgregadaDTO;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        categoriaAgregadaDTO = categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
    }

    @AfterEach
    public void cleanUp()
    {
        categoriaRepository.deleteAll();
    }

    @Test
    public void guardarCategoria() throws Exception
    {
        CategoriaDTO categoriaDTO = new CategoriaDTO("Bed & Breakfast", "Los mejores B&B para un descanso gratificante a un precio razonable", "https://www.bedandbreakfast.com/");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CategoriaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CategoriaDTO.class);
        List<CategoriaDTO> lista = categoriaService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCategoria() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/categorias/"+categoriaAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CategoriaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CategoriaDTO.class);
        Assertions.assertEquals(categoriaAgregadaDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCategoriaInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/categorias/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("La categoría con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        categoriaService.agregar(new CategoriaDTO("Hotel de Playa", "Hoteles cercanos a balnearios", "http://www.hoteldeplaya.com"));
        categoriaService.agregar(new CategoriaDTO("Hotel en la Naturaleza", "Hoteles cercanos a selvas/bosques/montañas", "http://www.hotelnaturaleza.com"));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/categorias")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<CategoriaDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<CategoriaDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void actualizarCategoria() throws Exception
    {
        CategoriaDTO categoriaDTO = categoriaService.buscar(categoriaAgregadaDTO.getId());
        Assertions.assertEquals("http://www.hotelurbano.com", categoriaDTO.getUrlImagen());

        categoriaDTO.setUrlImagen("http://www.hotelmetropolitano.org");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CategoriaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CategoriaDTO.class);
        Assertions.assertEquals("http://www.hotelmetropolitano.org", responseAsObject.getUrlImagen());
    }

    @Test
    public void eliminarCategoria() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/categorias/"+categoriaAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("La categoría con el ID "+categoriaAgregadaDTO.getId()+" fue eliminada correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscar(categoriaAgregadaDTO.getId()));
    }
}
