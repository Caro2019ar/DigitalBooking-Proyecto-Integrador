package com.equipo2.Integrador.DTO.mapper;


import com.equipo2.Integrador.DTO.RolDTO;
import com.equipo2.Integrador.model.Rol;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RolDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public RolDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public RolDTO toDTO(Rol rol)
    {
        return objectMapper.convertValue(rol, RolDTO.class);
    }

    public Rol toModel(RolDTO rolDTO)
    {
        return objectMapper.convertValue(rolDTO, Rol.class);
    }

    public List<RolDTO> toDTOList(List<Rol> rols)
    {
        return objectMapper.convertValue(rols, new TypeReference<List<RolDTO>>(){});
    }
}
