package com.a5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.a5")
@EnableCaching 
@EntityScan("com.a5.model")
@EnableJpaRepositories("com.a5.repository")
public class A5Application {
    public static void main(String[] args) {
        SpringApplication.run(A5Application.class, args);
    }
}