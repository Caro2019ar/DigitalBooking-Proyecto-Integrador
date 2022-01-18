package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.PuntuacionDTO;
import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.service.impl.PuntuacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Puntuaciones")
@CrossOrigin
@RestController
@RequestMapping("/puntuaciones")
public class PuntuacionController {

    private final PuntuacionService puntuacionService;

    @Autowired
    public PuntuacionController(PuntuacionService puntuacionService)
    {
        this.puntuacionService = puntuacionService;
    }


    @Operation(summary = "Buscar una puntuación por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PuntuacionDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(puntuacionService.buscar(id));
    }

    @Operation(summary = "Obtener todas las puntuaciones")
    @GetMapping
    public ResponseEntity<List<PuntuacionDTO>> listar()
    {
        return ResponseEntity.ok(puntuacionService.listar());
    }

    @Operation(summary = "Obtener (si existe) la puntuación de un cliente a un producto")
    @GetMapping("/buscar")
    public ResponseEntity<List<PuntuacionDTO>> buscarPorProductoIdYOUsuarioId(@RequestParam(name = "producto", required = false) Long idProducto,
                                                                              @RequestParam (name = "usuario", required = false) Long idCliente) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(puntuacionService.buscarPorProductoIdYOClienteId(idProducto, idCliente));
    }
}