package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.service.impl.CiudadService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Ciudades")
@CrossOrigin
@RestController
@RequestMapping("/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    @Autowired
    public CiudadController(CiudadService ciudadService)
    {
        this.ciudadService = ciudadService;
    }


    @Operation(summary = "Registrar una nueva ciudad")
    @PostMapping
    public ResponseEntity<CiudadDTO> agregar(@RequestBody CiudadDTO ciudadDTO) throws BadRequestException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.agregar(ciudadDTO));
    }

    @Operation(summary = "Buscar una ciudad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CiudadDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(ciudadService.buscar(id));
    }

    @Operation(summary = "Obtener todas las ciudades")
    @GetMapping
    public ResponseEntity<List<CiudadDTO>> listar()
    {
        return ResponseEntity.ok(ciudadService.listar());
    }

    @Operation(summary = "Actualizar una ciudad")
    @PutMapping
    public ResponseEntity<CiudadDTO> actualizar(@RequestBody CiudadDTO ciudadDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(ciudadService.actualizar(ciudadDTO));
    }

    @Operation(summary = "Eliminar una ciudad por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        ciudadService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("La ciudad con el ID " + id + " fue eliminada correctamente."));
    }
}
