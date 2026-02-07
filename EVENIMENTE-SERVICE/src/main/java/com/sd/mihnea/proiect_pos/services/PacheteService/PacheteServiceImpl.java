package com.sd.mihnea.proiect_pos.services.PacheteService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.PachetPatchDTO;
import com.sd.mihnea.proiect_pos.model.Bilete;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import com.sd.mihnea.proiect_pos.repository.BileteRepositoryJPA;
import com.sd.mihnea.proiect_pos.repository.PacheteRepositoryJPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PacheteServiceImpl implements PacheteService {


    private final PacheteRepositoryJPA pacheteRepositoryJPA;
    private final BileteRepositoryJPA bileteRepositoryJPA;

    public PacheteServiceImpl(PacheteRepositoryJPA pacheteRepositoryJPA, BileteRepositoryJPA bileteRepositoryJPA) {
        this.pacheteRepositoryJPA = pacheteRepositoryJPA;
        this.bileteRepositoryJPA = bileteRepositoryJPA;
    }


    @Override
    public List<Pachete> findAll() {


        return pacheteRepositoryJPA.findAll();

    }

    @Override
    public Pachete findById(Integer id) {

        return pacheteRepositoryJPA.findById(id).orElseThrow();

    }

    @Override
    public List<Pachete> findMyPachete(Integer idOwner) {

        List<Pachete> toatepachete = findAll();

        List<Pachete> MyList = new ArrayList<>();

        for(Pachete pachete : toatepachete)
        {
            if(idOwner.equals(pachete.getID_OWNER()))
            {
                MyList.add(pachete);
            }
        }
        return MyList;
    }

    @Override
    public Pachete createPOST(Pachete pachete) {

        return pacheteRepositoryJPA.save(pachete);

    }

    @Override
    public Pachete createPUT(Integer id, Pachete pachete, Integer ownerId)
    {
        Pachete pachete1 = pacheteRepositoryJPA.findById(id).orElseThrow();

        if(!ownerId.equals(pachete1.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        pachete.setID(id);

        return pacheteRepositoryJPA.save(pachete);

    }



    @Override
    public Pachete update(Integer id, PachetPatchDTO pachetPatchDTO, Integer ownerId) {

        Pachete pachete = pacheteRepositoryJPA.findById(id).orElseThrow();

        if(!ownerId.equals(pachete.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (pachetPatchDTO.getID_OWNER() != 0) pachete.setID_OWNER(pachetPatchDTO.getID_OWNER());
        if (pachetPatchDTO.getNume() != null) pachete.setNume(pachetPatchDTO.getNume());
        if (pachetPatchDTO.getLocatie() != null) pachete.setLocatie(pachetPatchDTO.getLocatie());
        if (pachetPatchDTO.getDescriere() != null) pachete.setDescriere(pachetPatchDTO.getDescriere());

        return pacheteRepositoryJPA.save(pachete);

    }

    @Override
    public void delete(Integer id, Integer ownerId) {


        Pachete pachete = pacheteRepositoryJPA.findById(id).orElseThrow();

        if(!ownerId.equals(pachete.getID_OWNER()))
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        pacheteRepositoryJPA.delete(pachete);

    }

    @Override
    public List<Evenimente> getEventsForPacket(Integer packetId) {

        return pacheteRepositoryJPA.findById(packetId).orElseThrow().getEvenimente();

    }

    @Override
    public List<BiletResponseDTO> getBileteForPacket(Integer packetId) {

        Pachete pachete = pacheteRepositoryJPA.findById(packetId).orElseThrow();

        List<Bilete> bilete = bileteRepositoryJPA.findAll();

        List<BiletResponseDTO> filtered = new ArrayList<>();

        for(Bilete b : bilete)
        {

            if(b.getPachete() != null)
                if(pachete.equals(b.getPachete()))
                {
                    filtered.add(new BiletResponseDTO(b));
                }
        }

        return filtered;

    }

    @Override
    public BiletResponseDTO getBiletForPacket(Integer packetId, UUID cod) {

        Pachete pachete = pacheteRepositoryJPA.findById(packetId).orElseThrow();
        Bilete bilet = bileteRepositoryJPA.findById(cod).orElseThrow();

        if(bilet.getPachete()==null)
        {
            throw new NoSuchElementException();
        }
        if(pachete.equals(bilet.getPachete()))
        {
            return new BiletResponseDTO(bilet);
        }
        else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Pachete> findPaged(int page, int itemsPerPage) {

        int Pagelimit = (itemsPerPage != 0) ? itemsPerPage : 10;

        PageRequest pageRequest = PageRequest.of(page, Pagelimit);

        Page<Pachete> pageResult = pacheteRepositoryJPA.findAll(pageRequest);

        return pageResult.getContent();

    }

    @Override
    public List<Pachete> filterByAvailableTickets(int available_tickets) {
        List<Pachete> pachete = pacheteRepositoryJPA.findAll();
        List<Pachete> filtered_pachete = new ArrayList<>();

        for(Pachete i : pachete)
        {

            if(i.getNumarLocuri() > available_tickets)
            {
                filtered_pachete.add(i);
            }

        }

        return filtered_pachete;
    }

    @Override
    public List<Pachete> filterByDescriptionContains(String type) {

        List<Pachete> pachete = pacheteRepositoryJPA.findAll();


        List<Pachete> filtered = new ArrayList<>();

        for(Pachete i : pachete)
        {
            if(i.getDescriere().toUpperCase().contains(type.toUpperCase()))
            {
                filtered.add(i);
            }
        }

        return filtered;
    }


}
