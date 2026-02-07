package com.sd.mihnea.proiect_pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.sd.mihnea.proiect_pos.mongo.repository")
public class ProiectPosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProiectPosApplication.class, args);
    }

}
