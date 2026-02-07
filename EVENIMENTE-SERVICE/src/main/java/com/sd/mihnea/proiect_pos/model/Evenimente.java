package com.sd.mihnea.proiect_pos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.websocket.server.PathParam;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Evenimente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private int ID_OWNER;

    @NotBlank
    @Unique
    private String nume;

    @NotBlank
    private String locatie;

    private String descriere;

    @Column(name = "numarLocuri")
    @PositiveOrZero
    private int numarLocuri;

    @ManyToMany
    @JoinTable(
            name = "join_pe",
            joinColumns = @JoinColumn(name = "EvenimentID"),
            inverseJoinColumns = @JoinColumn(name = "PachetID")
    )

    @JsonIgnore
    private List<Pachete> pachete = new ArrayList<>();


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

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

    public int getNumarLocuri() {
        return numarLocuri;
    }

    public void setNumarLocuri(int numarLocuri) {
        this.numarLocuri = numarLocuri;
    }

    public List<Pachete> getPachete() {
        return pachete;
    }

    public void setPachete(List<Pachete> pachete) {
        this.pachete = pachete;
    }
}
