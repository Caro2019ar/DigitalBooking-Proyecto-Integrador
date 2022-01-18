package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.PoliticaDTO;
import com.equipo2.Integrador.model.Politica;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PoliticaDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public PoliticaDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public PoliticaDTO toDTO(Politica politica)
    {
        return objectMapper.convertValue(politica, PoliticaDTO.class);
    }

    public Politica toModel(PoliticaDTO politicaDTO)
    {
        return objectMapper.convertValue(politicaDTO, Politica.class);
    }

    public List<PoliticaDTO> toDTOList(List<Politica> politicas)
    {
        return objectMapper.convertValue(politicas, new TypeReference<List<PoliticaDTO>>(){});
    }
}
