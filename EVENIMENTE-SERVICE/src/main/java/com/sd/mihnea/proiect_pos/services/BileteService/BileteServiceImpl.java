package com.sd.mihnea.proiect_pos.services.BileteService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.BiletValidatDTO;
import com.sd.mihnea.proiect_pos.grpc.SecurityUtils;
import com.sd.mihnea.proiect_pos.model.Bilete;
import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import com.sd.mihnea.proiect_pos.repository.BileteRepositoryJPA;
import com.sd.mihnea.proiect_pos.repository.EvenimenteRepositoryJPA;
import com.sd.mihnea.proiect_pos.repository.PacheteRepositoryJPA;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BileteServiceImpl implements BileteService {


    private final BileteRepositoryJPA bileteRepositoryJPA;
    private final EvenimenteRepositoryJPA evenimenteRepositoryJPA;
    private final PacheteRepositoryJPA pacheteRepositoryJPA;
    private final RestTemplate restTemplate;


    public BileteServiceImpl(BileteRepositoryJPA bileteRepositoryJPA, EvenimenteRepositoryJPA evenimenteRepositoryJPA, PacheteRepositoryJPA pacheteRepositoryJPA, RestTemplate restTemplate) {
        this.bileteRepositoryJPA = bileteRepositoryJPA;
        this.evenimenteRepositoryJPA = evenimenteRepositoryJPA;
        this.pacheteRepositoryJPA = pacheteRepositoryJPA;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Bilete> getAllBilete() {

        return bileteRepositoryJPA.findAll();

    }

    @Override
    public Bilete getBiletById(UUID cod) {
        return bileteRepositoryJPA.findById(cod).orElseThrow();
    }

    @Override
    public Bilete createBilet(BiletResponseDTO biletedto) {
        Bilete BileteNou = new Bilete();

        if (biletedto.getEvenimentId() != null) {
            Evenimente eveniment = evenimenteRepositoryJPA.findById(biletedto.getEvenimentId()).orElseThrow();



                eveniment.setNumarLocuri(eveniment.getNumarLocuri() - 1);

                if(!eveniment.getPachete().isEmpty())
                {
                    for(Pachete p : eveniment.getPachete())
                    {
                        p.updateNumarLocuri();
                    }
                }


            BileteNou.setEvenimente(eveniment);
        }
        else
        if (biletedto.getPachetId() != null) {
            Pachete pachet = pacheteRepositoryJPA.findById(biletedto.getPachetId()).orElseThrow();

            //trebuie verificat Numar Locuri


            for(Evenimente i : pachet.getEvenimente())
            {
                i.setNumarLocuri(i.getNumarLocuri() - 1);
            }
            pachet.setNumarLocuri(pachet.getNumarLocuri() - 1);
            BileteNou.setPachete(pachet);
        }

        return bileteRepositoryJPA.save(BileteNou);
    }

    @Override
    public void deleteBilet(UUID cod) {


        Bilete bilet = bileteRepositoryJPA.findById(cod).orElseThrow();

        if(bilet.getEvenimente() != null)
        {
            bilet.getEvenimente().setNumarLocuri(bilet.getEvenimente().getNumarLocuri() + 1);
            if(!bilet.getEvenimente().getPachete().isEmpty())
            {
                for(Pachete p : bilet.getEvenimente().getPachete())
                {
                    p.updateNumarLocuri();
                }
            }

            bileteRepositoryJPA.delete(bilet);

        }
        else if(bilet.getPachete() != null)
        {
            for(Evenimente i : bilet.getPachete().getEvenimente()) {
                i.setNumarLocuri(i.getNumarLocuri() + 1);
            }

            bilet.getPachete().setNumarLocuri(bilet.getPachete().getNumarLocuri() + 1);

            bileteRepositoryJPA.delete(bilet);

        }
        else{
            bileteRepositoryJPA.delete(bilet);
        }


    }

    @Override
    public BiletValidatDTO validateBilet(UUID cod, String token) {

        Bilete bilete = bileteRepositoryJPA.findById(cod).orElseThrow();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        if(bilete.getEvenimente() != null)
        {
            String url = "http://localhost:8080/api/event-manager/events/" + bilete.getEvenimente().getID();

             restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Evenimente.class,
                    bilete.getEvenimente().getID()
            );
        }
        else
        if(bilete.getPachete() != null)
        {
            String url = "http://localhost:8080/api/event-manager/event-packets/" + bilete.getPachete().getID();
            restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Pachete.class,
                    bilete.getPachete().getID()
            );
        }
        else {
            return new BiletValidatDTO(false,cod,"Biletul NU este valid", LocalDateTime.now());
        }

        return new BiletValidatDTO(true,cod,"Biletul a fost validat!", LocalDateTime.now());
    }
}
