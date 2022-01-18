package com.equipo2.Integrador.service.impl;

import com.equipo2.Integrador.DTO.*;
import com.equipo2.Integrador.DTO.mapper.ImagenDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.ProductoDTOModelMapper;
import com.equipo2.Integrador.DTO.mapper.PuntuacionDTOModelMapper;
import com.equipo2.Integrador.exceptions.BadRequestException;
import com.equipo2.Integrador.exceptions.ResourceNotFoundException;
import com.equipo2.Integrador.model.*;
import com.equipo2.Integrador.repository.*;
import com.equipo2.Integrador.service.IService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
public class ProductoService implements IService<ProductoDTO, Long> {

    private final ProductoRepository productoRepository;
    private final CiudadRepository ciudadRepository;
    private final CategoriaRepository categoriaRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoDTOModelMapper productoDTOModelMapper;
    private final ImagenDTOModelMapper imagenDTOModelMapper;
    private final PuntuacionDTOModelMapper puntuacionDTOModelMapper;
    private final EntityManager entityManager;
    private final Logger logger = Logger.getLogger(ProductoService.class);

    @Autowired
    public ProductoService(ProductoRepository productoRepository, CiudadRepository ciudadRepository,
                           CategoriaRepository categoriaRepository, CaracteristicaRepository caracteristicaRepository,
                           ReservaRepository reservaRepository, ClienteRepository clienteRepository,
                           ProductoDTOModelMapper productoDTOModelMapper, ImagenDTOModelMapper imagenDTOModelMapper,
                           PuntuacionDTOModelMapper puntuacionDTOModelMapper, EntityManager entityManager)
    {
        this.productoRepository = productoRepository;
        this.ciudadRepository = ciudadRepository;
        this.categoriaRepository = categoriaRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.productoDTOModelMapper = productoDTOModelMapper;
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.imagenDTOModelMapper = imagenDTOModelMapper;
        this.puntuacionDTOModelMapper = puntuacionDTOModelMapper;
        this.entityManager = entityManager;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductoDTO agregar(ProductoDTO productoDTO) throws BadRequestException
    {

        Producto producto = productoDTOModelMapper.toModel(productoDTO);

        if (producto.getId() != null)
            throw new BadRequestException("El registro de productos no puede recibir un ID.");

        Ciudad ciudad = producto.getCiudad();
        if (ciudad == null)
            throw new BadRequestException("La ciudad no puede ser nula.");

        if (ciudad.getId() == null)
            throw new BadRequestException("El ID de la ciudad no puede ser nulo.");

        Optional<Ciudad> optionalCiudad = ciudadRepository.findById(ciudad.getId());
        if (optionalCiudad.isEmpty())
            throw new BadRequestException("La ciudad con ID " + ciudad.getId() + " no existe.");

        Categoria categoria = producto.getCategoria();
        if (categoria == null)
            throw new BadRequestException("La categoria no puede ser nula.");

        if (categoria.getId() == null)
            throw new BadRequestException("El ID de la categoria no puede ser nulo.");

        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoria.getId());
        if (optionalCategoria.isEmpty())
            throw new BadRequestException("La categoria con ID " + categoria.getId() + " no existe.");

        boolean imagenConIDFlag = false;
        if (producto.getImagenes() != null) {
            for (Imagen imagen : producto.getImagenes()) {
                if (imagen.getId() != null) {
                    imagenConIDFlag = true;
                    break;
                }
                // Necesario para que inserte la FK del producto en Imagenes y persista la relación (porque Imagen es el owner de la relacion)
                imagen.setProducto(producto);
            }
        }
        if (imagenConIDFlag)
            throw new BadRequestException("Las imágenes en el registro de productos no pueden tener un ID.");

        boolean caracteristicaSinIDFlag = false;
        boolean caracteristicaInexistente = false;
        List<Caracteristica> caracteristicasExistentes = caracteristicaRepository.findAll();
        Set<Caracteristica> caracteristicasParaElMerge = new HashSet<>();
        if (producto.getCaracteristicas() != null)
        {
            for (Caracteristica caracteristica : producto.getCaracteristicas()) {
                if (caracteristica.getId() == null) {
                    caracteristicaSinIDFlag = true;
                    break;
                }
                // Chequea si la caracteristica no existe
                Optional<Caracteristica> optionalCaracteristica = caracteristicasExistentes.stream().filter(c -> c.getId().equals(caracteristica.getId())).findAny();
                if (optionalCaracteristica.isEmpty()) {
                    caracteristicaInexistente = true;
                    break;
                } else
                    caracteristicasParaElMerge.add(optionalCaracteristica.get());
            }
        }
        if (caracteristicaSinIDFlag)
            throw new BadRequestException("Las características en el registro de productos deben tener un ID.");
        if (caracteristicaInexistente)
            throw new BadRequestException("Las características en el registro de productos deben existir previamente.");

        // Si llegó hasta acá es porque el JSON está en orden para la inserción

        // Esto es puramente para que ciudad/categoria/caracteristicas no tengan propiedades null en el JSON
        producto.setCiudad(optionalCiudad.get());
        producto.setCategoria(optionalCategoria.get());
        producto.setCaracteristicas(caracteristicasParaElMerge);

        Producto productoSaved = productoRepository.save(producto);
        logger.info("Se registró un producto con ID " + productoSaved.getId() + ".");

        return productoDTOModelMapper.toDTO(productoSaved);
    }

    @Override
    public ProductoDTO buscar(Long id) throws ResourceNotFoundException
    {
        Optional<Producto> optionalProducto = productoRepository.findById(id);

        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        return productoDTOModelMapper.toDTO(optionalProducto.get());
    }

    @Override
    public List<ProductoDTO> listar()
    {
        List<ProductoDTO> listaProductoDTO = productoDTOModelMapper.toDTOList(productoRepository.findAll());
        ordenarPorPuntuacionAgregada(listaProductoDTO);
        return listaProductoDTO;

    }

    public ProductosPaginadosDTO listarConPaginado(Integer pagina)
    {
        List<ProductoDTO> listaProductoDTO = productoDTOModelMapper.toDTOList(productoRepository.findAll());
        ordenarPorPuntuacionAgregada(listaProductoDTO);

        return convertirAListaPaginada(listaProductoDTO, pagina);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProductoDTO actualizar(ProductoDTO productoDTO) throws BadRequestException, ResourceNotFoundException
    {
        Producto producto = productoDTOModelMapper.toModel(productoDTO);

        if (producto.getId() == null)
            throw new BadRequestException("La actualización de productos requiere de un ID.");

        Optional<Producto> productoBuscado = productoRepository.findById(producto.getId());
        if (productoBuscado.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + producto.getId() + " no existe.");

        Ciudad ciudad = producto.getCiudad();
        if (ciudad == null)
            throw new BadRequestException("La ciudad no puede ser nula.");

        if (ciudad.getId() == null)
            throw new BadRequestException("El ID de la ciudad no puede ser nulo.");

        Optional<Ciudad> optionalCiudad = ciudadRepository.findById(ciudad.getId());
        if (optionalCiudad.isEmpty())
            throw new BadRequestException("La ciudad con ID " + ciudad.getId() + " no existe.");

        Categoria categoria = producto.getCategoria();
        if (categoria == null)
            throw new BadRequestException("La categoria no puede ser nula.");

        if (categoria.getId() == null)
            throw new BadRequestException("El ID de la categoria no puede ser nulo.");

        Optional<Categoria> optionalCategoria = categoriaRepository.findById(categoria.getId());
        if (optionalCategoria.isEmpty())
            throw new BadRequestException("La categoria con ID " + categoria.getId() + " no existe.");

        boolean imagenConIDFlag = false;
        if (producto.getImagenes() != null) {
            for (Imagen imagen : producto.getImagenes()) {
                if (imagen.getId() != null) {
                    imagenConIDFlag = true;
                    break;
                }
            }
        }
        if (imagenConIDFlag)
            throw new BadRequestException("Las imágenes en el registro de productos no pueden tener un ID.");

        boolean caracteristicaSinIDFlag = false;
        List<Caracteristica> caracteristicasExistentes = caracteristicaRepository.findAll();
        boolean caracteristicaInexistente = false;
        if (producto.getCaracteristicas() != null) {
            for (Caracteristica caracteristica : producto.getCaracteristicas()) {
                if (caracteristica.getId() == null) {
                    caracteristicaSinIDFlag = true;
                    break;
                }
                if (caracteristicasExistentes.stream().filter(c -> c.getId().equals(caracteristica.getId())).findAny().isEmpty()) {
                    caracteristicaInexistente = true;
                    break;
                }
            }
        }
        if (caracteristicaSinIDFlag)
            throw new BadRequestException("Las características en el registro de productos deben tener un ID.");
        if (caracteristicaInexistente)
            throw new BadRequestException("Las características en el registro de productos deben existir previamente.");

        // Esto es puramente para que caracteristicas no sea null en el JSON
        if (producto.getCaracteristicas() == null)
            producto.setCaracteristicas(new HashSet<>());

        producto.setPuntuaciones(productoBuscado.get().getPuntuaciones());

        return productoDTOModelMapper.toDTO(productoRepository.save(producto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void eliminar(Long id) throws ResourceNotFoundException, BadRequestException
    {
        if (productoRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        if (!reservaRepository.findByProductoId(id).isEmpty())
            throw new BadRequestException("El producto con el ID " + id + " no puede ser eliminado porque tiene reservas asociadas.");

        borrarTodosLosFavoritos(id);

        productoRepository.deleteById(id);
        logger.info("Se eliminó el producto con ID " + id + ".");
    }

    // Esto es para poder borrar Productos que tienen favoritos asociados (no puedo usar cascade por ser la relación unidireccional)
    private void borrarTodosLosFavoritos(Long productId)
    {
        for( Cliente cliente : clienteRepository.findAll() )
        {
            cliente.getProductosFavoritos().removeIf( producto -> producto.getId().equals(productId));
        }
    }

    public List<ProductoDTO> buscarPorCiudadYOCategoria(String ciudad, String categoria)
    {
        List<ProductoDTO> listaProductoDTO = productoDTOModelMapper.toDTOList(productoRepository.findByOptionalCiudadAndOptionalCategoria(ciudad, categoria));
        ordenarPorPuntuacionAgregada(listaProductoDTO);
        return listaProductoDTO;
    }

    public ProductosPaginadosDTO buscarPorCiudadYOCategoriaPaginado(String ciudad, String categoria, Integer pagina)
    {
        List<ProductoDTO> listaProductoDTO = productoDTOModelMapper.toDTOList(productoRepository.findByOptionalCiudadAndOptionalCategoria(ciudad, categoria));
        ordenarPorPuntuacionAgregada(listaProductoDTO);
        return convertirAListaPaginada(listaProductoDTO, pagina);
    }

    public List<ProductoDTO> buscarPorCiudadYFechas(String ciudad, LocalDate inicio, LocalDate fin)
    {
        List<ProductoDTO> listaProductoDTO = productoDTOModelMapper.toDTOList(productoRepository.findByOptionalCiudadAndFechas(ciudad, inicio, fin));
        ordenarPorPuntuacionAgregada(listaProductoDTO);
        return listaProductoDTO;
    }

    public ProductosPaginadosDTO buscarPorCiudadYFechasPaginado(String ciudad, LocalDate inicio, LocalDate fin, Integer pagina)
    {
        List<ProductoDTO> listaProductoDTO = productoDTOModelMapper.toDTOList(productoRepository.findByOptionalCiudadAndFechas(ciudad, inicio, fin));
        ordenarPorPuntuacionAgregada(listaProductoDTO);
        return convertirAListaPaginada(listaProductoDTO, pagina);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductoDTO agregarImagenes(Long id, List<ImagenDTO> imagenesDTO) throws ResourceNotFoundException, BadRequestException
    {
        List<Imagen> imagenes = imagenDTOModelMapper.toModelList(imagenesDTO);

        Optional<Producto> optionalProducto = productoRepository.findById(id);

        if (optionalProducto.isPresent())
        {
            boolean imagenConIDFlag = false;
            for( Imagen imagen : imagenes)
            {
                if (imagen.getId() == null)
                    optionalProducto.get().addImagen(imagen);
                else
                {
                    imagenConIDFlag = true;
                    break;
                }
            }
            if (imagenConIDFlag)
                throw new BadRequestException("Las imágenes a agregar no pueden tener un ID.");
        }
        else
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        // Con @Transactional no es necesario hacer el save (los cambios se persisten al manipular la entidad -por esto es necesario el rollback-)
        // De cualquier manera, el save evita los id's null de las imágenes insertadas en el JSON de respuesta
        // Pero cuando uso el save con GenerationType.IDENTITY, hay un bug de Hibernate que hace que las imágenes recién insertadas aparezcan duplicadas en la lista
        // Para evitar esto, hago un flush con el EntityManager y me ahorro el innecesario save
        // return productoDTOModelMapper.toDTO(productoRepository.save(optionalProducto.get()));
        entityManager.flush();
        return productoDTOModelMapper.toDTO(optionalProducto.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductoDTO eliminarImagenes(Long id, List<ImagenDTO> imagenesDTO) throws ResourceNotFoundException, BadRequestException
    {
        List<Imagen> imagenes = imagenDTOModelMapper.toModelList(imagenesDTO);

        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isPresent())
        {
            boolean imagenSinIDFlag = false;
            boolean imagenInexistenteEnEsteProducto = false;
            for( Imagen imagen : imagenes )
            {
                if (imagen.getId() != null)
                {
                    // Chequeo si la imagen existe en este producto (si además no existe en la tabla Imagenes no me importa)
                    Optional<Imagen> imagenMatch = optionalProducto.get().getImagenes().stream().filter(i -> i.getId().equals(imagen.getId())).findFirst();
                    if (imagenMatch.isPresent())
                        optionalProducto.get().removeImagen(imagenMatch.get());
                    else
                    {
                        imagenInexistenteEnEsteProducto = true;
                        break;
                    }
                }
                else
                {
                    imagenSinIDFlag = true;
                    break;
                }
            }
            if (imagenSinIDFlag)
                throw new BadRequestException("Las imágenes a eliminar deben tener un ID.");
            if (imagenInexistenteEnEsteProducto)
                throw new BadRequestException("Las imágenes a eliminar deben existir en este producto.");
        }
        else
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        // Con @Transactional no es necesario hacer el save (los cambios se persisten al manipular la entidad -por esto es necesario el rollback-)
        return productoDTOModelMapper.toDTO(productoRepository.save(optionalProducto.get()));
    }

    public List<LocalDate> listarFechasNoDisponibleOrdenado(Long id) throws ResourceNotFoundException
    {
        if (productoRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        List<Reserva> listaReservas = reservaRepository.findByProductoId(id);
        List<LocalDate> listaFechas = new ArrayList<>();

        // Itero todas las reservas del producto
        for( Reserva reserva : listaReservas )
        {
            // Itero sobra el rango de fechas de la reserva para insertarlas en la lista
            for(LocalDate date = reserva.getFechaInicial(); date.isBefore(reserva.getFechaFinal().plusDays(1)); date = date.plusDays(1))
            {
                listaFechas.add(date);
            }
        }

        // Ordeno la lista
        Collections.sort(listaFechas);

        return listaFechas;
    }

    public List<ProductoDTO> obtenerFavoritosByUsuario(Long id) throws ResourceNotFoundException
    {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);

        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        List<Producto> favoritos = optionalCliente.get().getProductosFavoritos();

        return productoDTOModelMapper.toDTOList(favoritos);
    }

    public ProductosPaginadosDTO obtenerFavoritosByUsuarioPaginado(Long id, Integer pagina) throws ResourceNotFoundException
    {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);

        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        List<Producto> favoritos = optionalCliente.get().getProductosFavoritos();

        return convertirAListaPaginada(productoDTOModelMapper.toDTOList(favoritos), pagina);
    }

    public List<Long> obtenerFavoritosIdByUsuario(Long id) throws ResourceNotFoundException
    {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);

        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        List<Producto> favoritos = optionalCliente.get().getProductosFavoritos();
        List<Long> listaIds = favoritos.stream().mapToLong( producto -> producto.getId() ).boxed().collect(Collectors.toList());

        return listaIds;
    }

    public Boolean esFavoritoDelUsuario(Long productoId, Long clienteId) throws ResourceNotFoundException
    {
        Optional<Producto> optionalProducto = productoRepository.findById(productoId);
        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + productoId + " no existe.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(clienteId);
        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + clienteId + " no existe.");

        return optionalCliente.get().getProductosFavoritos().contains(optionalProducto.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void agregarPuntuacion(Long id, PuntuacionDTO puntuacionDTO) throws ResourceNotFoundException, BadRequestException
    {
        // Hago estos chequeos primero para poder setear el cliente en la puntuación previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = puntuacionDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        puntuacionDTO.setCliente(optionalCliente.get());

        Puntuacion puntuacion = puntuacionDTOModelMapper.toModel(puntuacionDTO);

        if (puntuacion.getProducto() != null)
            throw new BadRequestException("No debe enviarse un producto en el body de la petición.");

        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        if (puntuacion.getId() != null)
            throw new BadRequestException("La puntuación a agregar no puede tener un ID.");

        if (puntuacion.getPuntos() < 1 || puntuacion.getPuntos() > 5)
            throw new BadRequestException("La valoración debe ser un número entero entre 1 y 5.");

        // Chequeo si el cliente ya tiene una puntuación registrada en este producto
        Optional<Puntuacion> puntuacionMatch = optionalProducto.get().getPuntuaciones().stream().filter( p -> p.getCliente().getId().equals(optionalCliente.get().getId()) ).findFirst();
        if (puntuacionMatch.isPresent())
            throw new BadRequestException("El cliente ya tiene una votación registrada en este producto.");

        optionalProducto.get().addPuntuacion(puntuacion);

        // Con @Transactional no es necesario hacer el save (los cambios se persisten al manipular la entidad -por esto es necesario el rollback-)
        productoRepository.save(optionalProducto.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void actualizarPuntuacion(Long id, PuntuacionDTO puntuacionDTO) throws ResourceNotFoundException, BadRequestException
    {
        // Hago estos chequeos primero para poder setear el cliente en la puntuación previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = puntuacionDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        puntuacionDTO.setCliente(optionalCliente.get());

        Puntuacion puntuacion = puntuacionDTOModelMapper.toModel(puntuacionDTO);

        if (puntuacion.getProducto() != null)
            throw new BadRequestException("No debe enviarse un producto en el body de la petición.");

        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        if (puntuacion.getId() != null)
            throw new BadRequestException("La puntuación a actualizar sólo debe recibir ID del cliente.");

        if (puntuacion.getPuntos() < 1 || puntuacion.getPuntos() > 5)
            throw new BadRequestException("La valoración debe ser un número entero entre 1 y 5.");

        // Chequeo si el cliente tiene una puntuación registrada en este producto para poder actualizarla
        Optional<Puntuacion> puntuacionMatch = optionalProducto.get().getPuntuaciones().stream().filter( p -> p.getCliente().getId().equals(optionalCliente.get().getId()) ).findFirst();
        if (puntuacionMatch.isEmpty())
            throw new BadRequestException("El cliente no tiene una votación registrada en este producto.");

        puntuacionMatch.get().setPuntos(puntuacion.getPuntos());

        // Con @Transactional no es necesario hacer el save (los cambios se persisten al manipular la entidad -por esto es necesario el rollback-)
        productoRepository.save(optionalProducto.get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void eliminarPuntuacion(Long id, PuntuacionDTO puntuacionDTO) throws ResourceNotFoundException, BadRequestException
    {
        // Hago estos chequeos primero para poder setear el cliente en la puntuación previo al mapeo
        // (de lo contrario, Jackson tira error en los campos null)
        Cliente cliente = puntuacionDTO.getCliente();
        if(cliente == null)
            throw new BadRequestException("El cliente no puede ser nulo");

        if (cliente.getId() == null)
            throw new BadRequestException("El ID del cliente no puede ser nulo.");

        Optional<Cliente> optionalCliente = clienteRepository.findById(cliente.getId());
        if (optionalCliente.isEmpty())
            throw new BadRequestException("El cliente con ID " + cliente.getId() + " no existe.");

        // Hago el seteo del cliente
        puntuacionDTO.setCliente(optionalCliente.get());

        Puntuacion puntuacion = puntuacionDTOModelMapper.toModel(puntuacionDTO);

        if (puntuacion.getProducto() != null)
            throw new BadRequestException("No debe enviarse un producto en el body de la petición.");

        Optional<Producto> optionalProducto = productoRepository.findById(id);
        if (optionalProducto.isEmpty())
            throw new ResourceNotFoundException("El producto con el ID " + id + " no existe.");

        // Chequeo si el cliente tiene una puntuación registrada en este producto para poder eliminarla
        Optional<Puntuacion> puntuacionMatch = optionalProducto.get().getPuntuaciones().stream().filter(i -> i.getCliente().getId().equals(optionalCliente.get().getId())).findFirst();
        if (puntuacionMatch.isEmpty())
            throw new BadRequestException("El cliente no tiene una votación registrada en este producto.");

        optionalProducto.get().removePuntuacion(puntuacionMatch.get());

        // Con @Transactional no es necesario hacer el save (los cambios se persisten al manipular la entidad -por esto es necesario el rollback-)
        productoRepository.save(optionalProducto.get());
    }


    private void ordenarPorPuntuacionAgregada(List<ProductoDTO> lista)
    {
        class ProductoDTOComparator implements Comparator<ProductoDTO>
        {
            @Override
            public int compare(ProductoDTO p1, ProductoDTO p2)
            {
                double promedio1 =  (p1.getValoracion().getCantidadVotos() == 0) ? 0.0
                        : p1.getValoracion().getPuntajeTotal().doubleValue() / p1.getValoracion().getCantidadVotos().doubleValue();
                double promedio2 =  (p2.getValoracion().getCantidadVotos() == 0) ? 0.0
                        : p2.getValoracion().getPuntajeTotal().doubleValue() / p2.getValoracion().getCantidadVotos().doubleValue();
                return Double.compare(promedio2, promedio1);
            }
        }

        lista.sort(new ProductoDTOComparator());
    }

    private ProductosPaginadosDTO convertirAListaPaginada(List<ProductoDTO> listaProductoDTO, Integer pagina)
    {
        ProductosPaginadosDTO listaPaginada = new ProductosPaginadosDTO();
        listaPaginada.setResultadosTotales(listaProductoDTO.size());
        if (listaProductoDTO.size() != 0)
        {
            Integer terminarEn = listaPaginada.getResultadosPorPagina() * pagina;
            Integer empezarDesde = terminarEn - listaPaginada.getResultadosPorPagina();
            if (terminarEn > listaProductoDTO.size())
                terminarEn = listaProductoDTO.size();
            listaProductoDTO.subList(empezarDesde, terminarEn);
            listaPaginada.setProductos(listaProductoDTO.subList(empezarDesde, terminarEn));
        }

        return listaPaginada;
    }

    public List<ProductoDTO> obtenerByPuntuacionesUsuario(Long id) throws ResourceNotFoundException
    {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);

        if (optionalCliente.isEmpty())
            throw new ResourceNotFoundException("El cliente con el ID " + id + " no existe.");

        List<Producto> puntuacionesCliente = productoRepository.findByPuntuacionesCliente(id);

        return productoDTOModelMapper.toDTOList(puntuacionesCliente);
    }
}