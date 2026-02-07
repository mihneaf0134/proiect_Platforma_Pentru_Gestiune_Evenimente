package com.sd.mihnea.proiect_pos.services.EvenimenteService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.EvenimentPatchDTO;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface EvenimenteService {


    List<Evenimente> findAllEvents();
    Evenimente findById(Integer id);
    List<Evenimente> getOwnersEvents(Integer idOwner);
    Evenimente createPOST(Evenimente evenimente);
    Evenimente createPUT(Integer id, Evenimente evenimente, Integer idOwner);

    Evenimente update(Integer id, EvenimentPatchDTO evenimentPatchDTO, Integer idOwner);
    void deleteEvent(Integer id, Integer idOwner);

    List<Pachete> getPacheteForEvent(Integer id);
    Evenimente linkEventToPachet(Integer eventId, Integer packetId, Integer ownerId);
    void unlinkEventFromPachet(Integer eventId, Integer packetId, Integer ownerId);

    List<BiletResponseDTO> getTicketsForEvent(Integer eventId);

    BiletResponseDTO getTicketForEvent(Integer eventId, UUID cod);

//    List<Client> getClientsByEv(Integer idOwner);

    List<Evenimente> searchByLocation(String location);
    List<Evenimente> searchByName(String name);
}
