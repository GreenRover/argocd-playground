package ch.sbb.tms.operator.brokerconfig.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import jakarta.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultValidatorConfig {
    @Bean
    public Validator validator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            return factory.getValidator();
        }
    }
}
