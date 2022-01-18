package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.RolDTO;
import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.DTO.mapper.RolDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Usuario;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.UsuarioRepository;
import com.equipo2.Integrador.service.impl.CiudadService;
import com.equipo2.Integrador.service.impl.RolService;
import com.equipo2.Integrador.service.impl.UsuarioService;
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
public class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolService rolService;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private RolDTOModelMapper rolDTOModelMapper;

    private UsuarioDTO usuarioAgregadoDTO;
    private RolDTO rolAgregadoDTO;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolAgregadoDTO = rolService.agregar(new RolDTO("ROLE_NUEVO"));
        usuarioAgregadoDTO = usuarioService.agregar(new UsuarioDTO(
                "Tito",
                "Sánchez",
                "tito_sanchez@outlook.com",
                "soytito",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                true
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void guardarUsuario() throws Exception
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                "Nuevo",
                "Usuario",
                "nuevo@usuario.com",
                "soynuevo",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                false
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        UsuarioDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), UsuarioDTO.class);
        List<UsuarioDTO> lista = usuarioService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerUsuario() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/"+ usuarioAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        UsuarioDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), UsuarioDTO.class);
        Assertions.assertEquals(usuarioAgregadoDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerUsuarioPorEmail() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/buscar?email="+ usuarioAgregadoDTO.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        UsuarioDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), UsuarioDTO.class);
        Assertions.assertEquals(usuarioAgregadoDTO.getEmail(), responseAsObject.getEmail());
    }

    @Test
    public void obtenerUsuarioInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("El usuario con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        usuarioService.agregar(new UsuarioDTO(
                "Nuevo",
                "Usuario",
                "nuevo@usuario.com",
                "soynuevo",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                false
        ));
        usuarioService.agregar(new UsuarioDTO(
                "Nuevo_2",
                "Usuario_2",
                "nuevo_2@usuario_2.com",
                "soynuevo_2",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                true
        ));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/usuarios")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<UsuarioDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<UsuarioDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void actualizarUsuario() throws Exception
    {
        UsuarioDTO usuarioDTO = usuarioService.buscar(usuarioAgregadoDTO.getId());
        Assertions.assertEquals("Tito", usuarioDTO.getNombre());

        usuarioDTO.setNombre("Roberto");
        usuarioDTO.setContrasena("nueva_contrasena");    // Si no queda en null y tira error

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        UsuarioDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), UsuarioDTO.class);
        Assertions.assertEquals("Roberto", responseAsObject.getNombre());
    }

    @Test
    public void eliminarUsuario() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/usuarios/" + usuarioAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("El usuario con el ID "+ usuarioAgregadoDTO.getId()+" fue eliminado correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscar(usuarioAgregadoDTO.getId()));
    }

    @Test
    public void validarLoginCorrecto() throws Exception
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                null,
                usuarioAgregadoDTO.getEmail(),
                "soytito",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                true
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/validate-user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        UsuarioDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), UsuarioDTO.class);
        Assertions.assertEquals(usuarioDTO.getEmail(), responseAsObject.getEmail());
    }

    @Test
    public void validarEmailIncorrecto() throws Exception
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                null,
                "email_incorrecto@outlook.com",
                "soytito",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                true
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/validate-user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(401, responseAsObject.getStatus());
        Assertions.assertEquals("El usuario con el email " + usuarioDTO.getEmail() + " no existe.", responseAsObject.getError());
    }

    @Test
    public void validarContrasenaIncorrecta() throws Exception
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                null,
                usuarioAgregadoDTO.getEmail(),
                "contrasena_incorrecta",
                rolDTOModelMapper.toModel(rolAgregadoDTO),
                true
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/usuarios/validate-user-login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(401, responseAsObject.getStatus());
        Assertions.assertEquals("La contraseña ingresada es incorrecta.", responseAsObject.getError());
    }
}
