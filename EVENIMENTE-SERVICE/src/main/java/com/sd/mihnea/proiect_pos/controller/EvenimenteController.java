package com.sd.mihnea.proiect_pos.controller;


import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.EvenimentPatchDTO;
import com.sd.mihnea.proiect_pos.grpc.SecurityUtils;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalBilet;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalEveniment;
import com.sd.mihnea.proiect_pos.services.EvenimenteService.EvenimenteService;
import com.sd.mihnea.proiect_pos.services.EvenimenteService.EvenimenteServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/event-manager/events")
@CrossOrigin("*")
public class EvenimenteController {

    private final RepresentationalEveniment representationalEveniment;

    private final EvenimenteService evenimenteService;
    private final RepresentationalBilet representationalBilet;


    public EvenimenteController(RepresentationalEveniment representationalEveniment, EvenimenteServiceImpl evenimenteService, RepresentationalBilet representationalBilet, SecurityUtils securityUtils) {

        this.representationalEveniment = representationalEveniment;
        this.evenimenteService = evenimenteService;
        this.representationalBilet = representationalBilet;
    }

    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<Evenimente>>> getAllEv(HttpServletRequest request)
    {

        List<Evenimente> evenimenteList = evenimenteService.findAllEvents();

       List<EntityModel<Evenimente>> resources = new ArrayList<>();

       for(Evenimente e : evenimenteList)
       {
           resources.add(representationalEveniment.toModel(e));
       }

        CollectionModel<EntityModel<Evenimente>> collectionModel = CollectionModel.of(resources);

        collectionModel.add(linkTo(methodOn(EvenimenteController.class).getAllEv(request)).withSelfRel());


        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);
    }

//    @GetMapping("myclients")
//    public ResponseEntity<CollectionModel<EntityModel<?>>> getMyClients(HttpServletRequest request)
//    {
//
//        return ResponseEntity.status(HttpStatus.OK).body(representationalClient.toCollectionModel(evenimenteService.getClientsByEv(SecurityUtils.getUserId(request))));
//
//    }


    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Evenimente>> getEv(@PathVariable Integer id, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event","client"));

        Evenimente evenimente = evenimenteService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(representationalEveniment.toModel(evenimente));

    }

    @GetMapping("myEvents")
    public ResponseEntity<CollectionModel<EntityModel<Evenimente>>> getMyEvents(HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(
                representationalEveniment.toCollectionModel(evenimenteService.getOwnersEvents(SecurityUtils.getUserId(request))));
    }


    @PostMapping("")
    public ResponseEntity<EntityModel<Evenimente>> createPOSTEvenimente(@Valid @RequestBody Evenimente evenimente, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        Integer ownerId = SecurityUtils.getUserId(request);
        evenimente.setID_OWNER(ownerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(representationalEveniment.toModel(evenimenteService.createPOST(evenimente)));

    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Evenimente>> createPUTEvenimente(@PathVariable int id, @Valid @RequestBody Evenimente evenimente, HttpServletRequest request) throws AccessDeniedException {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalEveniment.toModel(evenimenteService.createPUT(id,evenimente,SecurityUtils.getUserId(request))));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Evenimente>> updateEvenimente(@PathVariable Integer id, @RequestBody EvenimentPatchDTO evenimentPatchDTO, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));


        return ResponseEntity.status(HttpStatus.OK).body(representationalEveniment.toModel(evenimenteService.update(id,evenimentPatchDTO,SecurityUtils.getUserId(request))));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEveniment(@PathVariable int id, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));


        evenimenteService.deleteEvent(id, SecurityUtils.getUserId(request));


        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/{id}/event-packets")
    public ResponseEntity<List<Pachete>> getPacheteForEveniment(@PathVariable int id) {

        return ResponseEntity.status(HttpStatus.OK).body(evenimenteService.getPacheteForEvent(id));

    }

    @PutMapping("/{eventId}/event-packets/{packetId}")
    public ResponseEntity<?> linkEvenimentToPachet(@PathVariable int eventId, @PathVariable int packetId,HttpServletRequest request) {


        SecurityUtils.requireRole(request, List.of("owner-event"));

        Integer ownerId = SecurityUtils.getUserId(request);


        return ResponseEntity.status(HttpStatus.OK).body(evenimenteService.linkEventToPachet(eventId,packetId,ownerId));

    }

    @DeleteMapping("/{eventId}/event-packets/{packetId}")
    public ResponseEntity<?> unlinkEvenimentFromPachet(@PathVariable int eventId, @PathVariable int packetId, HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("owner-event"));
        evenimenteService.unlinkEventFromPachet(eventId,packetId, SecurityUtils.getUserId(request));
        return ResponseEntity.status(HttpStatus.OK).build();

    }


    @GetMapping("/{id}/tickets/{cod}")
    public ResponseEntity<EntityModel<BiletResponseDTO>> getTicketForEvent(@PathVariable int id, @PathVariable UUID cod, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalBilet.toModel(evenimenteService.getTicketForEvent(id,cod)));

    }


    @GetMapping("/{id}/tickets")
    public ResponseEntity<CollectionModel<EntityModel<BiletResponseDTO>>> getTicketsForEvent(@PathVariable int id, HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalBilet.toCollectionModel(evenimenteService.getTicketsForEvent(id)));
    }


    @GetMapping(params="location")
    public ResponseEntity<CollectionModel<EntityModel<Evenimente>>> searchbyLoc(@RequestParam(required = false) String location, HttpServletRequest request)
    {

        List<Evenimente> evenimenteList = evenimenteService.searchByLocation(location);

        List<EntityModel<Evenimente>> resources = new ArrayList<>();

        for(Evenimente e : evenimenteList)
        {
            resources.add(representationalEveniment.toModel(e));
        }

        CollectionModel<EntityModel<Evenimente>> collectionModel = CollectionModel.of(resources);

        collectionModel.add(linkTo(methodOn(EvenimenteController.class).getAllEv(request)).withSelfRel());


        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

    }


    @GetMapping(params="name")
    public ResponseEntity<CollectionModel<EntityModel<Evenimente>>> searchbyName(@RequestParam(required = false) String name, HttpServletRequest request)
    {

        List<Evenimente> evenimenteList = evenimenteService.searchByName(name);

        List<EntityModel<Evenimente>> resources = new ArrayList<>();

        for(Evenimente e : evenimenteList)
        {
            resources.add(representationalEveniment.toModel(e));
        }

        CollectionModel<EntityModel<Evenimente>> collectionModel = CollectionModel.of(resources);

        collectionModel.add(linkTo(methodOn(EvenimenteController.class).getAllEv(request)).withSelfRel());


        return ResponseEntity.status(HttpStatus.OK).body(collectionModel);

    }

}
