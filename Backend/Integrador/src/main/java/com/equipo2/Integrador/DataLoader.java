package com.equipo2.Integrador;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.DTO.mapper.*;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceConflictException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.service.impl.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


// Estos datos se usan para testear en Postman y para probar el Frontend

@Profile("!test")
@Component
public class DataLoader implements ApplicationRunner {

    private final CategoriaService categoriaService;
    private final CiudadService ciudadService;
    private final CaracteristicaService caracteristicaService;
    private final ProductoService productoService;
    private final RolService rolService;
    private final UsuarioService usuarioService;
    private final AdminService adminService;
    private final ClienteService clienteService;
    private final ReservaService reservaService;
    private final PuntuacionService puntuacionService;
    private final CategoriaDTOModelMapper categoriaDTOModelMapper;
    private final CiudadDTOModelMapper ciudadDTOModelMapper;
    private final CaracteristicaDTOModelMapper caracteristicaDTOModelMapper;
    private final ProductoDTOModelMapper productoDTOModelMapper;
    private final RolDTOModelMapper rolDTOModelMapper;
    private final ClienteDTOModelMapper clienteDTOModelMapper;
    private final DataSource dataSource;
    private final Logger logger = Logger.getLogger(DataLoader.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    public DataLoader(CategoriaService categoriaService, CiudadService ciudadService, CaracteristicaService caracteristicaService,
                      ProductoService productoService, RolService rolService, UsuarioService usuarioService, AdminService adminService,
                      ClienteService clienteService, ReservaService reservaService, PuntuacionService puntuacionService,
                      CategoriaDTOModelMapper categoriaDTOModelMapper, CiudadDTOModelMapper ciudadDTOModelMapper,
                      CaracteristicaDTOModelMapper caracteristicaDTOModelMapper, ProductoDTOModelMapper productoDTOModelMapper,
                      RolDTOModelMapper rolDTOModelMapper, ClienteDTOModelMapper clienteDTOModelMapper, DataSource dataSource)
    {
        this.categoriaService = categoriaService;
        this.ciudadService = ciudadService;
        this.caracteristicaService = caracteristicaService;
        this.productoService = productoService;
        this.rolService = rolService;
        this.usuarioService = usuarioService;
        this.adminService = adminService;
        this.clienteService = clienteService;
        this.reservaService = reservaService;
        this.puntuacionService = puntuacionService;
        this.categoriaDTOModelMapper = categoriaDTOModelMapper;
        this.ciudadDTOModelMapper = ciudadDTOModelMapper;
        this.caracteristicaDTOModelMapper = caracteristicaDTOModelMapper;
        this.productoDTOModelMapper = productoDTOModelMapper;
        this.rolDTOModelMapper = rolDTOModelMapper;
        this.clienteDTOModelMapper = clienteDTOModelMapper;
        this.dataSource = dataSource;
    }


    @Transactional
    @Override
    public void run(ApplicationArguments args)
    {
        try {

            String dbURL = dataSource.getConnection().getMetaData().getURL();

            if (dbURL.contains("h2"))
            {
                if (rolService.listar().isEmpty()) {
                    try {
                        rolService.agregar(new RolDTO("ROLE_ADMIN"));
                        rolService.agregar(new RolDTO("ROLE_CUSTOMER"));
                        rolService.agregar(new RolDTO("ROLE_VENDEDOR"));
                    } catch (BadRequestException | ResourceConflictException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban los roles.");
                    }
                }

                if (usuarioService.listar().isEmpty()) {
                    try {
                        adminService.agregar(new AdminDTO("Admin", "Admin", "admin@gmail.com", "admin", true));
                        clienteService.agregar(new ClienteDTO("Ana Laura", "Ramírez García", "anaru@hotmail.com", "soyanaruyamolosgatos", true));
                        clienteService.agregar(new ClienteDTO("Fernanda", "Elaskar", "ferchu@yahoo.com", "tedesarmoelauto", true));
                        usuarioService.agregar(new UsuarioDTO("Tito", "Sánchez", "tito_sanchez@outlook.com", "soytito", rolDTOModelMapper.toModel(rolService.buscar(3L)), true));
                    } catch (BadRequestException | ResourceConflictException | ResourceNotFoundException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban los usuarios.");
                    }
                }

                if (categoriaService.listar().isEmpty()) {
                    try {
                        categoriaService.agregar(new CategoriaDTO("Hotel Urbano", "Hoteles cercanos al centro de la ciudad", "https://static1.eskypartners.com/travelguide/vancouver-hotels.jpg"));
                        categoriaService.agregar(new CategoriaDTO("Hotel de Playa", "Hoteles cercanos a balnearios", "https://imagenes.elpais.com/resizer/1LtHwgTxnLAuPkChqlN9zpZs1to=/414x0/cloudfront-eu-central-1.images.arcpublishing.com/prisa/PZHPU56HUHMDZPWGNYLY2ZHWLM.jpg"));
                        categoriaService.agregar(new CategoriaDTO("Hotel en la Naturaleza", "Hoteles cercanos a selvas/bosques/montañas", "https://aws.admagazine.com/prod/designs/v1/assets/767x384/81139.jpg"));
                    } catch (BadRequestException | ResourceConflictException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban las categorías.");
                    }
                }

                if (ciudadService.listar().isEmpty()) {
                    try {
                        ciudadService.agregar(new CiudadDTO("Mendoza", "Argentina"));
                        ciudadService.agregar(new CiudadDTO("Cartagena", "Colombia"));
                        ciudadService.agregar(new CiudadDTO("Quito", "Ecuador"));
                    } catch (BadRequestException | ResourceConflictException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban las ciudades.");
                    }
                }

                if (caracteristicaService.listar().isEmpty()) {
                    try {
                        caracteristicaService.agregar(new CaracteristicaDTO("Pileta", "fas fa-swimmer"));
                        caracteristicaService.agregar(new CaracteristicaDTO("WiFi", "fas fa-wifi"));
                        caracteristicaService.agregar(new CaracteristicaDTO("Desayuno", "fas fa-bacon"));
                    } catch (BadRequestException | ResourceConflictException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban las características.");
                    }
                }

                if (productoService.listar().isEmpty()) {
                    try {
                        productoService.agregar(new ProductoDTO(
                                "Producto-001",
                                "Great hotel, the best of the best.",
                                ciudadDTOModelMapper.toModel(ciudadService.buscar(1L)),
                                categoriaDTOModelMapper.toModel(categoriaService.buscar(2L)),
                                new ArrayList<>(Arrays.asList(new Imagen("Imagen-001", "https://realestatemarket.com.mx/images/2020/07-Julio/0507/hoteles_cancun_.jpg"), new Imagen("Imagen-002", "https://mexicorutamagica.mx/wp-content/uploads/2021/02/hoteles-en-cancun.jpg"), new Imagen("Imagen-003", "https://content.r9cdn.net/himg/7f/bb/e5/arbisoftimages-645819-SEVCU_MasterSuite_Bedroom_5-image.jpg"), new Imagen("imagen-004", "https://www.riu.com/images/hotels/2801.jpg"), new Imagen("imagen-005", "https://exp.cdn-hotels.com/hotels/17000000/16420000/16419100/16419073/57d43b53_z.jpg?impolicy=fcrop&w=500&h=333&q=medium"))),
                                new HashSet<>(caracteristicaDTOModelMapper.toModelList(Arrays.asList(caracteristicaService.buscar(2L), caracteristicaService.buscar(3L)))),
                                -32.888557,
                                -68.850720,
                                "Av. Rivadavia 1280",
                                new Politica("Normas 01. Normas 2. Normas 3.", "Salud y Seguridad 01. Salud y Seguridad 02. Salud y Seguridad 03.", "Cancelación 01. Cancelación 02. Cancelación 03.")
                        ));
                        productoService.agregar(new ProductoDTO(
                                "Producto-002",
                                "Good hotel, but there are better ones.",
                                ciudadDTOModelMapper.toModel(ciudadService.buscar(3L)),
                                categoriaDTOModelMapper.toModel(categoriaService.buscar(1L)),
                                new ArrayList<>(Arrays.asList(new Imagen("Imagen-001", "https://concepto.de/wp-content/uploads/2018/08/Londres-e1533855310803.jpg"), new Imagen("Imagen-002", "https://concepto.de/wp-content/uploads/2018/08/Tokio-e1533855427944.jpg"))),
                                new HashSet<>(caracteristicaDTOModelMapper.toModelList(Arrays.asList(caracteristicaService.buscar(1L)))),
                                -0.204205,
                                -78.487900,
                                "Paraguay 243",
                                new Politica("Normas 01. Normas 2. Normas 3.", "Salud y Seguridad 01. Salud y Seguridad 02. Salud y Seguridad 03.", "Cancelación 01. Cancelación 02. Cancelación 03.")
                        ));
                        productoService.agregar(new ProductoDTO(
                                "Producto-003",
                                "Bad hotel, run for your life.",
                                ciudadDTOModelMapper.toModel(ciudadService.buscar(1L)),
                                categoriaDTOModelMapper.toModel(categoriaService.buscar(1L)),
                                new ArrayList<>(Arrays.asList(new Imagen("Imagen-003", "https://static.abc.es/media/MM/2020/10/21/chateau-marmont-kEuH--912x900@abc.jpg"))),
                                null,
                                -32.859130,
                                -68.861314,
                                "Bolivar 975",
                                new Politica("Normas 01. Normas 2. Normas 3.", "Salud y Seguridad 01. Salud y Seguridad 02. Salud y Seguridad 03.", "Cancelación 01. Cancelación 02. Cancelación 03.")
                        ));
                    } catch (BadRequestException | ResourceNotFoundException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban los productos.");
                    }

                    try {
                        clienteService.agregarFavorito(2L, productoService.buscar(1L));
                        clienteService.agregarFavorito(2L, productoService.buscar(2L));
                        clienteService.agregarFavorito(3L, productoService.buscar(1L));
                    } catch (BadRequestException | ResourceNotFoundException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban los favoritos.");
                    }

                    if (puntuacionService.listar().isEmpty()) {
                        try {
                            productoService.agregarPuntuacion(
                                    1L,
                                    new PuntuacionDTO(null, clienteDTOModelMapper.toModel(clienteService.buscar(2L)), 4)
                            );
                            productoService.agregarPuntuacion(
                                    1L,
                                    new PuntuacionDTO(null, clienteDTOModelMapper.toModel(clienteService.buscar(3L)), 2)
                            );
                            productoService.agregarPuntuacion(
                                    2L,
                                    new PuntuacionDTO(null, clienteDTOModelMapper.toModel(clienteService.buscar(2L)), 1)
                            );
                        } catch (BadRequestException | ResourceNotFoundException e) {
                            logger.debug("Ha ocurrido un error mientras se registraban las puntuaciones.");
                        }
                    }
                }

                if (reservaService.listar().isEmpty()) {
                    try {
                        reservaService.agregar(new ReservaDTO(
                                LocalTime.of(9, 0),
                                LocalDate.of(2021, 12, 14),
                                LocalDate.of(2021, 12, 19),
                                productoDTOModelMapper.toModel(productoService.buscar(1L)),
                                clienteDTOModelMapper.toModel(clienteService.buscar(2L)),
                                "Rosario",
                                "Nunca tuve covid y no planeo tenerlo. Si me lo agarro, me la banco.",
                                false
                        ));
                        reservaService.agregar(new ReservaDTO(
                                LocalTime.of(12, 0),
                                LocalDate.of(2021, 12, 23),
                                LocalDate.of(2021, 12, 30),
                                productoDTOModelMapper.toModel(productoService.buscar(1L)),
                                clienteDTOModelMapper.toModel(clienteService.buscar(2L)),
                                "Santiago de Chile",
                                "Tuve covid 4 veces. Voy a salir en el Libro Guinness de récords mundiales.",
                                false
                        ));
                        reservaService.agregar(new ReservaDTO(
                                LocalTime.of(15, 0),
                                LocalDate.of(2021, 12, 18),
                                LocalDate.of(2021, 12, 27),
                                productoDTOModelMapper.toModel(productoService.buscar(2L)),
                                clienteDTOModelMapper.toModel(clienteService.buscar(2L)),
                                "Pilar",
                                "Tuve covid una vez. No la pasé tan mal, pero ahora estoy más tranquila porque ya estoy vacunada.",
                                true
                        ));
                    } catch (BadRequestException | ResourceNotFoundException e) {
                        logger.debug("Ha ocurrido un error mientras se registraban las reservas.");
                    }
                }
            }

        } catch (SQLException e) {
            logger.debug("Ocurrió un error intentando obtener la URL de la BD.");
        }



        // Código que usé para probar la diferencia entre las relaciones One-to-Many unidireccionales y las bidireccionales
        /*
        Producto producto = new Producto("producto1", "el mejor producto", new ArrayList<Imagen>());
        entityManager.persist(producto);

        Imagen imagen = new Imagen("imagen1", "www.imagen1.com");

        producto.addImagen(imagen);
        entityManager.persist(imagen);
        */
    }
}
