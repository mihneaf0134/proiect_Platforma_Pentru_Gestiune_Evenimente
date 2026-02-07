package com.sd.mihnea.proiect_pos.DTOs;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

public class BiletValidatDTO {

    private boolean valid;
    private UUID biletId;
    private String message;
    private LocalDateTime timestamp;


    public BiletValidatDTO(boolean valid, UUID biletId, String message, LocalDateTime timestamp)
    {
        this.valid = valid;
        this.biletId = biletId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public UUID getBiletId() {
        return biletId;
    }

    public void setBiletId(UUID biletId) {
        this.biletId = biletId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
