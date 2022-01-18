package com.equipo2.Integrador.config;

import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.TreeMap;


@Configuration
public class SwaggerConfig {

    // Para ordenar los Schemas alfabéticamente
    @Bean
    public OpenApiCustomiser sortSchemasAlphabetically()
    {
        return openApi -> {
            Map<String, Schema> schemas = openApi.getComponents().getSchemas();
            openApi.getComponents().setSchemas(new TreeMap<>(schemas));
        };
    }
}
