package com.sd.mihnea.proiect_pos.representationalmodel;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.controller.ClientController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RepresentationalBilet
        implements RepresentationModelAssembler<BiletResponseDTO, EntityModel<BiletResponseDTO>> {

    @Override
    public EntityModel<BiletResponseDTO> toModel(BiletResponseDTO bilet) {

        EntityModel<BiletResponseDTO> resource = EntityModel.of(bilet);

        resource.add(linkTo(methodOn(ClientController.class)
                .getMyTickets(null))
                .withRel("my-tickets"));

        if (bilet.getCod() != null) {
            resource.add(linkTo(methodOn(ClientController.class)
                    .validareBilet(bilet.getCod(), null))
                    .withRel("validate-ticket")
                    .withType("PUT"));
        }

        return resource;
    }

    @Override
    public CollectionModel<EntityModel<BiletResponseDTO>> toCollectionModel(
            Iterable<? extends BiletResponseDTO> bilete) {

        List<EntityModel<BiletResponseDTO>> models = new ArrayList<>();

        for (BiletResponseDTO b : bilete) {
            models.add(toModel(b));
        }

        return CollectionModel.of(
                models,
                linkTo(methodOn(ClientController.class)
                        .getMyTickets(null))
                        .withSelfRel()
        );
    }
}
