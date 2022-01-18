package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.repository.CaracteristicaRepository;
import com.equipo2.Integrador.service.impl.CaracteristicaService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class CaracteristicaServiceTests {

    @Autowired
    private CaracteristicaService caracteristicaService;
    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    private Caracteristica caracteristicaAgregada;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        caracteristicaAgregada = caracteristicaRepository.save(new Caracteristica("Pileta", "fa-pileta"));
    }

    @AfterEach
    public void cleanUp()
    {
        caracteristicaRepository.deleteAll();
    }

    @Test
    public void agregarCaracteristica() throws BadRequestException, ResourceConflictException
    {
        CaracteristicaDTO caracteristicaDTO = caracteristicaService.agregar(new CaracteristicaDTO("WiFi", "fa-wifi"));
        Assertions.assertNotNull(caracteristicaDTO.getId());
    }

    @Test
    public void agregarCaracteristicaNombreRepetido()
    {
        Assertions.assertThrows(ResourceConflictException.class, () ->
                caracteristicaService.agregar(new CaracteristicaDTO("Pileta", "fa-pool")));
    }

    @Test
    public void buscarCaracteristica() throws BadRequestException, ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> caracteristicaService.buscar(caracteristicaAgregada.getId()) );
        CaracteristicaDTO caracteristicaBuscadaDTO = caracteristicaService.buscar(caracteristicaAgregada.getId());
        Assertions.assertNotNull(caracteristicaBuscadaDTO);
        Assertions.assertEquals(caracteristicaAgregada.getId(), caracteristicaBuscadaDTO.getId());
    }

    @Test
    public void buscarCaracteristicaInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> caracteristicaService.buscar(9999L));
    }

    @Test
    public void listarTodas() throws BadRequestException, ResourceConflictException
    {
        caracteristicaService.agregar(new CaracteristicaDTO("WiFi", "fa-wifi"));
        caracteristicaService.agregar(new CaracteristicaDTO("Desayuno", "fa-desayuno"));
        List<CaracteristicaDTO> caracteristicas = caracteristicaService.listar();
        Assertions.assertTrue(!caracteristicas.isEmpty());
        Assertions.assertTrue(caracteristicas.size() == 3);
    }

    @Test
    public void actualizarCaracteristica() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        CaracteristicaDTO caracteristicaBuscadaDTO = caracteristicaService.buscar(caracteristicaAgregada.getId());
        Assertions.assertNotNull(caracteristicaBuscadaDTO);
        Assertions.assertEquals("fa-pileta", caracteristicaBuscadaDTO.getIcono());

        caracteristicaBuscadaDTO.setNombre("fa-piscina");
        CaracteristicaDTO caracteristicaActualizadaDTO = caracteristicaService.actualizar(caracteristicaBuscadaDTO);
        CaracteristicaDTO caracteristicaActualizadaBuscadaDTO = caracteristicaService.buscar(caracteristicaActualizadaDTO.getId());
        Assertions.assertNotNull(caracteristicaActualizadaBuscadaDTO);
        Assertions.assertEquals("fa-piscina", caracteristicaActualizadaBuscadaDTO.getNombre());
    }

    @Test
    public void eliminarCaracteristica() throws ResourceNotFoundException, BadRequestException
    {
        caracteristicaService.eliminar(caracteristicaAgregada.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> caracteristicaService.buscar(caracteristicaAgregada.getId()));
    }
}