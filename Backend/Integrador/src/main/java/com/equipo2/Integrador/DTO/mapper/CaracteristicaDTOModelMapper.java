package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.model.Caracteristica;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaracteristicaDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public CaracteristicaDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public CaracteristicaDTO toDTO(Caracteristica caracteristica)
    {
        return objectMapper.convertValue(caracteristica, CaracteristicaDTO.class);
    }

    public Caracteristica toModel(CaracteristicaDTO caracteristicaDTO)
    {
        return objectMapper.convertValue(caracteristicaDTO, Caracteristica.class);
    }

    public List<CaracteristicaDTO> toDTOList(List<Caracteristica> caracteristicas)
    {
        return objectMapper.convertValue(caracteristicas, new TypeReference<List<CaracteristicaDTO>>(){});
    }

    public List<Caracteristica> toModelList(List<CaracteristicaDTO> caracteristicasDTO)
    {
        return objectMapper.convertValue(caracteristicasDTO, new TypeReference<List<Caracteristica>>(){});
    }
}
