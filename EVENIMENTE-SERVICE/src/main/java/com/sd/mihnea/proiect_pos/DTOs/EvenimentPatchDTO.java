package com.sd.mihnea.proiect_pos.DTOs;

public class EvenimentPatchDTO {

    private String nume;
    private String locatie;
    private String descriere;
    private int numarLocuri;
    private int ID_Owner;


    public EvenimentPatchDTO(String nume, String locatie, String descriere, int numarLocuri, int ID_Owner)
    {
        this.nume = nume;
        this.locatie = locatie;
        this.descriere = descriere;
        this.numarLocuri =  numarLocuri;
        this.ID_Owner = ID_Owner;
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

    public int getID_Owner() {
        return ID_Owner;
    }

    public void setID_Owner(int ID_Owner) {
        this.ID_Owner = ID_Owner;
    }
}
