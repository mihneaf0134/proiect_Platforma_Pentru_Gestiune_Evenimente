package com.sd.mihnea.proiect_pos.representationalmodel;

import com.sd.mihnea.proiect_pos.controller.EvenimenteController;
import com.sd.mihnea.proiect_pos.controller.PacheteController;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RepresentationalPachete implements RepresentationModelAssembler<Pachete, EntityModel<Pachete>> {


    @Override
    public EntityModel<Pachete> toModel(Pachete pachete) {
        EntityModel<Pachete> resource = EntityModel.of(pachete);

        resource.add(linkTo(methodOn(PacheteController.class).getByID(pachete.getID(),null)).withSelfRel());
        resource.add(linkTo(methodOn(PacheteController.class).getEvenimenteForPachet(pachete.getID(),null)).withRel("event-packets").withType("GET"));
        resource.add(linkTo(methodOn(PacheteController.class).deletePachet(pachete.getID(),null)).withRel("delete").withType("DELETE"));


        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Pachete>> toCollectionModel(Iterable<? extends Pachete> entities) {
        List<EntityModel<Pachete>> pacheteModels = new ArrayList<>();
        for (Pachete p : entities) {
            pacheteModels.add(toModel(p));
        }

        CollectionModel<EntityModel<Pachete>> collection = CollectionModel.of(pacheteModels);

        collection.add(linkTo(methodOn(PacheteController.class).getPac(null)).withSelfRel());

        return collection;
    }

}
