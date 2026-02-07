package com.sd.mihnea.proiect_pos.DTOs;

import com.sd.mihnea.proiect_pos.model.Bilete;
import org.springframework.hateoas.server.core.Relation;

import java.util.UUID;


@Relation(collectionRelation = "bilete", itemRelation = "bilet")

public class BiletResponseDTO {

    private UUID cod;

    private Integer evenimentId;

    private Integer pachetId;

    public BiletResponseDTO() {}

    public BiletResponseDTO(UUID cod, Integer EvenimentId, Integer PachetId)
    {
        this.cod = cod;
        this.evenimentId = EvenimentId;
        this.pachetId = PachetId;
    }

    public BiletResponseDTO(Bilete bilete)
    {
        this.cod = bilete.getCOD();

        if(bilete.getEvenimente() != null)
            this.evenimentId = bilete.getEvenimente().getID();
        else if(bilete.getPachete() != null)
                this.pachetId = bilete.getPachete().getID();

    }

    public void setCod(UUID cod)
    {
        this.cod = cod;
    }

    public UUID getCod() {
        return cod;
    }

    public Integer getEvenimentId() {
        return evenimentId;
    }

    public Integer getPachetId() {
        return pachetId;
    }

    public void setEvenimentId(Integer evenimentId) { this.evenimentId = evenimentId; }

    public void setPachetId(Integer pachetId) { this.pachetId = pachetId; }


}
