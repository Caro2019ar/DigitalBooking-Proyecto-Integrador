package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.AdminDTO;
import com.equipo2.Integrador.model.Admin;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AdminDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public AdminDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public AdminDTO toDTO(Admin admin)
    {
        return objectMapper.convertValue(admin, AdminDTO.class);
    }

    public Admin toModel(AdminDTO adminDTO)
    {
        return objectMapper.convertValue(adminDTO, Admin.class);
    }

    public List<AdminDTO> toDTOList(List<Admin> admins)
    {
        return objectMapper.convertValue(admins, new TypeReference<List<AdminDTO>>(){});
    }
}