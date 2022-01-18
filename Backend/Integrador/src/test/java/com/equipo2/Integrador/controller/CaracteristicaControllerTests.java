package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.repository.CaracteristicaRepository;
import com.equipo2.Integrador.service.impl.CaracteristicaService;
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
public class CaracteristicaControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CaracteristicaService caracteristicaService;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    private CaracteristicaDTO caracteristicaAgregadaDTO;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        caracteristicaAgregadaDTO = caracteristicaService.agregar(new CaracteristicaDTO("Pileta", "fa-pileta"));
    }

    @AfterEach
    public void cleanUp()
    {
        caracteristicaRepository.deleteAll();
    }

    @Test
    public void guardarCaracteristica() throws Exception
    {
        CaracteristicaDTO caracteristicaDTO = new CaracteristicaDTO("WiFi", "fa-wifi");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/caracteristicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caracteristicaDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CaracteristicaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CaracteristicaDTO.class);
        List<CaracteristicaDTO> lista = caracteristicaService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCaracteristica() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/caracteristicas/"+caracteristicaAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CaracteristicaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CaracteristicaDTO.class);
        Assertions.assertEquals(caracteristicaAgregadaDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCaracteristicaInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/caracteristicas/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("La característica con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        caracteristicaService.agregar(new CaracteristicaDTO("WiFi", "fa-wifi"));
        caracteristicaService.agregar(new CaracteristicaDTO("Desayuno", "fa-desayuno"));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/caracteristicas")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<CaracteristicaDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<CaracteristicaDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void actualizarCaracteristica() throws Exception
    {
        CaracteristicaDTO caracteristicaDTO = caracteristicaService.buscar(caracteristicaAgregadaDTO.getId());
        Assertions.assertEquals("fa-pileta", caracteristicaDTO.getIcono());

        caracteristicaDTO.setIcono("fa-piscina");

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/caracteristicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caracteristicaDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        CaracteristicaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), CaracteristicaDTO.class);
        Assertions.assertEquals("fa-piscina", responseAsObject.getIcono());
    }

    @Test
    public void eliminarCaracteristica() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/caracteristicas/"+caracteristicaAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("La característica con el ID "+caracteristicaAgregadaDTO.getId()+" fue eliminada correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> caracteristicaService.buscar(caracteristicaAgregadaDTO.getId()));
    }
}
