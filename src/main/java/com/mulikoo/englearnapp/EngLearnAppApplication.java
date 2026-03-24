package com.mulikoo.englearnapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EngLearnAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(EngLearnAppApplication.class, args);

    }

}
