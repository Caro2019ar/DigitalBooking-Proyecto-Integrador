package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.RolDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Rol;
import com.equipo2.Integrador.service.impl.RolService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Roles")
@CrossOrigin
@RestController
@RequestMapping("/roles")
public class RolController {

    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService)
    {
        this.rolService = rolService;
    }


    @Operation(summary = "Registrar un nuevo rol")
    @PostMapping
    public ResponseEntity<RolDTO> agregar(@RequestBody RolDTO rolDTO) throws BadRequestException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(rolService.agregar(rolDTO));
    }

    @Operation(summary = "Buscar un rol por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(rolService.buscar(id));
    }

    @Operation(summary = "Obtener todos los roles")
    @GetMapping
    public ResponseEntity<List<RolDTO>> listar()
    {
        return ResponseEntity.ok(rolService.listar());
    }

    @Operation(summary = "Actualizar un rol")
    @PutMapping
    public ResponseEntity<RolDTO> actualizar(@RequestBody RolDTO rolDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(rolService.actualizar(rolDTO));
    }

    @Operation(summary = "Eliminar un rol por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        rolService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("El rol con el ID " + id + " fue eliminado correctamente."));
    }
}