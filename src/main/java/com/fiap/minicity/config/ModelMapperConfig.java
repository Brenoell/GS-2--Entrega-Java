package com.fiap.minicity.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    //o MODELMAPPER foi configurado propriamente, porem como foi utilizado Record nos DTOs foi gerado um conflito, entao foi optado não utilizar.
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}