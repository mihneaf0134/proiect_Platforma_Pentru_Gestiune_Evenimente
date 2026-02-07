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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.BiletValidatDTO;
import com.sd.mihnea.proiect_pos.DTOs.ClientInfoDTO;
import com.sd.mihnea.proiect_pos.config.SecurityUtils;
import com.sd.mihnea.proiect_pos.mongo.model.Client;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalBilet;
import com.sd.mihnea.proiect_pos.representationalmodel.RepresentationalClient;
import com.sd.mihnea.proiect_pos.services.ClientService.ClientService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")

public class ClientController {

    private final ClientService clientService;
    private final RepresentationalClient representationalClient;
    private final RepresentationalBilet representationalBilet;

    public ClientController(ClientService clientService, RepresentationalClient representationalClient, RepresentationalBilet representationalBilet) {
        this.clientService = clientService;
        this.representationalClient = representationalClient;
        this.representationalBilet = representationalBilet;
    }


    @GetMapping("")
    public ResponseEntity<CollectionModel<EntityModel<Client>>> getMethod(HttpServletRequest request)
    {
        SecurityUtils.requireRole(request,List.of("owner-event","admin"));
        return ResponseEntity.status(HttpStatus.OK).body(representationalClient.toCollectionModel(clientService.getClients()));
    }


    @GetMapping(params = "email")
    public ResponseEntity<EntityModel<Client>> getidMethod(@RequestParam(required = false) String email)
    {

        Client client = clientService.getClient(email);
        return ResponseEntity.status(HttpStatus.OK).body(representationalClient.toModel(client));

    }

    @GetMapping("me")
    public ResponseEntity<EntityModel<Client>> getMe(HttpServletRequest request)
    {

        return ResponseEntity.status(HttpStatus.OK).body(representationalClient.toModel(clientService.getMe(SecurityUtils.getEmail(request))));

    }

    @PostMapping("")
    public ResponseEntity<EntityModel<Client>> createClient(@RequestBody String email)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(representationalClient.toModel(clientService.createClient(email)));
    }

    @PutMapping("/{email}")
    public ResponseEntity<EntityModel<Client>> updateClient(@PathVariable String email, @RequestBody ClientInfoDTO clientInfoDTO, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("admin","client"));

        if(request.getAttribute("role").equals("client"))
        {
            if(request.getAttribute("email")!=email)
            {
                throw new IllegalArgumentException("NOT ALLOWED");
            }
        }


        return ResponseEntity.status(HttpStatus.OK).body(representationalClient.toModel(clientService.updateClient(email, clientInfoDTO)));
    }


    @DeleteMapping("/{email}")
    public ResponseEntity<?> deleteClient(@PathVariable String email, HttpServletRequest request)
    {
        SecurityUtils.requireRole(request, List.of("admin","client"));

        clientService.deleteClient(email);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }



    @PostMapping("/purchase-ticket/")
    public ResponseEntity<String> purchaseTicket(@RequestBody BiletResponseDTO biletResponseDTO, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request,List.of("client"));

        String token = request.getHeader("Authorization");

        clientService.purchaseBilet((String) request.getAttribute("email"),biletResponseDTO.getEvenimentId(),biletResponseDTO.getPachetId(), token);

        return ResponseEntity.status(HttpStatus.CREATED).body("Successfully Purchased the ticket!");

    }


    @GetMapping("/me/tickets")
    public ResponseEntity<CollectionModel<EntityModel<BiletResponseDTO>>> getMyTickets(
            HttpServletRequest request) {

        SecurityUtils.requireRole(request, List.of("client"));

        String email = (String) request.getAttribute("email");

        return ResponseEntity.ok(
                representationalBilet.toCollectionModel(clientService.getMyTickets(email,request.getHeader("Authorization")))
        );
    }



    @PutMapping("validate-ticket/{id}")
    public ResponseEntity<BiletValidatDTO> validareBilet(@PathVariable UUID id, HttpServletRequest request)
    {

        SecurityUtils.requireRole(request, List.of("client"));

        String token = request.getHeader("Authorization");

        return ResponseEntity.status(HttpStatus.OK).body(clientService.validareBilet(id,token));

    }

}
