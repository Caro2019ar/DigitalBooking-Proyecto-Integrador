package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.repository.CategoriaRepository;
import com.equipo2.Integrador.service.impl.CategoriaService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Esto es para poder usar el BeforeAll non-static
@SpringBootTest
public class CategoriaService2Tests {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Esto es para limpiar los remanentes de los tests en CategoriaService1Tests
    @BeforeAll
    public void cleanUp()
    {
        categoriaRepository.deleteAll();
    }

    @Test
    public void agregarCategoria() throws BadRequestException, ResourceConflictException
    {
        CategoriaDTO categoriaAgregadaDTO = categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        Assertions.assertNotNull(categoriaAgregadaDTO.getId());

        categoriaRepository.deleteAll();
    }

    @Test
    public void agregarCategoriaTituloRepetido() throws BadRequestException, ResourceConflictException
    {
        categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        Assertions.assertThrows(ResourceConflictException.class, () ->
                categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Descripción", "http://www.url.com")));
    }

    @Test
    public void buscarCategoria() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        CategoriaDTO categoriaAgregadaDTO = categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        Assertions.assertDoesNotThrow( () -> categoriaService.buscar(categoriaAgregadaDTO.getId()) );
        CategoriaDTO categoriaBuscadaDTO = categoriaService.buscar(categoriaAgregadaDTO.getId());
        Assertions.assertNotNull(categoriaBuscadaDTO);
        Assertions.assertEquals(categoriaAgregadaDTO.getId(), categoriaBuscadaDTO.getId());

        categoriaRepository.deleteAll();
    }

    @Test
    public void buscarCategoriaInexistente() throws BadRequestException, ResourceConflictException
    {
        categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscar(9999L));

        categoriaRepository.deleteAll();
    }

    @Test
    public void listarTodas() throws BadRequestException, ResourceConflictException
    {
        categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        categoriaService.agregar(new CategoriaDTO("Hotel de Playa", "Hoteles cercanos a balnearios", "http://www.hoteldeplaya.com"));
        categoriaService.agregar(new CategoriaDTO("Hotel en la Naturaleza", "Hoteles cercanos a selvas/bosques/montañas", "http://www.hotelnaturaleza.com"));
        List<CategoriaDTO> categoriasDTO = categoriaService.listar();
        Assertions.assertTrue(!categoriasDTO.isEmpty());
        Assertions.assertTrue(categoriasDTO.size() == 3);

        categoriaRepository.deleteAll();
    }

    @Test
    public void actualizarCategoria() throws BadRequestException, ResourceNotFoundException, ResourceConflictException
    {
        CategoriaDTO categoriaAgregadaDTO = categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        CategoriaDTO categoriaBuscadaDTO = categoriaService.buscar(categoriaAgregadaDTO.getId());
        Assertions.assertNotNull(categoriaBuscadaDTO);
        Assertions.assertEquals("http://www.hotelurbano.com", categoriaBuscadaDTO.getUrlImagen());

        categoriaBuscadaDTO.setUrlImagen("http://www.hotelmetropolitano.org");
        CategoriaDTO categoriaActualizadaDTO = categoriaService.actualizar(categoriaBuscadaDTO);
        CategoriaDTO categoriaActualizadaBuscadaDTO = categoriaService.buscar(categoriaActualizadaDTO.getId());
        Assertions.assertNotNull(categoriaActualizadaBuscadaDTO);
        Assertions.assertEquals("http://www.hotelmetropolitano.org", categoriaActualizadaBuscadaDTO.getUrlImagen());

        categoriaRepository.deleteAll();
    }

    @Test
    public void eliminarCategoria() throws ResourceNotFoundException, BadRequestException, ResourceConflictException
    {
        CategoriaDTO categoriaAgregadaDTO = categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com"));
        categoriaService.eliminar(categoriaAgregadaDTO.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> categoriaService.buscar(categoriaAgregadaDTO.getId()));

        categoriaRepository.deleteAll();
    }
}