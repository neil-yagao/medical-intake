package com.neil.fpdatabase;

import com.neil.fpdatabase.controller.FingerPrintIdentityHandler;
import com.neil.fpdatabase.controller.FingerPrintRegisterHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Created by nhu on 4/6/2017.
 */
@Configuration
@EnableAutoConfiguration
@EnableWebSocket
@ComponentScan(basePackages = "com.neil")
public class FingerDatabase implements WebSocketConfigurer {

    @Value("${file.location}")
    private String fileLocation;

    public static void main(String[] args) {
        SpringApplication.run(FingerDatabase.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(registerHandler(), "/finger").setAllowedOrigins("*");
        registry.addHandler(identityHandler(), "/identity").setAllowedOrigins("*");
    }

    @Bean
    public FingerPrintRegisterHandler registerHandler() {
        return new FingerPrintRegisterHandler();
    }

    @Bean
    public FingerPrintIdentityHandler identityHandler() {
        return new FingerPrintIdentityHandler();
    }

    /**
     * for testing purpose and could be removed after real back end deployed
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/img/*.png").addResourceLocations("file:" + fileLocation + "img/");
                super.addResourceHandlers(registry);
            }
        };
    }
}