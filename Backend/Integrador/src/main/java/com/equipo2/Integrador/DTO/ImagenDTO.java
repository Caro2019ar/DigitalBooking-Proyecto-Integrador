package com.equipo2.Integrador.DTO;


import com.equipo2.Integrador.model.Producto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Schema(name = "Imagen")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImagenDTO {

    private Long id;
    private String titulo;
    private String url;

    @Schema(hidden = true)
    @JsonIgnoreProperties({ "descripcion", "ciudad", "categoria", "imagenes", "caracteristicas",
                            "latitud", "longitud", "direccion", "politica", "puntuaciones" })
    private Producto producto;


    // Se usa sólo para tests
    public ImagenDTO(String titulo, String url)
    {
        this.titulo = titulo;
        this.url = url;
        this.producto = null;
    }

    public ImagenDTO(String titulo, String url, Producto producto)
    {
        this.titulo = titulo;
        this.url = url;
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "ImagenDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", url='" + url + '\'' +
                ", producto=" + producto +
                '}';
    }
}
