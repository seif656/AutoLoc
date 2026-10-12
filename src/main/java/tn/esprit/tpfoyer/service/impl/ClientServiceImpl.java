package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Client;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IClientRepository;
import tn.esprit.tpfoyer.service.IClientService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    private void validate(Client client) {
        if (client == null || client.getNom() == null || client.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        if (client.getEmail() == null || client.getEmail().isBlank()
                || !client.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("L'email du client est invalide");
        }
    }

    @Override
    public Client create(Client client) {
        validate(client);
        if (client.getIdClient() != null) {
            throw new IllegalArgumentException("Un nouveau client ne doit pas avoir d'identifiant");
        }
        return clientRepository.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        validate(client);
        Client existing = findById(id);
        existing.setNom(client.getNom());
        existing.setPrenom(client.getPrenom());
        existing.setEmail(client.getEmail());
        existing.setTelephone(client.getTelephone());
        existing.setNumPermis(client.getNumPermis());
        existing.setDateInscription(client.getDateInscription());
        return clientRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.delete(findById(id));
    }
}