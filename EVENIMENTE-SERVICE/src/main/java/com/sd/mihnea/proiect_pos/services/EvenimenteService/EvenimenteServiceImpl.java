package com.sd.mihnea.proiect_pos.services.EvenimenteService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.EvenimentPatchDTO;
import com.sd.mihnea.proiect_pos.exceptions.ExceptionHandlerAdvice;
import com.sd.mihnea.proiect_pos.model.Bilete;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;

import com.sd.mihnea.proiect_pos.repository.BileteRepositoryJPA;
import com.sd.mihnea.proiect_pos.repository.EvenimenteRepositoryJPA;
import com.sd.mihnea.proiect_pos.repository.PacheteRepositoryJPA;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.*;


@Service
public class EvenimenteServiceImpl implements EvenimenteService {


    private final EvenimenteRepositoryJPA evenimenteRepositoryJPA;
    private final BileteRepositoryJPA bileteRepositoryJPA;
    private final PacheteRepositoryJPA pacheteRepositoryJPA;

    public EvenimenteServiceImpl(EvenimenteRepositoryJPA evenimenteRepositoryJPA, BileteRepositoryJPA bileteRepositoryJPA, PacheteRepositoryJPA pacheteRepositoryJPA) {
        this.evenimenteRepositoryJPA = evenimenteRepositoryJPA;
        this.bileteRepositoryJPA = bileteRepositoryJPA;
        this.pacheteRepositoryJPA = pacheteRepositoryJPA;
    }

    @Override
    public List<Evenimente> findAllEvents() {

        return evenimenteRepositoryJPA.findAll();
    }

    @Override
    public Evenimente findById(Integer id) {
        return evenimenteRepositoryJPA.findById(id).orElseThrow();
    }

    @Override
    public List<Evenimente> getOwnersEvents(Integer idOwner) {

        List<Evenimente> toateev = findAllEvents();

        List<Evenimente> MyList = new ArrayList<>();

        for(Evenimente ev : toateev)
        {

            if(ev.getID_OWNER() == idOwner)
            {
                MyList.add(ev);
            }
        }
        return MyList;

    }

    @Override
    public Evenimente createPOST(Evenimente evenimente) {


       return evenimenteRepositoryJPA.save(evenimente);

    }

    @Override
    public Evenimente createPUT(Integer id, Evenimente evenimente, Integer idOwner) {

        Evenimente evenimente1 = evenimenteRepositoryJPA.findById(id).orElseThrow();

        if(!idOwner.equals(evenimente1.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        evenimente.setID(id);

        return evenimenteRepositoryJPA.save(evenimente);

    }

    @Override
    public Evenimente update(Integer id, EvenimentPatchDTO evenimentPatchDTO, Integer idOwner){

        Evenimente evenimenteOld = evenimenteRepositoryJPA.findById(id).orElseThrow();

        if(!idOwner.equals(evenimenteOld.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (evenimentPatchDTO.getID_Owner() != 0) evenimenteOld.setID_OWNER(evenimentPatchDTO.getID_Owner());
        if (evenimentPatchDTO.getNume() != null) evenimenteOld.setNume(evenimentPatchDTO.getNume());
        if (evenimentPatchDTO.getLocatie() != null) evenimenteOld.setLocatie(evenimentPatchDTO.getLocatie());
        if (evenimentPatchDTO.getDescriere() != null) evenimenteOld.setDescriere(evenimentPatchDTO.getDescriere());
        if (evenimentPatchDTO.getNumarLocuri() != 0) evenimenteOld.setNumarLocuri(evenimentPatchDTO.getNumarLocuri());

        return evenimenteRepositoryJPA.save(evenimenteOld);


    }

    @Override
    public void deleteEvent(Integer id, Integer idOwner) {

        Evenimente evenimente = evenimenteRepositoryJPA.findById(id).orElseThrow();

        if(!idOwner.equals(evenimente.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        List<Bilete> bilete = bileteRepositoryJPA.findAll();

        for(Bilete b : bilete)
        {
            if(b.getEvenimente()!=null && Objects.equals(b.getEvenimente().getID(), id))
            {
                bileteRepositoryJPA.delete(b);
            }
        }

        evenimenteRepositoryJPA.delete(evenimente);

    }

    @Override
    public List<Pachete> getPacheteForEvent(Integer id) {
        Evenimente ev = evenimenteRepositoryJPA.findById(id).orElseThrow();

        return ev.getPachete();

    }

    @Override
    public Evenimente linkEventToPachet(Integer eventId, Integer packetId, Integer ownerID) {

        Evenimente ev = evenimenteRepositoryJPA.findById(eventId).orElseThrow();
        Pachete pachet = pacheteRepositoryJPA.findById(packetId).orElseThrow();

        if(!ownerID.equals(ev.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(!ownerID.equals(pachet.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (!ev.getPachete().contains(pachet)) {
            ev.getPachete().add(pachet);
            pachet.getEvenimente().add(ev);
        }

        if (pachet.getNumarLocuri() > ev.getNumarLocuri()) {
            pachet.setNumarLocuri(ev.getNumarLocuri());
        }

        pacheteRepositoryJPA.save(pachet);

        return evenimenteRepositoryJPA.save(ev);

    }

    @Override
    public void unlinkEventFromPachet(Integer eventId, Integer packetId, Integer ownerId) {

        Evenimente ev = evenimenteRepositoryJPA.findById(eventId).orElseThrow();
        Pachete pachete = pacheteRepositoryJPA.findById(packetId).orElseThrow();

        if(!ownerId.equals(ev.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(!ownerId.equals(pachete.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if(ev.getPachete().contains(pachete))
        {
            ev.getPachete().remove(pachete);
            evenimenteRepositoryJPA.save(ev);
            pachete.updateNumarLocuri();
            pacheteRepositoryJPA.save(pachete);
        }

    }

    @Override
    public List<BiletResponseDTO> getTicketsForEvent(Integer eventId) {


        Evenimente evenimente = evenimenteRepositoryJPA.findById(eventId).orElseThrow();
        List<Bilete> bilete = bileteRepositoryJPA.findAll();
        List<BiletResponseDTO> filtered = new ArrayList<>();

        for(Bilete i : bilete)
        {
            if(i.getEvenimente() != null)
                if(i.getEvenimente().equals(evenimente))
                {
                    filtered.add(new BiletResponseDTO(i));
                }
        }

        return filtered;

    }

    @Override
    public BiletResponseDTO getTicketForEvent(Integer eventId, UUID cod)
    {

        Evenimente evenimente = evenimenteRepositoryJPA.findById(eventId).orElseThrow();
        Bilete bilet = bileteRepositoryJPA.findById(cod).orElseThrow();


        if(bilet.getEvenimente()==null)
        {
            throw new NoSuchElementException("Ticket not for an event");
        }

        if(evenimente.equals(bilet.getEvenimente()))
        {
            return new BiletResponseDTO(bilet);
        }

        throw new NoSuchElementException("Ticket exists but not for this event");
    }

//    @Override
//    public List<Client> getClientsByEv(Integer idOwner) {
//
//        List<Client> clients = clientRepository.findAll();
//
//        List<Bilete> bilete = bileteRepositoryJPA.findAll();
//
//        List<Bilete> filteredBilete = new ArrayList<>();
//
//        List<Client> filteredClients = new ArrayList<>();
//
//        for(Bilete b : bilete)
//        {
//
//            if(b.getEvenimente()!=null && idOwner.equals(b.getEvenimente().getID_OWNER()))
//            {
//                filteredBilete.add(b);
//            }
//
//        }
//
//        for(Client client : clients)
//        {
//            for(Bilete f : filteredBilete)
//            {
//                if(client.getBileteList()!=null && client.getBileteList().contains(f.getCOD()) &&
//                !filteredClients.contains(client))
//                {
//                    filteredClients.add(client);
//                }
//            }
//        }
//
//        return filteredClients;
//
//    }


    @Override
    public List<Evenimente> searchByLocation(String location) {
        List<Evenimente> allEv = evenimenteRepositoryJPA.findAll();
        List<Evenimente> FilteredEv = new ArrayList<>();

        for(Evenimente evenimente : allEv) {
            if (location.toUpperCase().equals(evenimente.getLocatie().toUpperCase())) {

                FilteredEv.add(evenimente);
            }
        }
        return FilteredEv;
    }

    @Override
    public List<Evenimente> searchByName(String name) {
        List<Evenimente> allEv = evenimenteRepositoryJPA.findAll();
        List<Evenimente> FilteredEv = new ArrayList<>();

        for(Evenimente evenimente : allEv) {
            if (name.toUpperCase().equals(evenimente.getNume().toUpperCase())) {

                FilteredEv.add(evenimente);
            }
        }
        return FilteredEv;
    }
}
