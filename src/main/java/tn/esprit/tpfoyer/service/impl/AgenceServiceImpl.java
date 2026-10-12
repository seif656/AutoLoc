package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Agence;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IAgenceRepository;
import tn.esprit.tpfoyer.service.IAgenceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgenceServiceImpl implements IAgenceService {

    private final IAgenceRepository agenceRepository;

    private void validate(Agence agence) {
        if (agence == null || agence.getNom() == null || agence.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }

    @Override
    public Agence create(Agence agence) {
        validate(agence);
        if (agence.getIdAgence() != null) {
            throw new IllegalArgumentException("Une nouvelle agence ne doit pas avoir d'identifiant");
        }
        return agenceRepository.save(agence);
    }

    @Override
    public Agence findById(Long id) {
        return agenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", id));
    }

    @Override
    public List<Agence> findAll() {
        return agenceRepository.findAll();
    }

    @Override
    public Agence update(Long id, Agence agence) {
        validate(agence);
        Agence existing = findById(id);
        existing.setNom(agence.getNom());
        existing.setVille(agence.getVille());
        existing.setAdresse(agence.getAdresse());
        existing.setTelephone(agence.getTelephone());
        return agenceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        agenceRepository.delete(findById(id));
    }
}