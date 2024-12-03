package com.example.elevendash.global.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

    @Bean
    public BCrypt.Hasher hasher() {
        return BCrypt.withDefaults();
    }

    @Bean
    public BCrypt.Verifyer verifyer() {
        return BCrypt.verifyer();
    }
}