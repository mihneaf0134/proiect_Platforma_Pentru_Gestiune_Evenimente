package com.sd.mihnea.proiect_pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoDBConf {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory,
                                       MappingMongoConverter converter) {
        return new MongoTemplate(mongoDbFactory, converter);
    }
}

