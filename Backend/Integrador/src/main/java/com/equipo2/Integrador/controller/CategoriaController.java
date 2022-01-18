package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.service.impl.CategoriaService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Categorias")
@CrossOrigin
@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService)
    {
        this.categoriaService = categoriaService;
    }


    @Operation(summary = "Registrar una nueva categoría")
    @PostMapping
    public ResponseEntity<CategoriaDTO> agregar(@RequestBody CategoriaDTO categoriaDTO) throws BadRequestException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.agregar(categoriaDTO));
    }

    @Operation(summary = "Buscar una categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.buscar(id));
    }

    @Operation(summary = "Obtener todas las categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar()
    {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Operation(summary = "Actualizar una categoría")
    @PutMapping
    public ResponseEntity<CategoriaDTO> actualizar(@RequestBody CategoriaDTO categoriaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(categoriaService.actualizar(categoriaDTO));
    }

    @Operation(summary = "Eliminar una categoría por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        categoriaService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("La categoría con el ID " + id + " fue eliminada correctamente."));
    }
}
