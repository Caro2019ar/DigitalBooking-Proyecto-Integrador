package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.ImagenDTO;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Imagen;
import com.equipo2.Integrador.service.impl.ImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Imagenes")
@CrossOrigin
@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    @Autowired
    public ImagenController(ImagenService imagenService)
    {
        this.imagenService = imagenService;
    }

    // Este endpoint no se usa porque las imágenes deben registrarse mediante el endpoint de productos
    /*@Operation(summary = "Registrar una nueva imagen")
    @PostMapping
    public ResponseEntity<ImagenDTO> agregar(@RequestBody ImagenDTO imagenDTO) throws BadRequestException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(imagenService.agregar(imagenDTO));
    }*/

    @Operation(summary = "Buscar una imagen por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ImagenDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(imagenService.buscar(id));
    }

    @Operation(summary = "Obtener todas las imágenes")
    @GetMapping
    public ResponseEntity<List<ImagenDTO>> listar()
    {
        return ResponseEntity.ok(imagenService.listar());
    }

    // Este endpoint no se usa porque las imágenes deben actualizarse mediante el endpoint de productos
    /*@Operation(summary = "Actualizar una imagen")
    @PutMapping
    public ResponseEntity<ImagenDTO> actualizar(@RequestBody ImagenDTO imagenDTO) throws BadRequestException, ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(imagenService.actualizar(imagenDTO));
    }*/

    // Este endpoint no se usa porque las imágenes deben eliminarse mediante el endpoint de productos
    /*@Operation(summary = "Eliminar una imagen por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        imagenService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponse("La imagen con el ID " + id + " fue eliminada correctamente."));
    }*/
}
