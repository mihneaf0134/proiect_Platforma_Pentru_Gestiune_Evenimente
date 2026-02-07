package com.sd.mihnea.proiect_pos.representationalmodel;

import com.sd.mihnea.proiect_pos.controller.ClientController;
import com.sd.mihnea.proiect_pos.mongo.model.Client;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RepresentationalClient
        implements RepresentationModelAssembler<Client, EntityModel<Client>> {

    @Override
    public EntityModel<Client> toModel(Client client) {

        EntityModel<Client> resource = EntityModel.of(client);

        resource.add(linkTo(methodOn(ClientController.class)
                .getidMethod(client.getEmail()))
                .withSelfRel());

        resource.add(linkTo(methodOn(ClientController.class)
                .updateClient(client.getEmail(), null,null))
                .withRel("update-client")
                .withType("PUT"));

        resource.add(linkTo(methodOn(ClientController.class)
                .deleteClient(client.getEmail(),null))
                .withRel("delete-client")
                .withType("DELETE"));

        resource.add(linkTo(methodOn(ClientController.class)
                .purchaseTicket(null, null))
                .withRel("purchase-ticket")
                .withType("POST"));

        resource.add(linkTo(methodOn(ClientController.class)
                .validareBilet(UUID.randomUUID(),null))
                .withRel("validate-ticket")
                .withType("GET"));

        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Client>> toCollectionModel(
            Iterable<? extends Client> clients) {

        List<EntityModel<Client>> clientModels = new ArrayList<>();

        for (Client c : clients) {
            clientModels.add(toModel(c));
        }

        CollectionModel<EntityModel<Client>> collection =
                CollectionModel.of(clientModels);

        collection.add(linkTo(methodOn(ClientController.class)
                .getMethod(null))
                .withSelfRel());

        return collection;
    }
}
