package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.RolDTO;
import com.equipo2.Integrador.DTO.AdminDTO;
import com.equipo2.Integrador.DTO.mapper.RolDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.AdminRepository;
import com.equipo2.Integrador.service.impl.RolService;
import com.equipo2.Integrador.service.impl.AdminService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RolService rolService;
    @Autowired
    private RolRepository rolRepository;

    private AdminDTO adminAgregadoDTO;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolService.agregar(new RolDTO("ROLE_ADMIN"));
        adminAgregadoDTO = adminService.agregar(new AdminDTO(
                "Admin",
                "Grandioso",
                "admin@admin.com",
                "soyadmin",
                true
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        adminRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void guardarAdmin() throws Exception
    {
        AdminDTO adminDTO = new AdminDTO(
                "Nuevo",
                "Admin",
                "nuevo@admin.com",
                "soynuevo",
                true
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        AdminDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), AdminDTO.class);
        List<AdminDTO> lista = adminService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerAdmin() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admins/"+ adminAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        AdminDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), AdminDTO.class);
        Assertions.assertEquals(adminAgregadoDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerAdminPorEmail() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admins/buscar?email="+ adminAgregadoDTO.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        AdminDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), AdminDTO.class);
        Assertions.assertEquals(adminAgregadoDTO.getEmail(), responseAsObject.getEmail());
    }

    @Test
    public void obtenerAdminInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admins/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("El admin con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        adminService.agregar(new AdminDTO(
                "Nuevo",
                "Admin",
                "nuevo@admin.com",
                "soynuevo",
                false
        ));
        adminService.agregar(new AdminDTO(
                "Nuevo_2",
                "Admin_2",
                "nuevo_2@admin_2.com",
                "soynuevo_2",
                true
        ));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/admins")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<AdminDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<AdminDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void actualizarAdmin() throws Exception
    {
        AdminDTO adminDTO = adminService.buscar(adminAgregadoDTO.getId());
        Assertions.assertEquals("Grandioso", adminDTO.getApellido());

        adminDTO.setApellido("Magnífico");
        adminDTO.setContrasena("nueva_contrasena");    // Si no queda en null y tira error

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        AdminDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), AdminDTO.class);
        Assertions.assertEquals("Magnífico", responseAsObject.getApellido());
    }

    @Test
    public void eliminarAdmin() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/admins/" + adminAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("El admin con el ID "+ adminAgregadoDTO.getId()+" fue eliminado correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> adminService.buscar(adminAgregadoDTO.getId()));
    }
}