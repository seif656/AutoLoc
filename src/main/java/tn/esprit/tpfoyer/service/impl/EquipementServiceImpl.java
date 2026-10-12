package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Equipement;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IEquipementRepository;
import tn.esprit.tpfoyer.service.IEquipementService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementServiceImpl implements IEquipementService {

    private final IEquipementRepository equipementRepository;

    private void validate(Equipement equipement) {
        if (equipement == null || equipement.getLibelle() == null
                || equipement.getLibelle().isBlank()) {
            throw new IllegalArgumentException("Le libellé de l'équipement est obligatoire");
        }
    }

    @Override
    public Equipement create(Equipement equipement) {
        validate(equipement);
        if (equipement.getIdEquipement() != null) {
            throw new IllegalArgumentException("Un nouvel équipement ne doit pas avoir d'identifiant");
        }
        return equipementRepository.save(equipement);
    }

    @Override
    public Equipement findById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement", id));
    }

    @Override
    public List<Equipement> findAll() {
        return equipementRepository.findAll();
    }

    @Override
    public Equipement update(Long id, Equipement equipement) {
        validate(equipement);
        Equipement existing = findById(id);
        existing.setLibelle(equipement.getLibelle());
        return equipementRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        equipementRepository.delete(findById(id));
    }
}