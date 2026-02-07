package com.sd.mihnea.proiect_pos.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.BiletValidatDTO;
import com.sd.mihnea.proiect_pos.grpc.SecurityUtils;
import com.sd.mihnea.proiect_pos.model.Bilete;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalBilet;
import com.sd.mihnea.proiect_pos.services.BileteService.BileteService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/event-manager/tickets")
@CrossOrigin("*")

public class BileteController {


    private final BileteService bileteService;
    private final RepresentationalBilet representationalBilet;

    public BileteController(BileteService bileteService, RepresentationalBilet representationalBilet) {
        this.bileteService = bileteService;
        this.representationalBilet = representationalBilet;
    }


    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<BiletResponseDTO>>> getBilete()
    {
        List<Bilete> bilete = bileteService.getAllBilete();

        List<BiletResponseDTO> DTOLIST = new ArrayList<>();

        for(Bilete i: bilete)
        {
            DTOLIST.add(new BiletResponseDTO(i));
        }

        return ResponseEntity.status(HttpStatus.OK).body(representationalBilet.toCollectionModel(DTOLIST));
    }


    @GetMapping("/{cod}")
    public ResponseEntity<EntityModel<BiletResponseDTO>> getBiletebyID(@PathVariable UUID cod)
    {

        Bilete bilete = bileteService.getBiletById(cod);
        BiletResponseDTO biletResponseDTO = new BiletResponseDTO(bilete);

        return ResponseEntity.status(HttpStatus.OK).body(representationalBilet.toModel(biletResponseDTO));
    }


    @PostMapping("")
    public ResponseEntity<EntityModel<BiletResponseDTO>> createBilet(@Valid @RequestBody BiletResponseDTO biletedto, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("client"));

        Bilete bilete = bileteService.createBilet(biletedto);

        BiletResponseDTO biletResponseDTO = new BiletResponseDTO(bilete);

        return ResponseEntity.status(HttpStatus.CREATED).body(representationalBilet.toModel(biletResponseDTO));
    }

    @DeleteMapping("/{cod}")
    public ResponseEntity<?> deleteBilete(@PathVariable UUID cod)
    {

        bileteService.deleteBilet(cod);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @PutMapping("validate-ticket/{cod}")
    public ResponseEntity<BiletValidatDTO> validareBilet(@PathVariable UUID cod, HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("client"));

        String token = request.getHeader("Authorization");

        return ResponseEntity.status(HttpStatus.OK).body(bileteService.validateBilet(cod, token));
    }



}
