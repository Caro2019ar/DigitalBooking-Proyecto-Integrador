package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.ProductoDTO;
import com.equipo2.Integrador.DTO.PuntuacionAgregadaDTO;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.model.Puntuacion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Component
public class ProductoDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public ProductoDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public ProductoDTO toDTO(Producto producto)
    {
        ProductoDTO productoDTO = objectMapper.convertValue(producto, ProductoDTO.class);
        productoDTO.setValoracion(calcularValoracion(producto));
        return productoDTO;
    }

    public Producto toModel(ProductoDTO productoDTO)
    {
        return objectMapper.convertValue(productoDTO, Producto.class);
    }

    public List<ProductoDTO> toDTOList(Collection<Producto> productos)
    {
        return convertToDTOListConValoracion(productos);
    }

    private PuntuacionAgregadaDTO calcularValoracion(Producto producto)
    {
        Long puntajeTotal = 0L;
        for( Puntuacion puntuacion : producto.getPuntuaciones() )
        {
            puntajeTotal += puntuacion.getPuntos();
        }
        Integer cantidadVotos = producto.getPuntuaciones().size();
        return new PuntuacionAgregadaDTO(puntajeTotal, cantidadVotos);
    }

    private List<ProductoDTO> convertToDTOListConValoracion(Collection<Producto> listaProductos)
    {
        List<ProductoDTO> listaProductoDTO = new ArrayList<>();
        for( Producto producto : listaProductos )
        {
            ProductoDTO productoDTO = this.toDTO(producto);
            productoDTO.setValoracion(calcularValoracion(producto));
            listaProductoDTO.add(productoDTO);
        }

        return listaProductoDTO;
    }
}