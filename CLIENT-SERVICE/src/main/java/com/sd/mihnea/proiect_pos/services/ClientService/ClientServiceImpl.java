package com.sd.mihnea.proiect_pos.services.ClientService;

import com.sd.mihnea.proiect_pos.DTOs.BiletResponseDTO;
import com.sd.mihnea.proiect_pos.DTOs.BiletValidatDTO;
import com.sd.mihnea.proiect_pos.DTOs.ClientInfoDTO;
import com.sd.mihnea.proiect_pos.mongo.model.Client;
import com.sd.mihnea.proiect_pos.mongo.repository.ClientRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;
    private final RestTemplate restTemplate;


    public ClientServiceImpl(ClientRepository clientRepository, RestTemplate restTemplate) {
        this.clientRepository = clientRepository;
        this.restTemplate = restTemplate;
    }


    @Override
    public List<Client> getClients() {


        return clientRepository.findAll();


    }

    @Override
    public Client getClient(String email) {

        Client client = clientRepository.findClientByEmail(email);

        if(client == null)
        {
            throw new NoSuchElementException();
        }

        return client;

    }

    @Override
    public Client getMe(String email) {
        return getClient(email);
    }

    @Override
    public Client createClient(String email)
    {

        Client client = new Client();
        client.setEmail(email);
        return clientRepository.save(client);
    }


    @Override
    public void deleteClient(String email)
    {

        if(clientRepository.findClientByEmail(email) == null)
        {
            throw new NoSuchElementException();
        }

        clientRepository.deleteClientByEmail(email);
    }


    @Override
    public void purchaseBilet(String email, Integer evenimentId, Integer pachetId, String token)
    {

        String url = "http://EVENIMENTE-SERVICE:8080/api/event-manager/tickets";

        BiletResponseDTO request = new BiletResponseDTO();


        if(evenimentId != null && pachetId != null)
        {
            throw new IllegalArgumentException("Nu poti cumpara si eveniment si pachet in acelasi timp");
        }

        if(evenimentId == null && pachetId == null)
        {
            throw new IllegalArgumentException("Nu pot fi ambele nule");
        }

        if (evenimentId != null) {
            request.setEvenimentId(evenimentId);
        } else {
            request.setPachetId(pachetId);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<BiletResponseDTO> entity =
                new HttpEntity<>(request, headers);

        BiletResponseDTO bilete = restTemplate.postForObject(
                url,
                entity,
                BiletResponseDTO.class
        );

        Client client = clientRepository.findClientByEmail(email);


        List<UUID> biletenou = new ArrayList<>();

        if(client.getBileteList() != null)
            client.getBileteList().add(bilete != null ? bilete.getCod() : null);
        else{
            assert bilete != null;
            biletenou.add(bilete.getCod());
            client.setBileteList(biletenou);
        }

        clientRepository.save(client);

    }


    @Override
    public Client updateClient(String email, ClientInfoDTO clientInfoDTO) {

        Client client = clientRepository.findClientByEmail(email);


        if(clientInfoDTO.getNume()!=null) client.setNume(clientInfoDTO.getNume());
        if(clientInfoDTO.getPrenume()!=null) client.setPrenume(clientInfoDTO.getPrenume());
        if(clientInfoDTO.getSocial_media()!=null)
        {
            if(client.getSocial_media()!=null)
            {
                client.getSocial_media().clear();
            }
            for(String i : clientInfoDTO.getSocial_media()) {
                client.getSocial_media().add(i);
            }
        }

        return clientRepository.save(client);

    }

    @Override
    public BiletValidatDTO validareBilet(UUID cod, String token) {

        String url = "http://EVENIMENTE-SERVICE:8080/api/event-manager/tickets/validate-ticket/{cod}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        BiletValidatDTO response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                BiletValidatDTO.class,
                cod
        ).getBody();

        return response;

    }

    @Override
    public List<BiletResponseDTO> getMyTickets(String email, String token) {

        Client client = clientRepository.findClientByEmail(email);

        List<BiletResponseDTO> bilete = new ArrayList<>();

        if(client.getBileteList()==null)
        {
            return bilete;
        }

        String url = "http://EVENIMENTE-SERVICE:8080/api/event-manager/tickets";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<CollectionModel<EntityModel<BiletResponseDTO>>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<>() {}
                );

        assert response.getBody() != null;
        List<BiletResponseDTO> bileteList = response.getBody()
                .getContent()
                .stream()
                .map(EntityModel::getContent)
                .toList();


        for(UUID cod : client.getBileteList())
        {
            for(BiletResponseDTO bilet : bileteList)
                if(bilet.getCod().equals(cod))
                    bilete.add(bilet);
        }


        return bilete;

    }
}
