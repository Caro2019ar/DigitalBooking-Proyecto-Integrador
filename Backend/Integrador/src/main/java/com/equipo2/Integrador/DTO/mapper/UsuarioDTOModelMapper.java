package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.model.Producto;
import com.equipo2.Integrador.model.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UsuarioDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public UsuarioDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public UsuarioDTO toDTO(Usuario usuario)
    {
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

    public Usuario toModel(UsuarioDTO usuarioDTO)
    {
        return objectMapper.convertValue(usuarioDTO, Usuario.class);
    }

    public List<UsuarioDTO> toDTOList(List<Usuario> usuarios)
    {
        return objectMapper.convertValue(usuarios, new TypeReference<List<UsuarioDTO>>(){});
    }
}