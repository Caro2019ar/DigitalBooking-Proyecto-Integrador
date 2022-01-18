package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.ProductoDTO;
import com.equipo2.Integrador.exceptions.*;
import com.equipo2.Integrador.model.VerificationToken;
import com.equipo2.Integrador.service.impl.ClienteService;
import com.equipo2.Integrador.service.impl.VerificationTokenService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import com.equipo2.Integrador.util.OnRegistrationCompleteEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Tag(name = "Clientes")
@CrossOrigin
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final VerificationTokenService verificationTokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ClienteController(ClienteService clienteService, VerificationTokenService verificationTokenService, ApplicationEventPublisher eventPublisher)
    {
        this.clienteService = clienteService;
        this.verificationTokenService = verificationTokenService;
        this.eventPublisher = eventPublisher;
    }


    @Operation(summary = "Registrar un nuevo cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> agregar(@RequestBody ClienteDTO clienteDTO) throws BadRequestException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.agregar(clienteDTO));
    }

    @Operation(summary = "Buscar un cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscar(id));
    }

    @Operation(summary = "Buscar un cliente por email")
    @GetMapping("/buscar")
    public ResponseEntity<ClienteDTO> buscarPorEmail(@RequestParam("email") String email) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.buscarPorEmail(email));
    }

    @Operation(summary = "Obtener todos los clientes")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar()
    {
        return ResponseEntity.ok(clienteService.listar());
    }

    @Operation(summary = "Actualizar un cliente")
    @PutMapping
    public ResponseEntity<ClienteDTO> actualizar(@RequestBody ClienteDTO clienteDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.actualizar(clienteDTO));
    }

    @Operation(summary = "Eliminar un cliente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        clienteService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("El cliente con el ID " + id + " fue eliminado correctamente."));
    }

    @Operation(summary = "Registrar un nuevo cliente deshabilitado")
    @PostMapping("/disabled")
    public ResponseEntity<ClienteDTO> registrarClienteInhabilitado(@RequestBody ClienteDTO clienteDTO, HttpServletRequest request) throws BadRequestException, ResourceConflictException
    {
        ClienteDTO clienteRegistrado = clienteService.registrarClienteInhabilitado(clienteDTO);
        //System.out.println(request.getLocalName());
        //System.out.println(request.getLocalAddr());
        //System.out.println(request.getServerPort());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        System.out.println(baseUrl);
        //String appUrl = request.getRequestURL().toString();
        Thread emailSendThread = new Thread(() -> {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(clienteRegistrado, request.getLocale(), baseUrl));
        });
        emailSendThread.start();

        return ResponseEntity.status(HttpStatus.CREATED).body(clienteRegistrado);
    }

    @Operation(summary = "Confirmar el registro de un cliente deshabilitado")
    @GetMapping("/registrationConfirm")
    public ResponseEntity<JsonResponseMessage> confirmRegistration(@RequestParam("token") String token) throws VerificationTokenException
    {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken == null)
        {
            throw new VerificationTokenException("El token de verificación no existe.");
        }

        if (verificationToken.estaExpirado())
        {
            throw new VerificationTokenException("El link de verificación ha expirado.");
        }

        clienteService.habilitarCliente(verificationToken);
        return ResponseEntity.ok(new JsonResponseMessage("OK"));
    }

    @Operation(summary = "Agregar un producto ccomo favorito a un cliente")
    @PutMapping("/{id}/agregar-favorito")
    public ResponseEntity<JsonResponseMessage> agregarFavorito(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) throws ResourceNotFoundException, BadRequestException
    {
        clienteService.agregarFavorito(id, productoDTO);

        return ResponseEntity.ok(new JsonResponseMessage("OK"));
    }

    @Operation(summary = "Eliminar un producto ccomo favorito de un cliente")
    @PutMapping("/{id}/eliminar-favorito")
    public ResponseEntity<JsonResponseMessage> eliminarFavorito(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) throws ResourceNotFoundException, BadRequestException
    {
        clienteService.eliminarFavorito(id, productoDTO);

        return ResponseEntity.ok(new JsonResponseMessage("OK"));
    }
}