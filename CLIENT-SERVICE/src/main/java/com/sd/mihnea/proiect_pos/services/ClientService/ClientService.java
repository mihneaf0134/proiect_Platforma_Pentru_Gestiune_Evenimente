package com.sd.mihnea.proiect_pos.services.ClientService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.BiletValidatDTO;
import com.sd.mihnea.proiect_pos.DTOs.ClientInfoDTO;
import com.sd.mihnea.proiect_pos.mongo.model.Client;

import java.util.List;
import java.util.UUID;

public interface ClientService {


     List<Client> getClients();

     Client getClient(String email);

     Client getMe(String email);

     Client createClient(String email);

     Client updateClient(String email, ClientInfoDTO clientInfoDTO);

     void deleteClient(String email);

     void purchaseBilet(String email, Integer evenimentId, Integer pachetId, String token);

     BiletValidatDTO validareBilet(UUID cod, String token);

     List<BiletResponseDTO> getMyTickets(String email, String token);

}
