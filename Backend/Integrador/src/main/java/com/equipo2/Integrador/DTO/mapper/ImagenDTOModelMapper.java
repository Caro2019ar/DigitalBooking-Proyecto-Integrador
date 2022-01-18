package com.equipo2.Integrador.DTO.mapper;


import com.equipo2.Integrador.DTO.CaracteristicaDTO;
import com.equipo2.Integrador.DTO.ImagenDTO;
import com.equipo2.Integrador.model.Caracteristica;
import com.equipo2.Integrador.model.Imagen;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ImagenDTOModelMapper {

    ObjectMapper objectMapper;

    @Autowired
    public ImagenDTOModelMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public ImagenDTO toDTO(Imagen imagen)
    {
        return objectMapper.convertValue(imagen, ImagenDTO.class);
    }

    public Imagen toModel(ImagenDTO imagenDTO)
    {
        return objectMapper.convertValue(imagenDTO, Imagen.class);
    }

    public List<ImagenDTO> toDTOList(List<Imagen> imagens)
    {
        return objectMapper.convertValue(imagens, new TypeReference<List<ImagenDTO>>(){});
    }

    public List<Imagen> toModelList(List<ImagenDTO> imagenesDTO)
    {
        return objectMapper.convertValue(imagenesDTO, new TypeReference<List<Imagen>>(){});
    }
}
