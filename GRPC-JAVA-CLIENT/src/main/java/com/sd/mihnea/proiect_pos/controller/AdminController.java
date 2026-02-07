package com.sd.mihnea.proiect_pos.controller;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.sd.mihnea.proiect_pos.DTOs.CreateUserDTO;
import com.sd.mihnea.proiect_pos.DTOs.UserResponseDTO;
import com.sd.mihnea.proiect_pos.grpc.GrpcAuthClient;
import com.sd.mihnea.proiect_pos.grpc.SecurityUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin/users")
@CrossOrigin("*")

public class AdminController {

    private final GrpcAuthClient grpcAuthClient;
    private final RestTemplate restTemplate;


    public AdminController(GrpcAuthClient grpcAuthClient, RestTemplate restTemplate) {
        this.grpcAuthClient = grpcAuthClient;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO dto, HttpServletRequest request) {

        SecurityUtils.requireRole(request, List.of("admin"));

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        String url = "http://CLIENT-SERVICE:8081/api/clients";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(dto.getEmail(), headers);

        grpcAuthClient.createUser(
                token,
                dto.getEmail(),
                dto.getPassword(),
                dto.getRole()
        );

        if(!request.getAttribute("email").equals("admin")) {

            try {
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        Void.class
                );
            } catch (HttpClientErrorException | HttpServerErrorException ex) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY,
                        "Client service unavailable"
                );
            }

        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(HttpServletRequest request) {
        SecurityUtils.requireRole(request, List.of("admin"));

        String token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(grpcAuthClient.getAllUsers(token));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable int id,
            HttpServletRequest request
    ) {
        SecurityUtils.requireRole(request, List.of("admin"));

        String token = request.getHeader("Authorization").substring(7);
        grpcAuthClient.deleteUser(token, id);


        return ResponseEntity.noContent().build();
    }



}
