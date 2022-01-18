package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.repository.CiudadRepository;
import com.equipo2.Integrador.service.impl.CiudadService;
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
public class CiudadControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CiudadService ciudadService;
    @Autowired
    private CiudadRepository ciudadRepository;

    private CiudadDTO ciudadAgregadaDTO;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        ciudadAgregadaDTO = ciudadService.agregar(new CiudadDTO("Mendoza", "Argentina"));
    }

    @AfterEach
    public void cleanUp()
    {
        ciudadRepository.deleteAll();
    }

    @Test
    public void guardarCiudad() throws Exception
    {
        CiudadDTO ciudadDTO = new CiudadDTO("Montevideo", "Uruguay");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/ciudades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciudadDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CiudadDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CiudadDTO.class);
        List<CiudadDTO> lista = ciudadService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCiudad() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/ciudades/"+ciudadAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CiudadDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CiudadDTO.class);
        Assertions.assertEquals(ciudadAgregadaDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCiudadInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/ciudades/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("La ciudad con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        ciudadService.agregar(new CiudadDTO("Cartagena", "Colombia"));
        ciudadService.agregar(new CiudadDTO("Quito", "Ecuador"));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/ciudades")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<CiudadDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<CiudadDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void actualizarCiudad() throws Exception
    {
        CiudadDTO ciudadDTO = ciudadService.buscar(ciudadAgregadaDTO.getId());
        Assertions.assertEquals("Mendoza", ciudadDTO.getNombre());

        ciudadDTO.setNombre("Resistencia");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/ciudades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ciudadDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CiudadDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CiudadDTO.class);
        Assertions.assertEquals("Resistencia", responseAsObject.getNombre());
    }

    @Test
    public void eliminarCiudad() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/ciudades/"+ciudadAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("La ciudad con el ID "+ciudadAgregadaDTO.getId()+" fue eliminada correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> ciudadService.buscar(ciudadAgregadaDTO.getId()));
    }
}
