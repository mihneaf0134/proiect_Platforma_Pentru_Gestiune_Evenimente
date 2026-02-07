package com.sd.mihnea.proiect_pos.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import com.sd.mihnea.proiect_pos.DTOs.PachetPatchDTO;
import com.sd.mihnea.proiect_pos.grpc.SecurityUtils;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalBilet;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalEveniment;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalPachete;
import com.sd.mihnea.proiect_pos.services.PacheteService.PacheteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/event-manager/event-packets")
@CrossOrigin("*")
public class PacheteController {


    private final PacheteService pacheteService;
    private final RepresentationalPachete representationalPachete;
    private final RepresentationalEveniment representationalEveniment;
    private final RepresentationalBilet representationalBilet;

    public PacheteController(PacheteService pacheteService, RepresentationalPachete representationalPachete, RepresentationalEveniment representationalEveniment, RepresentationalBilet representationalBilet)
    {

        this.pacheteService = pacheteService;
        this.representationalPachete = representationalPachete;
        this.representationalEveniment = representationalEveniment;
        this.representationalBilet = representationalBilet;
    }


    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<Pachete>>> getPac(HttpServletRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body(representationalPachete.toCollectionModel(pacheteService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Pachete>> getByID(@PathVariable int id, HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("client","owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalPachete.toModel(pacheteService.findById(id)));
    }

    @GetMapping("MyPackets")
    public ResponseEntity<CollectionModel<EntityModel<Pachete>>> getMyPackets(HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("owner-event"));
        return ResponseEntity.status(HttpStatus.OK).body(
                representationalPachete.toCollectionModel(pacheteService.findMyPachete(SecurityUtils.getUserId(request))));

    }

    @PostMapping("")
    public ResponseEntity<EntityModel<Pachete>> storePOSTPachete(@Valid @RequestBody Pachete pachete, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        Integer ownerId = SecurityUtils.getUserId(request);
        pachete.setID_OWNER(ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(representationalPachete.toModel(pacheteService.createPOST(pachete)));

    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Pachete>> storePUTPachete(@PathVariable Integer id,@Valid @RequestBody Pachete pachete, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.CREATED).body(representationalPachete.toModel(pacheteService.createPUT(id,pachete, SecurityUtils.getUserId(request))));

    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Pachete>> update(@PathVariable int id, @RequestBody PachetPatchDTO pachetPatchDTO, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalPachete.toModel(pacheteService.update(id,pachetPatchDTO, SecurityUtils.getUserId(request))));

    }

   @DeleteMapping("/{id}")
    public ResponseEntity<Pachete> deletePachet(@PathVariable int id, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        pacheteService.delete(id, SecurityUtils.getUserId(request));


        return ResponseEntity.status(HttpStatus.OK).build();

    }



    @GetMapping("/{id}/events")
    public ResponseEntity<CollectionModel<EntityModel<Evenimente>>> getEvenimenteForPachet(@PathVariable int id, HttpServletRequest request) {


        SecurityUtils.requireRole(request, List.of("owner-event","client"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalEveniment.toCollectionModel(pacheteService.getEventsForPacket(id)));

    }


    @GetMapping("/{id}/tickets/{cod}")
    public ResponseEntity<EntityModel<BiletResponseDTO>> getTicketForEvent(@PathVariable int id, @PathVariable UUID cod, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalBilet.toModel(pacheteService.getBiletForPacket(id,cod)));

    }


    @GetMapping("/{id}/tickets")
    public ResponseEntity<CollectionModel<EntityModel<BiletResponseDTO>>> getTicketsForEvent(@PathVariable int id, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalBilet.toCollectionModel(pacheteService.getBileteForPacket(id)));
    }



    @GetMapping(params = {"page","itemNumbers"})
    public ResponseEntity<CollectionModel<EntityModel<Pachete>>> pageShow(@RequestParam(required = false) int page, @RequestParam(required = false) int itemNumbers, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event","client"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalPachete.toCollectionModel(pacheteService.findPaged(page,itemNumbers)));

    }

    @GetMapping(params = "available_tickets")
    public ResponseEntity<CollectionModel<EntityModel<Pachete>>> SearchAvTickets(@RequestParam(required = false) int available_tickets, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event","client"));


        return ResponseEntity.status(HttpStatus.OK).body(representationalPachete.toCollectionModel(pacheteService.filterByAvailableTickets(available_tickets)));

    }

    @GetMapping(params = "type")
    public ResponseEntity<CollectionModel<EntityModel<Pachete>>> SearchByWords(@RequestParam(required = false) String type, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("owner-event","client"));

        return ResponseEntity.status(HttpStatus.OK).body(representationalPachete.toCollectionModel(pacheteService.filterByDescriptionContains(type)));

    }
}
