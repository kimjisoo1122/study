package com.example.springjsp.config;

import com.example.springjsp.validator.GlobalValidator;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public Validator getValidator() {
        return new GlobalValidator();
    }
}
