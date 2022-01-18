package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.exceptions.*;
import com.equipo2.Integrador.service.impl.UsuarioService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuarios")
@CrossOrigin
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService)
    {
        this.usuarioService = usuarioService;
    }


    @Operation(summary = "Registrar un nuevo usuario")
    @PostMapping
    public ResponseEntity<UsuarioDTO> agregar(@RequestBody UsuarioDTO usuarioDTO) throws BadRequestException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.agregar(usuarioDTO));
    }

    @Operation(summary = "Buscar un usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscar(id));
    }

    @Operation(summary = "Buscar un usuario por email")
    @GetMapping("/buscar")
    public ResponseEntity<UsuarioDTO> buscarPorEmail(@RequestParam("email") String email) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.buscarPorEmail(email));
    }

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar()
    {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "Actualizar un usuario")
    @PutMapping
    public ResponseEntity<UsuarioDTO> actualizar(@RequestBody UsuarioDTO usuarioDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.actualizar(usuarioDTO));
    }

    @Operation(summary = "Eliminar un usuario por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        usuarioService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("El usuario con el ID " + id + " fue eliminado correctamente."));
    }

    @Operation(summary = "Validar las credenciales de login")
    @PostMapping("/validate-user-login")
    public ResponseEntity<UsuarioDTO> validateUserLogin(@RequestBody UsuarioDTO usuarioDTO) throws UnauthorizedAccessException
    {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.validateUserLogin(usuarioDTO));
    }
}