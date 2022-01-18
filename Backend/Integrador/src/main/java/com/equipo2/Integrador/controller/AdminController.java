package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.AdminDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.service.impl.AdminService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Admins")
@CrossOrigin
@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }


    @Operation(summary = "Registrar un nuevo admin")
    @PostMapping
    public ResponseEntity<AdminDTO> agregar(@RequestBody AdminDTO adminDTO) throws BadRequestException, ResourceConflictException {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.agregar(adminDTO));
    }

    @Operation(summary = "Buscar un admin por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.buscar(id));
    }

    @Operation(summary = "Buscar un admin por email")
    @GetMapping("/buscar")
    public ResponseEntity<AdminDTO> buscarPorEmail(@RequestParam("email") String email) throws ResourceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.buscarPorEmail(email));
    }

    @Operation(summary = "Obtener todos los admins")
    @GetMapping
    public ResponseEntity<List<AdminDTO>> listar() {
        return ResponseEntity.ok(adminService.listar());
    }

    @Operation(summary = "Actualizar un admin")
    @PutMapping
    public ResponseEntity<AdminDTO> actualizar(@RequestBody AdminDTO adminDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException {
        return ResponseEntity.status(HttpStatus.OK).body(adminService.actualizar(adminDTO));
    }

    @Operation(summary = "Eliminar un admin por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException {
        adminService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("El admin con el ID " + id + " fue eliminado correctamente."));
    }
}