package com.equipo2.Integrador.service;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.DTO.ImagenDTO;
import com.equipo2.Integrador.DTO.ProductoDTO;
import com.equipo2.Integrador.DTO.mapper.CiudadDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.Categoria;
import com.equipo2.Integrador.model.Ciudad;
import com.equipo2.Integrador.model.Imagen;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.repository.CategoriaRepository;
import com.equipo2.Integrador.repository.CiudadRepository;
import com.equipo2.Integrador.repository.ImagenRepository;
import com.equipo2.Integrador.repository.ProductoRepository;
import com.equipo2.Integrador.service.impl.CategoriaService;
import com.equipo2.Integrador.service.impl.CiudadService;
import com.equipo2.Integrador.service.impl.ImagenService;
import com.equipo2.Integrador.service.impl.ProductoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@ActiveProfiles("test")
/*@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)*/  // Anotación para resetear le DB antes de cada test (así son independientes)
@ExtendWith(SpringExtension.class)  // Es redundante
@SpringBootTest
public class ImagenServiceTests {

    @Autowired
    private ImagenService imagenService;
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CiudadRepository ciudadRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    private Imagen imagenAgregada;
    private Producto producto;

    @BeforeEach
    public void setUp() throws BadRequestException, ResourceConflictException
    {
        producto = productoRepository.save(new Producto(
                "Producto-001",
                "Great hotel, the best of the best.",
                ciudadRepository.save(new Ciudad("Mendoza", "Argentina")),
                categoriaRepository.save(new Categoria("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "http://www.hotelurbano.com")),
                null,
                null,
                -32.888557,
                -68.850720,
                "Av. Rivadavia 1280",
                null
        ));
        imagenAgregada = imagenRepository.save(new Imagen("Imagen-001", "http://www.imagen001.com", producto));
    }

    @AfterEach
    public void cleanUp()
    {
        imagenRepository.deleteAll();
        productoRepository.deleteAll();
        ciudadRepository.deleteAll();
        categoriaRepository.deleteAll();
    }

    @Test
    public void agregarImagen() throws BadRequestException
    {
        ImagenDTO imagenDTO = imagenService.agregar(new ImagenDTO("Imagen-002", "http://www.imagen002.com", producto));
        Assertions.assertNotNull(imagenDTO.getId());
    }

    @Test
    public void agregarImagenConProductoNulo()
    {
        Assertions.assertThrows(BadRequestException.class, () -> imagenService.agregar(new ImagenDTO("Imagen-012", "http://www.imagen012.com")));
    }

    @Test
    public void buscarImagen() throws ResourceNotFoundException
    {
        Assertions.assertDoesNotThrow( () -> imagenService.buscar(imagenAgregada.getId()) );
        ImagenDTO imagenBuscadaDTO = imagenService.buscar(imagenAgregada.getId());
        Assertions.assertNotNull(imagenBuscadaDTO);
        Assertions.assertEquals(imagenAgregada.getId(), imagenBuscadaDTO.getId());
    }

    @Test
    public void buscarImagenInexistente()
    {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> imagenService.buscar(9999L));
    }

    @Test
    public void listarTodas() throws BadRequestException
    {
        imagenService.agregar(new ImagenDTO("Imagen-002", "http://www.imagen002.com", producto));
        imagenService.agregar(new ImagenDTO("Imagen-003", "http://www.imagen003.com", producto));
        List<ImagenDTO> imagenes = imagenService.listar();
        Assertions.assertTrue(!imagenes.isEmpty());
        Assertions.assertTrue(imagenes.size() == 3);
    }

    @Test
    public void actualizarImagen() throws BadRequestException, ResourceNotFoundException
    {
        ImagenDTO imagenBuscadaDTO = imagenService.buscar(imagenAgregada.getId());
        Assertions.assertNotNull(imagenBuscadaDTO);
        Assertions.assertEquals("Imagen-001", imagenBuscadaDTO.getTitulo());

        imagenBuscadaDTO.setTitulo("Nuevo-título");
        ImagenDTO imagenActualizadaDTO = imagenService.actualizar(imagenBuscadaDTO);
        ImagenDTO imagenActualizadaBuscadaDTO = imagenService.buscar(imagenActualizadaDTO.getId());
        Assertions.assertNotNull(imagenActualizadaBuscadaDTO);
        Assertions.assertEquals("Nuevo-título", imagenActualizadaBuscadaDTO.getTitulo());
    }

    @Test
    public void eliminarImagen() throws ResourceNotFoundException, BadRequestException
    {
        imagenService.eliminar(imagenAgregada.getId());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> imagenService.buscar(imagenAgregada.getId()));
    }
}
