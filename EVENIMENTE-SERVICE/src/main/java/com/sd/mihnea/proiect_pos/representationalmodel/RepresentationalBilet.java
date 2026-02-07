package com.sd.mihnea.proiect_pos.representationalmodel;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.controller.BileteController;
import com.sd.mihnea.proiect_pos.model.Bilete;
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
public class RepresentationalBilet implements RepresentationModelAssembler<BiletResponseDTO, EntityModel<BiletResponseDTO>> {

    @Override
    public EntityModel<BiletResponseDTO> toModel(BiletResponseDTO bilet) {

        EntityModel<BiletResponseDTO> resource = EntityModel.of(bilet);

        UUID cod = bilet.getCod();

        resource.add(linkTo(methodOn(BileteController.class).getBiletebyID(cod)).withSelfRel());

        resource.add(linkTo(methodOn(BileteController.class).deleteBilete(cod))
                .withRel("delete")
                .withType("DELETE"));

        resource.add(linkTo(methodOn(BileteController.class).validareBilet(cod,null))
                .withRel("validate")
                .withType("PUT"));

        return resource;
    }

    @Override
    public CollectionModel<EntityModel<BiletResponseDTO>> toCollectionModel(Iterable<? extends BiletResponseDTO> entities) {

        List<EntityModel<BiletResponseDTO>> models = new ArrayList<>();
        for (BiletResponseDTO b : entities) {
            models.add(toModel(b));
        }

        CollectionModel<EntityModel<BiletResponseDTO>> collection = CollectionModel.of(models);

        collection.add(linkTo(methodOn(BileteController.class).getBilete()).withSelfRel());

        return collection;
    }


}
