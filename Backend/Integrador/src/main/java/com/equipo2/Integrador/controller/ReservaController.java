package com.equipo2.Integrador.controller;


import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Reserva;
import com.equipo2.Integrador.service.impl.ReservaService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import com.equipo2.Integrador.util.OnRegistrationCompleteEvent;
import com.equipo2.Integrador.util.OnReservationCompleteEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;


@Tag(name = "Reservas")
@CrossOrigin
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ReservaController(ReservaService reservaService, ApplicationEventPublisher eventPublisher)
    {
        this.reservaService = reservaService;
        this.eventPublisher = eventPublisher;
    }


    @Operation(summary = "Registrar una nueva reserva")
    @PostMapping
    public ResponseEntity<ReservaDTO> agregar(@RequestBody ReservaDTO reservaDTO, HttpServletRequest request) throws ResourceConflictException, BadRequestException
    {
        ReservaDTO reservaRegistrada = reservaService.agregar(reservaDTO);
        System.out.println("entraaaaaaaaaaaaaaaa"); System.out.println(reservaRegistrada);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        System.out.println(baseUrl);

        Thread emailSendThread = new Thread(() -> {
            eventPublisher.publishEvent(new OnReservationCompleteEvent(reservaRegistrada, request.getLocale(), baseUrl));
        });
        emailSendThread.start();

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaRegistrada);
    }

    @Operation(summary = "Buscar todas las reservas por cliente y/o producto")
    @GetMapping("/buscar")
    public ResponseEntity<List<ReservaDTO>> buscarPorProductoIdYOUsuarioId(@RequestParam(name = "producto", required = false) Long idProducto,
                                                                           @RequestParam (name = "usuario", required = false) Long idCliente) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(reservaService.buscarPorProductoIdYOClienteId(idProducto, idCliente));
    }

    @Operation(summary = "Buscar una reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> buscarPorId(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.buscar(id));
    }

    @Operation(summary = "Obtener todas las reservas")
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listar()
    {
        return ResponseEntity.ok(reservaService.listar());
    }

    @Operation(summary = "Actualizar una reserva")
    @PutMapping
    public ResponseEntity<ReservaDTO> actualizar(@RequestBody ReservaDTO reservaDTO) throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        return ResponseEntity.status(HttpStatus.OK).body(reservaService.actualizar(reservaDTO));
    }

    @Operation(summary = "Eliminar una reserva por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        reservaService.eliminar(id);
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("La reserva con el ID " + id + " fue eliminada correctamente."));
    }
}
