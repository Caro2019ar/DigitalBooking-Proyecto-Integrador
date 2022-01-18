package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.AdminDTO;
import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.model.Admin;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.AdminRepository;
import com.equipo2.Integrador.service.impl.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class AdminServiceTests {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RolRepository rolRepository;

    private Admin adminAgregado;
    private Rol rolAgregado;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolAgregado = rolRepository.save(new Rol("ROLE_ADMIN"));
        adminAgregado = adminRepository.save(new Admin(
                "Admin",
                "Grandioso",
                "admin@admin.com",
                new BCryptPasswordEncoder().encode("soyadmin"),
                rolAgregado,
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
    public void agregarAdmin() throws BadRequestException, ResourceConflictException
    {
        AdminDTO adminDTO = adminService.agregar(new AdminDTO(
                "Nuevo",
                "Admin",
                "nuevo@admin.com",
                "soynuevo",
                true
        ));
        Assertions.assertNotNull(adminDTO.getId());
    }

    @Test
    public void agregarAdminEmailRepetido()
    {
        Assertions.assertThrows(ResourceConflictException.class, () ->
                adminService.agregar(new AdminDTO(
                        "Soy",
                        "Repetido",
                        "admin@admin.com",
                        new BCryptPasswordEncoder().encode("soyrepetido"),
                        false
                )));
    }

    @Test
    public void buscarAdmin() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow(() -> adminService.buscar(adminAgregado.getId()));
        AdminDTO adminBuscadoDTO = adminService.buscar(adminAgregado.getId());
        Assertions.assertNotNull(adminBuscadoDTO);
        Assertions.assertEquals(adminAgregado.getId(), adminBuscadoDTO.getId());
    }

    @Test
    public void buscarAdminPorEmail() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow(() -> adminService.buscarPorEmail(adminAgregado.getEmail()));
        AdminDTO adminBuscadoDTO = adminService.buscarPorEmail(adminAgregado.getEmail());
        Assertions.assertNotNull(adminBuscadoDTO);
        Assertions.assertEquals(adminAgregado.getId(), adminBuscadoDTO.getId());
    }

    @Test
    public void buscarAdminInexistente() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> adminService.buscar(9999L));
    }

    @Test
    public void listarTodos() throws BadRequestException, ResourceConflictException
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
        List<AdminDTO> adminesDTO = adminService.listar();
        Assertions.assertTrue(!adminesDTO.isEmpty());
        Assertions.assertTrue(adminesDTO.size() == 3);
    }

    @Test
    public void actualizarAdmin() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        AdminDTO adminBuscadoDTO = adminService.buscar(adminAgregado.getId());
        Assertions.assertNotNull(adminBuscadoDTO);
        Assertions.assertEquals("Grandioso", adminBuscadoDTO.getApellido());

        adminBuscadoDTO.setApellido("Magnífico");
        adminBuscadoDTO.setContrasena("nueva_contrasena");    // Si no queda en null y tira error
        AdminDTO adminActualizadoDTO = adminService.actualizar(adminBuscadoDTO);
        AdminDTO adminActualizadoBuscadoDTO = adminService.buscar(adminActualizadoDTO.getId());
        Assertions.assertNotNull(adminActualizadoBuscadoDTO);
        Assertions.assertEquals("Magnífico", adminActualizadoBuscadoDTO.getApellido());
    }

    @Test
    public void eliminarAdmin() throws ResourceNotFoundException, BadRequestException {
        adminService.eliminar(adminAgregado.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> adminService.buscar(adminAgregado.getId()));
    }
}