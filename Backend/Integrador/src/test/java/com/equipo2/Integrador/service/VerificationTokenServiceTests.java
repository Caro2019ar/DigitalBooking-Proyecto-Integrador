package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.mapper.ClienteDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.model.VerificationToken;
import com.equipo2.Integrador.repository.ClienteRepository;
import com.equipo2.Integrador.repository.RolRepository;
import com.equipo2.Integrador.repository.VerificationTokenRepository;
import com.equipo2.Integrador.service.impl.RolService;
import com.equipo2.Integrador.service.impl.VerificationTokenService;
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

import java.time.LocalDateTime;

@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class VerificationTokenServiceTests {

    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ClienteDTOModelMapper clienteDTOModelMapper;

    private VerificationToken verificationTokenAgregado;
    private Rol rolAgregado;
    private Cliente clienteAgregado;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException {
        rolAgregado = rolRepository.save(new Rol("ROLE_CUSTOMER"));
        clienteAgregado = clienteRepository.save(new Cliente(
                "Daniel",
                "Chaulet",
                "daniel_chaulet@gmail.com",
                new BCryptPasswordEncoder().encode("soydaniel"),
                rolAgregado,
                false
        ));
        verificationTokenAgregado = verificationTokenRepository.save(new VerificationToken(
                "token-string-12345",
                clienteAgregado
        ));
    }

    @AfterEach
    public void cleanUp()
    {
        verificationTokenRepository.deleteAll();
        clienteRepository.deleteAll();
        rolRepository.deleteAll();
    }

    @Test
    public void buscarVerificationToken()
    {
        VerificationToken verificationTokenBuscado = verificationTokenService.getVerificationToken(verificationTokenAgregado.getToken());
        Assertions.assertNotNull(verificationTokenBuscado);
        Assertions.assertEquals(verificationTokenAgregado.getId(), verificationTokenBuscado.getId());
    }

    @Test
    public void crearVerificationToken()
    {
        verificationTokenService.createVerificationToken(clienteDTOModelMapper.toDTO(clienteAgregado), "nuevo-token");
        VerificationToken verificationTokenBuscado = verificationTokenRepository.findByToken("nuevo-token");
        Assertions.assertNotNull(verificationTokenBuscado);
    }
}