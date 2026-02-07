package com.sd.mihnea.proiect_pos.mongo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document("Client")
public class Client {

    @Id
    @JsonIgnore
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String email;

    private String prenume;

    private String nume;

    private List<String> social_media;

    private List<UUID> bileteList;


    public Client()
    {
        prenume = "";
        nume = "";
        social_media = new ArrayList<>();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<String> getSocial_media() {
        return social_media;
    }

    public void setSocial_media(List<String> social_media) {
        this.social_media = social_media;
    }

    public List<UUID> getBileteList() {
        return bileteList;
    }

    public void setBileteList(List<UUID> bileteList) {
        this.bileteList = bileteList;
    }
}
