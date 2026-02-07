package com.sd.mihnea.proiect_pos.DTOs;

public class UserResponseDTO {
    private int id;
    private String email;
    private String role;

    public UserResponseDTO(int id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
