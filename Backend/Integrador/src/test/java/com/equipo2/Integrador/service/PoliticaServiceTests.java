package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.PoliticaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Politica;
import com.equipo2.Integrador.repository.PoliticaRepository;
import com.equipo2.Integrador.service.impl.PoliticaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class PoliticaServiceTests {

    @Autowired
    private PoliticaService politicaService;
    @Autowired
    private PoliticaRepository politicaRepository;

    private Politica politicaAgregada;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        politicaAgregada = politicaRepository.save(new Politica("Politica_Normas", "Politica_SaludYSeguridad", "Politica_Cancelacion"));
    }

    @AfterEach
    public void cleanUp()
    {
        politicaRepository.deleteAll();
    }

    @Test
    public void agregarPolitica() throws BadRequestException, ResourceConflictException
    {
        PoliticaDTO politicaDTO = politicaService.agregar(new PoliticaDTO("Politica_Nueva_1", "Politica_Nueva_2", "Politica_Nueva_3"));
        Assertions.assertNotNull(politicaDTO.getId());
    }

    @Test
    public void buscarPolitica() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> politicaService.buscar(politicaAgregada.getId()) );
        PoliticaDTO politicaBuscadaDTO = politicaService.buscar(politicaAgregada.getId());
        Assertions.assertNotNull(politicaBuscadaDTO);
        Assertions.assertEquals(politicaAgregada.getId(), politicaBuscadaDTO.getId());
    }

    @Test
    public void buscarPoliticaInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> politicaService.buscar(9999L));
    }

    @Test
    public void listarTodos() throws BadRequestException, ResourceConflictException
    {
        politicaService.agregar(new PoliticaDTO("Politica_23", "Politica_27", "Politica_32"));
        politicaService.agregar(new PoliticaDTO("Politica_12", "Politica_29", "Politica_35"));
        List<PoliticaDTO> politicaesDTO = politicaService.listar();
        Assertions.assertTrue(!politicaesDTO.isEmpty());
        Assertions.assertTrue(politicaesDTO.size() == 3);
    }

    @Test
    public void actualizarPolitica() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        PoliticaDTO politicaBuscadaDTO = politicaService.buscar(politicaAgregada.getId());
        Assertions.assertNotNull(politicaBuscadaDTO);
        Assertions.assertEquals("Politica_Cancelacion", politicaBuscadaDTO.getCancelacion());

        politicaBuscadaDTO.setCancelacion("Politica_Cancelacion_Renovada");
        PoliticaDTO politicaActualizadaDTO = politicaService.actualizar(politicaBuscadaDTO);
        PoliticaDTO politicaActualizadaBuscadaDTO = politicaService.buscar(politicaActualizadaDTO.getId());
        Assertions.assertNotNull(politicaActualizadaBuscadaDTO);
        Assertions.assertEquals("Politica_Cancelacion_Renovada", politicaActualizadaBuscadaDTO.getCancelacion());
    }

    @Test
    public void eliminarPolitica() throws ResourceNotFoundException, BadRequestException
    {
        politicaService.eliminar(politicaAgregada.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> politicaService.buscar(politicaAgregada.getId()));
    }
}