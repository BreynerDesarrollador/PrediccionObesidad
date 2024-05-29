package com.PrediccionObesidad.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Agrega los orígenes permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Agrega los métodos HTTP permitidos
                        .allowedHeaders("*"); // Agrega los encabezados permitidos
            }
        };
    }
}
