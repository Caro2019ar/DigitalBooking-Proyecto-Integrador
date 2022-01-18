package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.CategoriaDTO;
import com.equipo2.Integrador.model.Categoria;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public CategoriaDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public CategoriaDTO toDTO(Categoria categoria)
    {
        return objectMapper.convertValue(categoria, CategoriaDTO.class);
    }

    public Categoria toModel(CategoriaDTO categoriaDTO)
    {
        return objectMapper.convertValue(categoriaDTO, Categoria.class);
    }

    public List<CategoriaDTO> toDTOList(List<Categoria> categorias)
    {
        return objectMapper.convertValue(categorias, new TypeReference<List<CategoriaDTO>>(){});
    }
}
