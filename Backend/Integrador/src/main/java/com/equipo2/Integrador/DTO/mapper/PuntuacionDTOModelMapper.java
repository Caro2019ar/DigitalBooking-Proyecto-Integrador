package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.PuntuacionDTO;
import com.equipo2.Integrador.model.Puntuacion;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PuntuacionDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public PuntuacionDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public PuntuacionDTO toDTO(Puntuacion puntuacion)
    {
        return objectMapper.convertValue(puntuacion, PuntuacionDTO.class);
    }

    public Puntuacion toModel(PuntuacionDTO puntuacionDTO)
    {
        return objectMapper.convertValue(puntuacionDTO, Puntuacion.class);
    }

    public List<PuntuacionDTO> toDTOList(List<Puntuacion> puntuacions)
    {
        return objectMapper.convertValue(puntuacions, new TypeReference<List<PuntuacionDTO>>(){});
    }
}