package com.equipo2.Integrador.service;


import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.model.Usuario;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.UsuarioRepository;
import com.equipo2.Integrador.service.impl.JwtUserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class JwtUserDetailsServiceTests {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RolRepository rolRepository;

    private UserDetails usuarioAgregado;
    private Rol rolAgregado;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        rolAgregado = rolRepository.save(new Rol("ROLE_TEST"));
        usuarioAgregado = usuarioRepository.save(new Usuario("Daniel", "Chaulet", "admin@gmail.com", "admin", rolAgregado, true));
    }

    @AfterEach
    public void cleanUp()
    {
        usuarioRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void loadUsuarioExistente() throws UsernameNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> jwtUserDetailsService.loadUserByUsername(usuarioAgregado.getUsername()) );

        UserDetails userLoaded = jwtUserDetailsService.loadUserByUsername(usuarioAgregado.getUsername());

        Assertions.assertNotNull(userLoaded);
        Assertions.assertEquals(userLoaded.getUsername(), usuarioAgregado.getUsername());
    }

    @Test
    public void loadUsuarioInexistente() throws UsernameNotFoundException
    {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsService.loadUserByUsername("email@inexistente.com"));
    }
}
