package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.service.impl.CaracteristicaService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Caracteristicas")
@CrossOrigin
@RestController
@RequestMapping("/caracteristicas")
public class CaracteristicaController {

    private final CaracteristicaService caracteristicaService;

    @Autowired
    public CaracteristicaController(CaracteristicaService caracteristicaService)
    {
        this.caracteristicaService = caracteristicaService;
    }


    @Operation(summary = "Registrar una nueva característica")
    @PostMapping
    public ResponseEntity<CaracteristicaDTO> agregar(@RequestBody CaracteristicaDTO caracteristicaDTO) throws BadRequestException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(caracteristicaService.agregar(caracteristicaDTO));
    }

    @Operation(summary = "Buscar una característica por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CaracteristicaDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(caracteristicaService.buscar(id));
    }

    @Operation(summary = "Obtener todas las características")
    @GetMapping
    public ResponseEntity<List<CaracteristicaDTO>> listar()
    {
        return ResponseEntity.ok(caracteristicaService.listar());
    }

    @Operation(summary = "Actualizar una característica")
    @PutMapping
    public ResponseEntity<CaracteristicaDTO> actualizar(@RequestBody CaracteristicaDTO caracteristicaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(caracteristicaService.actualizar(caracteristicaDTO));
    }

    @Operation(summary = "Eliminar una característica")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        caracteristicaService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("La característica con el ID " + id + " fue eliminada correctamente."));
    }
}
