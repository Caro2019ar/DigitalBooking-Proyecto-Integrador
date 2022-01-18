package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Producto;
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
public class ClienteControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private RolService rolService;
    @Autowired
    private RolRepository rolRepository;
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
    private CiudadDTOModelMapper ciudadDTOModelMapper;
    @Autowired
    private CategoriaDTOModelMapper categoriaDTOModelMapper;

    private ClienteDTO clienteAgregadoDTO;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolService.agregar(new RolDTO("ROLE_CUSTOMER"));
        clienteAgregadoDTO = clienteService.agregar(new ClienteDTO(
                "Daniel",
                "Chaulet",
                "daniel_chaulet@gmail.com",
                "soydaniel",
                false
        ));
    }

    @AfterEach
    public void cleanUp() throws Exception
    {
        // Lo tengo que hacer así en lugar de usar deleteAll por la restricción de la foreign key en la tabla FAVORITOS
        for( Producto producto : productoRepository.findAll() ) {
            productoService.eliminar(producto.getId());
        }
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void guardarCliente() throws Exception
    {
        ClienteDTO clienteDTO = new ClienteDTO(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                true
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ClienteDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ClienteDTO.class);
        List<ClienteDTO> lista = clienteService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerCliente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/clientes/"+ clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ClienteDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ClienteDTO.class);
        Assertions.assertEquals(clienteAgregadoDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerClientePorEmail() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/clientes/buscar?email="+ clienteAgregadoDTO.getEmail())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ClienteDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ClienteDTO.class);
        Assertions.assertEquals(clienteAgregadoDTO.getEmail(), responseAsObject.getEmail());
    }

    @Test
    public void obtenerClienteInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/clientes/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("El cliente con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodos() throws Exception
    {
        clienteService.agregar(new ClienteDTO(
                "Nuevo",
                "Cliente",
                "nuevo@cliente.com",
                "soynuevo",
                false
        ));
        clienteService.agregar(new ClienteDTO(
                "Nuevo_2",
                "Cliente_2",
                "nuevo_2@cliente_2.com",
                "soynuevo_2",
                true
        ));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ClienteDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ClienteDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void actualizarCliente() throws Exception
    {
        ClienteDTO clienteDTO = clienteService.buscar(clienteAgregadoDTO.getId());
        Assertions.assertEquals("daniel_chaulet@gmail.com", clienteDTO.getEmail());

        clienteDTO.setEmail("mi_nuevo_email@gmail.com");
        clienteDTO.setContrasena("nueva_contrasena");    // Si no queda en null y tira error

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ClienteDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ClienteDTO.class);
        Assertions.assertEquals("mi_nuevo_email@gmail.com", responseAsObject.getEmail());
    }

    @Test
    public void eliminarCliente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/" + clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("El cliente con el ID "+ clienteAgregadoDTO.getId()+" fue eliminado correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> clienteService.buscar(clienteAgregadoDTO.getId()));
    }

    @Test
    public void agregarFavorito() throws Exception
    {
        ProductoDTO productoDTO = productoService.agregar(new ProductoDTO(
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

        ProductoDTO productoDTOAEnviar = new ProductoDTO(productoDTO.getId(), null, null, null, null, null, null, null, null, null, null);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/clientes/" + clienteAgregadoDTO.getId() + "/agregar-favorito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTOAEnviar)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("OK", responseAsObject.getMessage());
    }

    @Test
    public void eliminarFavorito() throws Exception
    {
        ProductoDTO productoDTO = productoService.agregar(new ProductoDTO(
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

        ProductoDTO productoDTOAEnviar = new ProductoDTO(productoDTO.getId(), null, null, null, null, null, null, null, null, null, null);

        MvcResult responseAgregar = mockMvc.perform(MockMvcRequestBuilders.put("/clientes/" + clienteAgregadoDTO.getId() + "/agregar-favorito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTOAEnviar)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(responseAgregar.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObjectAgregar = objectMapper.readValue(responseAgregar.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("OK", responseAsObjectAgregar.getMessage());

        MvcResult responseEliminar = mockMvc.perform(MockMvcRequestBuilders.put("/clientes/" + clienteAgregadoDTO.getId() + "/eliminar-favorito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTOAEnviar)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(responseEliminar.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObjectEliminar = objectMapper.readValue(responseEliminar.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("OK", responseAsObjectEliminar.getMessage());
    }
}