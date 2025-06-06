package com.bux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
@RestController
public class BuxBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuxBackendApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        return "BUX Backend is alive!";
    }
    
    @Bean
    public CommandLineRunner generateAdminHash() {
        return args -> {
            String rawPassword = "admin";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hash = encoder.encode(rawPassword);
            System.out.println(" BCrypt hash for 'admin': " + hash);
        };
    }

}