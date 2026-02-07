package com.sd.mihnea.proiect_pos.grpc;


import com.sd.mihnea.grpcdemo.auth.*;
import com.sd.mihnea.proiect_pos.DTOs.UserResponseDTO;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcAuthClient {


    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub stub;

    public String login(String email, String password)
    {
        LoginResponse response = stub.login(
                LoginRequest.newBuilder()
                        .setEmail(email)
                        .setPassword(password)
                        .build()
        );

        return response.getTokenValue();
    }


    public ValidateResponse validate(String token)
    {
        return stub.validateToken(TokenRequest.newBuilder().setToken(token).build());
    }

    public void createUser(String adminToken, String email, String password, String role) {
        stub.createUser(
                CreateUserRequest.newBuilder()
                        .setAdminToken(adminToken)
                        .setEmail(email)
                        .setPassword(password)
                        .setRole(role)
                        .build()
        );
    }

    public void deleteUser(String adminToken, int userId) {
        DeleteUserRequest request = DeleteUserRequest.newBuilder()
                .setAdminToken(adminToken)
                .setUserId(userId)
                .build();

        stub.deleteUser(request);
    }

    public List<UserResponseDTO> getAllUsers(String adminToken) {
        GetAllUsersRequest request = GetAllUsersRequest.newBuilder()
                .setAdminToken(adminToken)
                .build();

        GetAllUsersResponse response = stub.getAllUsers(request);

        return response.getUsersList().stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getEmail(),
                        u.getRole()
                ))
                .toList();
    }



    public void logout(String token)
    {
        stub.logout(TokenRequest.newBuilder().setToken(token).build());
    }

}
