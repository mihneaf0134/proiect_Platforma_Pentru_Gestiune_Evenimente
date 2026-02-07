package com.sd.mihnea.proiect_pos.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
public class Bilete {


    @Id
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID COD;


    @ManyToOne(optional = true)
    @JoinColumn(name = "PachetID", nullable = true)
    private Pachete pachete;

    @AssertTrue(message = "A ticket must be linked to either a Pachet or an Eveniment")
    private boolean isValid() {
        return (pachete != null || evenimente != null);
    }



    @ManyToOne(optional = true)
    @JoinColumn(name = "EvenimentID",nullable = true)
    private Evenimente evenimente;
    

    public UUID getCOD() {
        return COD;
    }

    public void setCOD(UUID COD) {
        this.COD = COD;
    }


    public Pachete getPachete() {
        return pachete;
    }

    public void setPachete(Pachete pachete) {
        this.pachete = pachete;
    }

    public Evenimente getEvenimente() {
        return evenimente;
    }

    public void setEvenimente(Evenimente evenimente) {
        this.evenimente = evenimente;
    }
}
