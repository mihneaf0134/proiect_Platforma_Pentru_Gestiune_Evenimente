package com.sd.mihnea.proiect_pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sd.mihnea.proiect_pos.repository")
public class ProiectPosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProiectPosApplication.class, args);
    }

}
