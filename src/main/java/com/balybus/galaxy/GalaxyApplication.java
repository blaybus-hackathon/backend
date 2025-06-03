package com.balybus.galaxy;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients(basePackages = "com.balybus.galaxy")
@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class GalaxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GalaxyApplication.class, args);
    }

}
