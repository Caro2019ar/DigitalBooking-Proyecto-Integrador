package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.DTO.RolDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.service.impl.RolService;

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
public class RolServiceTests {

    @Autowired
    private RolService rolService;
    @Autowired
    private RolRepository rolRepository;

    private Rol rolAgregado;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolAgregado = rolRepository.save(new Rol("ROLE_TEST"));
    }

    @AfterEach
    public void cleanUp()
    {
        rolRepository.deleteAll();
    }

    @Test
    public void agregarRol() throws BadRequestException, ResourceConflictException
    {
        RolDTO rolDTO = rolService.agregar(new RolDTO("ROLE_NUEVO"));
        Assertions.assertNotNull(rolDTO.getId());
    }

    @Test
    public void agregarRolNombreRepetido()
    {
        Assertions.assertThrows(ResourceConflictException.class, () ->
                rolService.agregar(new RolDTO("ROLE_TEST")));
    }

    @Test
    public void buscarRol() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> rolService.buscar(rolAgregado.getId()) );
        RolDTO rolBuscadoDTO = rolService.buscar(rolAgregado.getId());
        Assertions.assertNotNull(rolBuscadoDTO);
        Assertions.assertEquals(rolAgregado.getId(), rolBuscadoDTO.getId());
    }

    @Test
    public void buscarRolInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> rolService.buscar(9999L));
    }

    @Test
    public void listarTodos() throws BadRequestException, ResourceConflictException
    {
        rolService.agregar(new RolDTO("ROLE_PROBANDO"));
        rolService.agregar(new RolDTO("ROLE_TESTEANDO"));
        List<RolDTO> rolesDTO = rolService.listar();
        Assertions.assertTrue(!rolesDTO.isEmpty());
        Assertions.assertTrue(rolesDTO.size() == 3);
    }

    @Test
    public void actualizarRol() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        RolDTO rolBuscadoDTO = rolService.buscar(rolAgregado.getId());
        Assertions.assertNotNull(rolBuscadoDTO);
        Assertions.assertEquals("ROLE_TEST", rolBuscadoDTO.getNombre());

        rolBuscadoDTO.setNombre("ROLE_NEW_NAME");
        RolDTO rolActualizadoDTO = rolService.actualizar(rolBuscadoDTO);
        RolDTO rolActualizadoBuscadoDTO = rolService.buscar(rolActualizadoDTO.getId());
        Assertions.assertNotNull(rolActualizadoBuscadoDTO);
        Assertions.assertEquals("ROLE_NEW_NAME", rolActualizadoBuscadoDTO.getNombre());
    }

    @Test
    public void eliminarRol() throws ResourceNotFoundException, BadRequestException
    {
        rolService.eliminar(rolAgregado.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> rolService.buscar(rolAgregado.getId()));
    }
}