package com.neil.medical;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by nhu on 3/31/2017.
 */
@Configuration
@EnableAutoConfiguration
@EnableIntegration
@ComponentScan(basePackages = "com.neil")
public class MedicalMatching {

    @Value("${file.location}")
    private String fileLocation;


    public static void main(String[] args) {
        SpringApplication.run(MedicalMatching.class, args);
    }

    /**
     * for testing purpose and could be removed after real back end deployed
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                super.addResourceHandlers(registry);
                registry.addResourceHandler("/img/head/*.png")
                        .addResourceLocations("file:" + fileLocation + "img/head/");
                registry.addResourceHandler("/video/*.webm")
                        .addResourceLocations("file:" + fileLocation + "video/");
            }
        };
    }
}
