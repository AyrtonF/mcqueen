package com.join.Mcqueen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração web para personalizar comportamento do Spring MVC
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configuração de CORS para permitir requisições de diferentes origens
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    /**
     * Configuração de mapeamento de paths para garantir que as rotas da API não
     * sejam tratadas como recursos estáticos
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // Garantir que paths da API sejam tratados corretamente
        configurer.setPathMatcher(new org.springframework.util.AntPathMatcher());
    }
}