package com.equipo2.Integrador.DTO.mapper;

import com.equipo2.Integrador.DTO.ClienteDTO;
import com.equipo2.Integrador.DTO.UsuarioDTO;
import com.equipo2.Integrador.model.Cliente;
import com.equipo2.Integrador.model.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ClienteDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public ClienteDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public ClienteDTO toDTO(Cliente cliente)
    {
        return objectMapper.convertValue(cliente, ClienteDTO.class);
    }

    public Cliente toModel(ClienteDTO clienteDTO)
    {
        return objectMapper.convertValue(clienteDTO, Cliente.class);
    }

    public List<ClienteDTO> toDTOList(List<Cliente> clientes)
    {
        return objectMapper.convertValue(clientes, new TypeReference<List<ClienteDTO>>(){});
    }
}
