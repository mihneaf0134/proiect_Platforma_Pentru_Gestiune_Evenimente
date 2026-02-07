package com.sd.mihnea.proiect_pos.DTOs;

import com.sd.mihnea.proiect_pos.model.Evenimente;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.ArrayList;
import java.util.List;

public class PachetPatchDTO {

    private int ID_OWNER;

    private String nume;

    private String locatie;

    private String descriere;

    public int getID_OWNER() {
        return ID_OWNER;
    }

    public void setID_OWNER(int ID_OWNER) {
        this.ID_OWNER = ID_OWNER;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
}
