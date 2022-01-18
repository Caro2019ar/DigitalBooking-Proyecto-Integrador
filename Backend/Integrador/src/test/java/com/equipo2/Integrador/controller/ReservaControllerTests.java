package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.DTO.mapper.CategoriaDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
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
import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@AutoConfigureMockMvc(addFilters = false)       // El addFilters es para desabilitar la seguridad de la API
@SpringBootTest
public class ReservaControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private ReservaRepository reservaRepository;
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

    private ReservaDTO reservaAgregadaDTO;
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

    @AfterEach
    public void cleanUp()
    {
        reservaRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void guardarReserva() throws Exception
    {
        ReservaDTO reservaDTO = new ReservaDTO(
                LocalTime.of(15, 0),
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 14),
                productoDTOModelMapper.toModel(productoAgregadoDTO),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                "Santiago de Chile",
                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                false
        );

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ReservaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ReservaDTO.class);
        List<ReservaDTO> lista = reservaService.listar();
        Assertions.assertEquals(lista.get(lista.size()-1).getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerReserva() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/reservas/"+reservaAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ReservaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ReservaDTO.class);
        Assertions.assertEquals(reservaAgregadaDTO.getId(), responseAsObject.getId());
    }

    @Test
    public void obtenerReservaInexistente() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/reservas/9999")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isNotFound())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseError responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseError.class);
        Assertions.assertEquals(404, responseAsObject.getStatus());
        Assertions.assertEquals("La reserva con el ID 9999 no existe.", responseAsObject.getError());
    }

    @Test
    public void obtenerTodas() throws Exception
    {
        insertarReservaes();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/reservas")
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ReservaDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ReservaDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(3, responseAsObjet.size());
    }

    @Test
    public void buscarReservasPorProducto() throws Exception
    {
        insertarReservaes();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/reservas/buscar?producto="+productoAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ReservaDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ReservaDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(2, responseAsObjet.size());
    }

    @Test
    public void buscarReservasPorUsuario() throws Exception
    {
        insertarReservaes();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/reservas/buscar?usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ReservaDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ReservaDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(2, responseAsObjet.size());
    }

    @Test
    public void buscarReservasPorProductoYUsuario() throws Exception
    {
        insertarReservaes();

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/reservas/buscar?producto="+productoAgregadoDTO.getId()+"&usuario="+clienteAgregadoDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        List<ReservaDTO> responseAsObjet = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), new TypeReference<List<ReservaDTO>>(){});
        Assertions.assertFalse(responseAsObjet.isEmpty());
        Assertions.assertEquals(1, responseAsObjet.size());
    }

    @Test
    public void actualizarReserva() throws Exception
    {
        ReservaDTO reservaDTO = reservaService.buscar(reservaAgregadaDTO.getId());
        Assertions.assertEquals(LocalDate.of(2022, 12, 8), reservaDTO.getFechaFinal());

        reservaDTO.setFechaFinal(LocalDate.of(2022, 12, 15));

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        ReservaDTO responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), ReservaDTO.class);
        Assertions.assertEquals(LocalDate.of(2022, 12, 15), responseAsObject.getFechaFinal());
    }

    @Test
    public void eliminarReserva() throws Exception
    {
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/reservas/"+reservaAgregadaDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        Assertions.assertFalse(response.getResponse().getContentAsString().isEmpty());
        JsonResponseMessage responseAsObject = objectMapper.readValue(response.getResponse().getContentAsString(StandardCharsets.UTF_8), JsonResponseMessage.class);
        Assertions.assertEquals("La reserva con el ID "+reservaAgregadaDTO.getId()+" fue eliminada correctamente.", responseAsObject.getMessage());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reservaService.buscar(reservaAgregadaDTO.getId()));
    }

    private void insertarReservaes() throws Exception
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

        reservaService.agregar(new ReservaDTO(
                LocalTime.of(18, 0),
                LocalDate.of(2022, 12, 12),
                LocalDate.of(2022, 12, 14),
                productoDTOModelMapper.toModel(productoAgregadoDTO1),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO),
                "Santiago de Chile",
                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                false
        ));
        reservaService.agregar(new ReservaDTO(
                LocalTime.of(23, 0),
                LocalDate.of(2022, 12, 21),
                LocalDate.of(2022, 12, 27),
                productoDTOModelMapper.toModel(productoAgregadoDTO),
                clienteDTOModelMapper.toModel(clienteAgregadoDTO1),
                "Pilar",
                "Tuve covid una vez. No la pasé tan mal, pero ahora estoy más tranquila porque ya estoy vacunada.",
                true
        ));
    }
}
