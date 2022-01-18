package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.repository.CiudadRepository;
import com.equipo2.Integrador.service.impl.CiudadService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class CiudadServiceTests {

    @Autowired
    private CiudadService ciudadService;
    @Autowired
    private CiudadRepository ciudadRepository;

    private Ciudad ciudadAgregada;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        ciudadAgregada = ciudadRepository.save(new Ciudad("Mendoza", "Argentina"));
    }

    @AfterEach
    public void cleanUp()
    {
        ciudadRepository.deleteAll();
    }

    @Test
    public void agregarCiudad() throws BadRequestException, ResourceConflictException
    {
        CiudadDTO ciudadDTO = ciudadService.agregar(new CiudadDTO("Cartagena", "Colombia"));
        Assertions.assertNotNull(ciudadDTO.getId());
    }

    @Test
    public void agregarCiudadNombreRepetido()
    {
        Assertions.assertThrows(ResourceConflictException.class, () ->
                ciudadService.agregar(new CiudadDTO("Mendoza", "Argentina")));
    }

    @Test
    public void buscarCiudad() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> ciudadService.buscar(ciudadAgregada.getId()) );
        CiudadDTO ciudadBuscadaDTO = ciudadService.buscar(ciudadAgregada.getId());
        Assertions.assertNotNull(ciudadBuscadaDTO);
        Assertions.assertEquals(ciudadAgregada.getId(), ciudadBuscadaDTO.getId());
    }

    @Test
    public void buscarCiudadInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> ciudadService.buscar(9999L));
    }

    @Test
    public void listarTodas() throws BadRequestException, ResourceConflictException
    {
        ciudadService.agregar(new CiudadDTO("Cartagena", "Colombia"));
        ciudadService.agregar(new CiudadDTO("Quito", "Ecuador"));
        List<CiudadDTO> ciudadesDTO = ciudadService.listar();
        Assertions.assertTrue(!ciudadesDTO.isEmpty());
        Assertions.assertTrue(ciudadesDTO.size() == 3);
    }

    @Test
    public void actualizarCiudad() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        CiudadDTO ciudadBuscadaDTO = ciudadService.buscar(ciudadAgregada.getId());
        Assertions.assertNotNull(ciudadBuscadaDTO);
        Assertions.assertEquals("Mendoza", ciudadBuscadaDTO.getNombre());

        ciudadBuscadaDTO.setNombre("Atenas");
        CiudadDTO ciudadActualizadaDTO = ciudadService.actualizar(ciudadBuscadaDTO);
        CiudadDTO ciudadActualizadaBuscadaDTO = ciudadService.buscar(ciudadActualizadaDTO.getId());
        Assertions.assertNotNull(ciudadActualizadaBuscadaDTO);
        Assertions.assertEquals("Atenas", ciudadActualizadaBuscadaDTO.getNombre());
    }

    @Test
    public void eliminarCiudad() throws ResourceNotFoundException, BadRequestException
    {
        ciudadService.eliminar(ciudadAgregada.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> ciudadService.buscar(ciudadAgregada.getId()));
    }
}