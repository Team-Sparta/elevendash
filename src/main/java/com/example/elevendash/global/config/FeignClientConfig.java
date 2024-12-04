package com.example.elevendash.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.example.elevendash.domain.member.client"})
public class FeignClientConfig {
}