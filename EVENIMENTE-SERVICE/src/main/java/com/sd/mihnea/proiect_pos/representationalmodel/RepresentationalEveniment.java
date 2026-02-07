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
public class RepresentationalEveniment implements RepresentationModelAssembler<Evenimente, EntityModel<Evenimente>> {


    @Override
    public EntityModel<Evenimente> toModel(Evenimente evenimente) {

        EntityModel<Evenimente> resource = EntityModel.of(evenimente);

        resource.add(linkTo(methodOn(EvenimenteController.class).getEv(evenimente.getID(),null)).withSelfRel());
        resource.add(linkTo(methodOn(EvenimenteController.class).getPacheteForEveniment(evenimente.getID())).withRel("event-packets").withType("GET"));
        resource.add(linkTo(methodOn(EvenimenteController.class).linkEvenimentToPachet(evenimente.getID(), 0,null)).withRel("link").withType("POST"));
        resource.add(linkTo(methodOn(EvenimenteController.class).deleteEveniment(evenimente.getID(),null)).withRel("delete").withType("DELETE"));


        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Evenimente>> toCollectionModel(Iterable<? extends Evenimente> entities) {
        List<EntityModel<Evenimente>> evenimenteModels = new ArrayList<>();
        for (Evenimente p : entities) {
            evenimenteModels.add(toModel(p));
        }

        CollectionModel<EntityModel<Evenimente>> collection = CollectionModel.of(evenimenteModels);

        collection.add(linkTo(methodOn(EvenimenteController.class).getAllEv(null)).withSelfRel());

        return collection;
    }

}
