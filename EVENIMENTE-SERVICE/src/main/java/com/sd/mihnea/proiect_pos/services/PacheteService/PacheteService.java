package com.sd.mihnea.proiect_pos.services.PacheteService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.PachetPatchDTO;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;

import java.util.List;
import java.util.UUID;

public interface PacheteService {

    List<Pachete> findAll();
    Pachete findById(Integer id);

    List<Pachete> findMyPachete(Integer idOwner);

    Pachete createPOST(Pachete pachete);
    Pachete createPUT(Integer id,Pachete pachete, Integer idOwner);
    Pachete update(Integer id, PachetPatchDTO pachetPatchDTO, Integer idOwner);
    void delete(Integer id, Integer idOwner);

    List<Evenimente> getEventsForPacket(Integer packetId);

    List<BiletResponseDTO> getBileteForPacket(Integer packetId);

    BiletResponseDTO getBiletForPacket(Integer packetId, UUID cod);

    List<Pachete> findPaged(int page, int itemsPerPage);

    List<Pachete> filterByAvailableTickets(int minTickets);
    List<Pachete> filterByDescriptionContains(String text);


}
