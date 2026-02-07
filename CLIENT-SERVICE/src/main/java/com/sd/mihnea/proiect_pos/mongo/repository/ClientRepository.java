package com.sd.mihnea.proiect_pos.mongo.repository;

import com.sd.mihnea.proiect_pos.mongo.model.Client;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends MongoRepository<Client, String> {


    Client findClientByEmail(String email);
    void deleteClientByEmail(String email);
}
