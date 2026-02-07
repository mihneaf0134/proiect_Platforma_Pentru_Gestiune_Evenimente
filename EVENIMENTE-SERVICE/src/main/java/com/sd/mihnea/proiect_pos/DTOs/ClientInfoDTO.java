package com.sd.mihnea.proiect_pos.DTOs;

import java.util.List;

public class ClientInfoDTO {


    private String email;
    private String nume;
    private String prenume;
    private List<String> social_media;


    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public List<String> getSocial_media() {
        return social_media;
    }

    public void setSocial_media(List<String> social_media) {
        this.social_media = social_media;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
