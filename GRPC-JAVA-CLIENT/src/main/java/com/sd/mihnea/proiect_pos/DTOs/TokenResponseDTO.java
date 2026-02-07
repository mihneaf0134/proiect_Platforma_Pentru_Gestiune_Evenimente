package com.sd.mihnea.proiect_pos.DTOs;

public class TokenResponseDTO {

    private String token;


    public TokenResponseDTO(String token)
    {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

}
