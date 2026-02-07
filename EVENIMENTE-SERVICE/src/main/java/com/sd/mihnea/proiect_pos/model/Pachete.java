package com.sd.mihnea.proiect_pos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.IdGeneratorType;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Entity

public class Pachete{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private int ID_OWNER;

    @NotBlank
    private String nume;

    @NotBlank
    private String locatie;

    private String descriere;

    @ManyToMany(mappedBy = "pachete")
    private List<Evenimente> evenimente = new ArrayList<>();

    @PositiveOrZero
    private int numarLocuri = 0;



    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getID_OWNER() {
        return ID_OWNER;
    }

    public void setID_OWNER(Integer ID_OWNER) {
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

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    public List<Evenimente> getEvenimente() {
        return evenimente;
    }

    public void setEvenimente(List<Evenimente> evenimente) {
        this.evenimente = evenimente;
    }

    public void updateNumarLocuri()
    {
        this.setNumarLocuri(this.getEvenimente().get(0).getNumarLocuri());

        for(Evenimente evenimente : this.getEvenimente())
        {
            if(evenimente.getNumarLocuri() < this.getNumarLocuri())
            {
                this.setNumarLocuri(evenimente.getNumarLocuri());
            }
        }

    }
}
