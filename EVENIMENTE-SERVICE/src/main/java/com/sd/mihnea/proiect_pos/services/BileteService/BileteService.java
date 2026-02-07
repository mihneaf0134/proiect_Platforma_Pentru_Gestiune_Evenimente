package com.sd.mihnea.proiect_pos.services.BileteService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.BiletValidatDTO;
import com.sd.mihnea.proiect_pos.model.Bilete;

import java.util.List;
import java.util.UUID;

public interface BileteService {

    List<Bilete> getAllBilete();

    Bilete getBiletById(UUID cod);

    Bilete createBilet(BiletResponseDTO biletedto);

    void deleteBilet(UUID cod);

    BiletValidatDTO validateBilet(UUID cod, String token);
}
