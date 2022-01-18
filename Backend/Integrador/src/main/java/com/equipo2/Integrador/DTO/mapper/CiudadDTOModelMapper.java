package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.CiudadDTO;
import com.equipo2.Integrador.model.Ciudad;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CiudadDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public CiudadDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public CiudadDTO toDTO(Ciudad ciudad)
    {
        return objectMapper.convertValue(ciudad, CiudadDTO.class);
    }

    public Ciudad toModel(CiudadDTO ciudadDTO)
    {
        return objectMapper.convertValue(ciudadDTO, Ciudad.class);
    }

    public List<CiudadDTO> toDTOList(List<Ciudad> ciudads)
    {
        return objectMapper.convertValue(ciudads, new TypeReference<List<CiudadDTO>>(){});
    }
}