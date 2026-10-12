package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Contrat;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IContratRepository;
import tn.esprit.tpfoyer.service.IContratService;
import tn.esprit.tpfoyer.service.MissingService;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final MissingService missingService;

    @Override
    public Contrat create(Contrat contrat) {
        if (contrat == null) {
            throw new IllegalArgumentException("Le contrat ne peut pas être null");
        }

        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant");
        }

        validate(contrat);
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        if (contrat == null) {
            throw new IllegalArgumentException("Le contrat ne peut pas être null");
        }

        validate(contrat);

        Contrat existing = findById(id);
        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.isValide());

        return contratRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        Contrat existing = findById(id);
        contratRepository.delete(existing);
    }

    private void validate(Contrat contrat) {
        if (contrat.getMontantTotal() == null ||
                contrat.getMontantTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Le montant total doit être positif ou nul"
            );
        }
    }
}