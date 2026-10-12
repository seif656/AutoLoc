package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Vehicule;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IVehiculeRepository;
import tn.esprit.tpfoyer.service.IVehiculeService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    private void validate(Vehicule vehicule) {
        if (vehicule == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire");
        }

        if (vehicule.getImmatriculation() == null
                || vehicule.getImmatriculation().isBlank()) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire");
        }

        if (vehicule.getMarque() == null || vehicule.getMarque().isBlank()
                || vehicule.getModele() == null || vehicule.getModele().isBlank()) {
            throw new IllegalArgumentException("La marque et le modèle sont obligatoires");
        }

        if (vehicule.getCategorie() == null || vehicule.getStatut() == null) {
            throw new IllegalArgumentException("La catégorie et le statut sont obligatoires");
        }

        if (vehicule.getTarifJournalier() == null
                || vehicule.getTarifJournalier().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier doit être positif ou nul");
        }
    }

    @Override
    public Vehicule create(Vehicule vehicule) {
        validate(vehicule);
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        validate(vehicule);
        Vehicule existing = findById(id);
        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        return vehiculeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        vehiculeRepository.delete(findById(id));
    }
}