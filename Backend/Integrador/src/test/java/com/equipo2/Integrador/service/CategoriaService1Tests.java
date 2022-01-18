package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.repository.CategoriaRepository;
import com.equipo2.Integrador.service.impl.CategoriaService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Esto es para poder usar el BeforeAll non-static
@SpringBootTest
public class CategoriaService1Tests {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaRepository categoriaRepository;


    private Categoria categoriaCasa;
    private Categoria categoriaHotel;

    @BeforeEach
    public void agregaCategorias() throws BadRequestException, ResourceConflictException
    {
        categoriaCasa = categoriaRepository.save(Categoria.builder().titulo("Casa").descripcion("Casas increíbles").urlImagen(
                "http" + "://www.fotos.com").build());
        categoriaHotel = categoriaRepository.save(Categoria.builder().titulo("Hotel").descripcion("Hoteles increíbles").urlImagen(
                "http" + "://www.fotos.com").build());
    }

    // Esto es para limpiar los remanentes de los tests en CategoriaService2Tests
    @BeforeAll
    public void setUp()
    {
        categoriaRepository.deleteAll();
    }

    @AfterEach
    public void cleanUp()
    {
        categoriaRepository.deleteAll();
    }

    @Test
    public void agregaYListaCategorias(){
        List<CategoriaDTO> lista = categoriaService.listar();
        Assertions.assertThat(lista.size()).isEqualTo(2);
    }

    @Test
    public void buscaCategoriaPorID() throws ResourceNotFoundException
    {
        // Si la categoría existe no tira excepción y el test pasa
        CategoriaDTO resultado = categoriaService.buscar(categoriaCasa.getId());
    }

    @Test
    public void buscaPorIDyActualizaCategoria() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        CategoriaDTO categoriaNuevaDTO = categoriaService.agregar(CategoriaDTO.builder().titulo("Hotel Nuevo").descripcion(
                "Hoteles nuevos").urlImagen("http" + "://www.new.com").build());
        CategoriaDTO categoriaDTO = categoriaService.buscar(categoriaNuevaDTO.getId());
        CategoriaDTO categoriaActualizadaDTO = categoriaService.actualizar(categoriaNuevaDTO);
        Assertions.assertThat(categoriaDTO.toString()).contains(categoriaActualizadaDTO.toString());
    }

    @Test
    public void eliminaCategoriaPorID() throws ResourceNotFoundException, BadRequestException, ResourceConflictException
    {
        CategoriaDTO categoriaNuevaDTO = categoriaService.agregar(CategoriaDTO.builder().titulo("Hotel A Eliminar").descripcion(
                "Hotel a ser eliminado").urlImagen("http" + "://www.elimineme.com").build());
        categoriaService.eliminar(categoriaNuevaDTO.getId());
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscar(categoriaNuevaDTO.getId()));
    }
}
