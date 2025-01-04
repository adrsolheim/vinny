package no.vinny.nightfly.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer  {

    @Autowired
    private Cors cors;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(new String[]{
                        "http://127.0.0.1:3000",
                        "http://localhost:3000",
                        "http://127.0.0.1:5500",
                        "http://localhost:5500",
                        "http://127.0.0.1:4173",
                        "http://localhost:4173",
                        "http://127.0.0.1:5173",
                        "http://localhost:5173",
                })
                .allowedMethods(new String[]{"GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS", "HEAD"})
                .maxAge(3600);
    }

    @Component
    @ConfigurationProperties(prefix = "web.cors")
    public static class Cors {
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private int maxAge;
    }
}
