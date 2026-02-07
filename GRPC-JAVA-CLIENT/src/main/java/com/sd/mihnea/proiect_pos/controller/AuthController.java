package com.sd.mihnea.proiect_pos.controller;


import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sd.mihnea.grpcdemo.auth.ValidateResponse;
import com.sd.mihnea.proiect_pos.DTOs.LoginDTO;
import com.sd.mihnea.proiect_pos.DTOs.TokenResponseDTO;
import com.sd.mihnea.proiect_pos.grpc.GrpcAuthClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final GrpcAuthClient grpcAuthClient;

    public AuthController(GrpcAuthClient grpcAuthClient) {
        this.grpcAuthClient = grpcAuthClient;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto)
    {

        String token = grpcAuthClient.login(dto.getEmail(),dto.getPassword());

        return ResponseEntity.ok(new TokenResponseDTO(token));

    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request)
    {
        String auth = request.getHeader("Authorization");
        grpcAuthClient.logout(auth.substring(7));
        return ResponseEntity.ok(null);
    }


    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = bearerToken.substring(7);

        ValidateResponse res = grpcAuthClient.validate(token);

        if (res.getSuccess()) {
            return ResponseEntity.ok(Map.of(
                    "email", res.getEmail(),
                    "role", res.getRole(),
                    "idOwner", res.getSub()
            ));
        } else {
            return ResponseEntity.status(401).body("Invalid Token");
        }
    }
}
