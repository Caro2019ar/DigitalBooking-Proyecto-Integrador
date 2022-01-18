package com.equipo2.Integrador.DTO.mapper;


import com.equipo2.Integrador.DTO.ReservaDTO;
import com.equipo2.Integrador.model.Reserva;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ReservaDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public ReservaDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public ReservaDTO toDTO(Reserva reserva)
    {
        return objectMapper.convertValue(reserva, ReservaDTO.class);
    }

    public Reserva toModel(ReservaDTO reservaDTO)
    {
        return objectMapper.convertValue(reservaDTO, Reserva.class);
    }

    public List<ReservaDTO> toDTOList(List<Reserva> reservas)
    {
        return objectMapper.convertValue(reservas, new TypeReference<List<ReservaDTO>>(){});
    }
}
