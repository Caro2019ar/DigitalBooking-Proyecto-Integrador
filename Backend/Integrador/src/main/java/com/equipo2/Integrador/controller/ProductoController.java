package com.equipo2.Integrador.controller;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Imagen;
import com.equipo2.Integrador.service.impl.ProductoService;
import com.equipo2.Integrador.util.JsonResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@Tag(name = "Productos")
@CrossOrigin
@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService)
    {
        this.productoService = productoService;
    }


    @Operation(summary = "Registrar un nuevo producto")
    @PostMapping
    public ResponseEntity<ProductoDTO> agregar(@RequestBody ProductoDTO productoDTO) throws BadRequestException
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.agregar(productoDTO));
    }

    @Operation(summary = "Buscar un producto por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> buscar(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.buscar(id));
    }

    @Operation(summary = "Obtener todos los productos")
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> listar()
    {
        return ResponseEntity.ok(productoService.listar());
    }

    @Operation(summary = "Obtener todos los productos con paginación")
    @GetMapping("/paginado")
    public ResponseEntity<ProductosPaginadosDTO> listarPaginado(@RequestParam(name = "page") Integer pagina)
    {
        return ResponseEntity.ok(productoService.listarConPaginado(pagina));
    }

    @Operation(summary = "Actualizar un producto")
    @PutMapping
    public ResponseEntity<ProductoDTO> actualizar(@RequestBody ProductoDTO productoDTO) throws BadRequestException, ResourceNotFoundException
    {
        return ResponseEntity.status(HttpStatus.OK).body(productoService.actualizar(productoDTO));
    }

    @Operation(summary = "Eliminar un producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponseMessage> eliminar(@PathVariable Long id) throws ResourceNotFoundException, BadRequestException
    {
        productoService.eliminar(id);

        // Si llega hasta acá es porque no hubo excepción
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("El producto con el ID " + id + " fue eliminado correctamente."));
    }

    @Operation(summary = "Buscar productos por ciudad y/o categoría")
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarPorCiudadYOCategoria(@RequestParam(name = "ciudad", required = false) String ciudad,
                                                                        @RequestParam(name = "categoria", required = false) String categoria)
    {
        return ResponseEntity.ok(productoService.buscarPorCiudadYOCategoria(ciudad, categoria));
    }

    @Operation(summary = "Buscar productos por ciudad y/o categoría con paginación")
    @GetMapping("/paginado/buscar")
    public ResponseEntity<ProductosPaginadosDTO> buscarPorCiudadYOCategoriaPaginado(@RequestParam(name = "ciudad", required = false) String ciudad,
                                                                                    @RequestParam(name = "categoria", required = false) String categoria,
                                                                                    @RequestParam(name = "page") Integer pagina)
    {
        return ResponseEntity.ok(productoService.buscarPorCiudadYOCategoriaPaginado(ciudad, categoria, pagina));
    }

    @Operation(summary = "Buscar productos por fechas y (opcional) ciudad")
    @GetMapping("/buscar-con-fechas")
    public ResponseEntity<List<ProductoDTO>> buscarPorCiudadYFechas(@RequestParam(name = "ciudad", required = false) String ciudad,
                                                                    @RequestParam(name = "inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                                    @RequestParam(name = "fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin)
    {
        return ResponseEntity.ok(productoService.buscarPorCiudadYFechas(ciudad, inicio, fin));
    }

    @Operation(summary = "Buscar productos por fechas y (opcional) ciudad con paginación")
    @GetMapping("/paginado/buscar-con-fechas")
    public ResponseEntity<ProductosPaginadosDTO> buscarPorCiudadYFechasPaginado(@RequestParam(name = "ciudad", required = false) String ciudad,
                                                                                @RequestParam(name = "inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                                                @RequestParam(name = "fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
                                                                                @RequestParam(name = "page") Integer pagina)
    {
        return ResponseEntity.ok(productoService.buscarPorCiudadYFechasPaginado(ciudad, inicio, fin, pagina));
    }

    @Operation(summary = "Agregar imágenes a un producto")
    @PutMapping("/{id}/agregar-imagenes")
    public ResponseEntity<ProductoDTO> agregarImagenes(@PathVariable Long id, @RequestBody List<ImagenDTO> imagenesDTO) throws ResourceNotFoundException, BadRequestException
    {
        ProductoDTO productoDTO = productoService.agregarImagenes(id, imagenesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productoDTO);
    }

    @Operation(summary = "Eliminar imágenes de un producto")
    @PutMapping("/{id}/eliminar-imagenes")
    public ResponseEntity<ProductoDTO> eliminarImagenes(@PathVariable Long id, @RequestBody List<ImagenDTO> imagenesDTO) throws ResourceNotFoundException, BadRequestException
    {
        ProductoDTO productoDTO = productoService.eliminarImagenes(id, imagenesDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productoDTO);
    }

    @Operation(summary = "Obtener todas las fechas en las que un producto no se encuentra disponible para reservar")
    @GetMapping("/{id}/fechas-no-disponible")
    public ResponseEntity<List<LocalDate>> listarFechasNoDisponibleOrdenado(@PathVariable Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(productoService.listarFechasNoDisponibleOrdenado(id));
    }

    @Operation(summary = "Obtener todos los productos favoritos de un cliente")
    @GetMapping("/favoritos")
    public ResponseEntity<List<ProductoDTO>> obtenerFavoritos(@RequestParam(name = "usuario") Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(productoService.obtenerFavoritosByUsuario(id));
    }

    @Operation(summary = "Obtener todos los productos favoritos de un cliente con paginación")
    @GetMapping("/paginado/favoritos")
    public ResponseEntity<ProductosPaginadosDTO> obtenerFavoritosPaginado(@RequestParam(name = "usuario") Long id,
                                                                          @RequestParam(name = "page") Integer pagina) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(productoService.obtenerFavoritosByUsuarioPaginado(id, pagina));
    }

    @Operation(summary = "Obtener todos los IDs de los productos favoritos de un cliente")
    @GetMapping("/favoritos-id")
    public ResponseEntity<List<Long>> obtenerFavoritosId(@RequestParam(name = "usuario") Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(productoService.obtenerFavoritosIdByUsuario(id));
    }

    @Operation(summary = "Averiguar si un producto es favorito de un cliente")
    @GetMapping("/{id}/es-favorito")
    public ResponseEntity<JsonResponseMessage> esFavoritoDelUsuario(@PathVariable Long id,
                                                                    @RequestParam(name = "usuario") Long idUsuario) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(new JsonResponseMessage(productoService.esFavoritoDelUsuario(id, idUsuario).toString()));
    }

    @Operation(summary = "Agregar puntuación de un cliente a un producto")
    @PutMapping("/{id}/agregar-puntuacion")
    public ResponseEntity<JsonResponseMessage> agregarPuntuacion(@PathVariable Long id, @RequestBody PuntuacionDTO puntuacionDTO) throws ResourceNotFoundException, BadRequestException
    {
        productoService.agregarPuntuacion(id, puntuacionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("OK"));
    }

    @Operation(summary = "Actualizar puntuación de un cliente a un producto")
    @PutMapping("/{id}/actualizar-puntuacion")
    public ResponseEntity<JsonResponseMessage> actualizarPuntuacion(@PathVariable Long id, @RequestBody PuntuacionDTO puntuacionDTO) throws ResourceNotFoundException, BadRequestException
    {
        productoService.actualizarPuntuacion(id, puntuacionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("OK"));
    }

    @Operation(summary = "Eliminar puntuación de un cliente a un producto")
    @PutMapping("/{id}/eliminar-puntuacion")
    public ResponseEntity<JsonResponseMessage> eliminarPuntuacion(@PathVariable Long id, @RequestBody PuntuacionDTO puntuacionDTO) throws ResourceNotFoundException, BadRequestException
    {
        productoService.eliminarPuntuacion(id, puntuacionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseMessage("OK"));
    }

    @Operation(summary = "Obtener productos con puntuación de un cliente")
    @GetMapping("/puntuaciones-cliente")
    public ResponseEntity<List<ProductoDTO>> obtenerByPuntuacionesUsuario(@RequestParam(name = "usuario") Long id) throws ResourceNotFoundException
    {
        return ResponseEntity.ok(productoService.obtenerByPuntuacionesUsuario(id));
    }
}