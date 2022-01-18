package com.equipo2.Integrador.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/*
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/**")
                .addResourceLocations("/")
                .setCachePeriod(0);
    }
}
*/

/*
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/booking-hotels/build/static/");
        registry.addResourceHandler("/*.js")
                .addResourceLocations("/booking-hotels/build/");
        registry.addResourceHandler("/*.jsx")
                .addResourceLocations("/booking-hotels/build/");
        registry.addResourceHandler("/*.json")
                .addResourceLocations("/booking-hotels/build/");
        registry.addResourceHandler("/*.ico")
                .addResourceLocations("/booking-hotels/build/");
        registry.addResourceHandler("/*.png")
                .addResourceLocations("/booking-hotels/build/");
        registry.addResourceHandler("/*.svg")
                .addResourceLocations("/booking-hotels/build/");
        registry.addResourceHandler("/index.html")
                .addResourceLocations("/booking-hotels/build/index.html");
    }
}*/