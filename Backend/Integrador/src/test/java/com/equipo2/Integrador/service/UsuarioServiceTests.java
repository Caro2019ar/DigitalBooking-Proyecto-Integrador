package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.exceptions.UnauthorizedAccessException;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.model.Usuario;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.UsuarioRepository;
import com.equipo2.Integrador.service.impl.UsuarioService;
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
public class UsuarioServiceTests {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    private Usuario usuarioAgregado;
    private Rol rolAgregado;


    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolAgregado = rolRepository.save(new Rol("ROLE_TEST"));
        usuarioAgregado = usuarioRepository.save(new Usuario(
                "Tito",
                "Sánchez",
                "tito_sanchez@outlook.com",
                new BCryptPasswordEncoder().encode("soytito"),
                rolAgregado,
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
    public void agregarUsuario() throws BadRequestException, ResourceConflictException
    {
        UsuarioDTO usuarioDTO = usuarioService.agregar(new UsuarioDTO(
                "Nuevo",
                "Usuario",
                "nuevo@usuario.com",
                "soynuevo",
                rolAgregado,
                false
        ));
        Assertions.assertNotNull(usuarioDTO.getId());
    }

    @Test
    public void agregarUsuarioEmailRepetido()
    {
        Assertions.assertThrows(ResourceConflictException.class, () ->
                usuarioService.agregar(new UsuarioDTO(
                        "Soy",
                        "Repetido",
                        "tito_sanchez@outlook.com",
                        new BCryptPasswordEncoder().encode("soyrepetido"),
                        rolAgregado,
                        false
                )));
    }

    @Test
    public void buscarUsuario() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> usuarioService.buscar(usuarioAgregado.getId()) );
        UsuarioDTO usuarioBuscadoDTO = usuarioService.buscar(usuarioAgregado.getId());
        Assertions.assertNotNull(usuarioBuscadoDTO);
        Assertions.assertEquals(usuarioAgregado.getId(), usuarioBuscadoDTO.getId());
    }

    @Test
    public void buscarUsuarioPorEmail() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> usuarioService.buscarPorEmail(usuarioAgregado.getEmail()) );
        UsuarioDTO usuarioBuscadoDTO = usuarioService.buscarPorEmail(usuarioAgregado.getEmail());
        Assertions.assertNotNull(usuarioBuscadoDTO);
        Assertions.assertEquals(usuarioAgregado.getId(), usuarioBuscadoDTO.getId());
    }

    @Test
    public void buscarUsuarioInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscar(9999L));
    }

    @Test
    public void listarTodos() throws BadRequestException, ResourceConflictException
    {
        usuarioService.agregar(new UsuarioDTO(
                "Nuevo",
                "Usuario",
                "nuevo@usuario.com",
                "soynuevo",
                rolAgregado,
                false
        ));
        usuarioService.agregar(new UsuarioDTO(
                "Nuevo_2",
                "Usuario_2",
                "nuevo_2@usuario_2.com",
                "soynuevo_2",
                rolAgregado,
                true
        ));
        List<UsuarioDTO> usuarioesDTO = usuarioService.listar();
        Assertions.assertTrue(!usuarioesDTO.isEmpty());
        Assertions.assertTrue(usuarioesDTO.size() == 3);
    }

    @Test
    public void actualizarUsuario() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        UsuarioDTO usuarioBuscadoDTO = usuarioService.buscar(usuarioAgregado.getId());
        Assertions.assertNotNull(usuarioBuscadoDTO);
        Assertions.assertEquals("Tito", usuarioBuscadoDTO.getNombre());

        usuarioBuscadoDTO.setNombre("Roberto");
        usuarioBuscadoDTO.setContrasena("nueva_contrasena");    // Si no queda en null y tira error
        UsuarioDTO usuarioActualizadoDTO = usuarioService.actualizar(usuarioBuscadoDTO);
        UsuarioDTO usuarioActualizadoBuscadoDTO = usuarioService.buscar(usuarioActualizadoDTO.getId());
        Assertions.assertNotNull(usuarioActualizadoBuscadoDTO);
        Assertions.assertEquals("Roberto", usuarioActualizadoBuscadoDTO.getNombre());
    }

    @Test
    public void eliminarUsuario() throws ResourceNotFoundException, BadRequestException
    {
        usuarioService.eliminar(usuarioAgregado.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> usuarioService.buscar(usuarioAgregado.getId()));
    }

    @Test
    public void validarLoginCorrecto() throws UnauthorizedAccessException
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                null,
                usuarioAgregado.getEmail(),
                "soytito",
                rolAgregado,
                true
        );

        Assertions.assertDoesNotThrow( () -> usuarioService.validateUserLogin(usuarioDTO) );
        UsuarioDTO usuarioDTOValidado = usuarioService.validateUserLogin(usuarioDTO);
        Assertions.assertEquals(usuarioAgregado.getEmail(), usuarioDTOValidado.getEmail());
    }

    @Test
    public void validarEmailIncorrecto() throws UnauthorizedAccessException
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                null,
                "email_incorrecto@outlook.com",
                "soytito",
                rolAgregado,
                true
        );

        Assertions.assertThrows(UnauthorizedAccessException.class, () -> usuarioService.validateUserLogin(usuarioDTO));
    }

    @Test
    public void validarContrasenaIncorrecta() throws UnauthorizedAccessException
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                null,
                null,
                usuarioAgregado.getEmail(),
                "contrasena_incorrecta",
                rolAgregado,
                true
        );

        Assertions.assertThrows(UnauthorizedAccessException.class, () -> usuarioService.validateUserLogin(usuarioDTO));
    }
}