package tn.esprit.tpfoyer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Employe;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IEmployeRepository;
import tn.esprit.tpfoyer.service.IEmployeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeServiceImpl implements IEmployeService {

    private final IEmployeRepository employeRepository;

    private void validate(Employe employe) {
        if (employe == null || employe.getNom() == null || employe.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (employe.getRole() == null) {
            throw new IllegalArgumentException("Le rôle de l'employé est obligatoire");
        }
    }

    @Override
    public Employe create(Employe employe) {
        validate(employe);
        if (employe.getIdEmploye() != null) {
            throw new IllegalArgumentException("Un nouvel employé ne doit pas avoir d'identifiant");
        }
        return employeRepository.save(employe);
    }

    @Override
    public Employe findById(Long id) {
        return employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe", id));
    }

    @Override
    public List<Employe> findAll() {
        return employeRepository.findAll();
    }

    @Override
    public Employe update(Long id, Employe employe) {
        validate(employe);
        Employe existing = findById(id);
        existing.setNom(employe.getNom());
        existing.setPrenom(employe.getPrenom());
        existing.setRole(employe.getRole());
        return employeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        employeRepository.delete(findById(id));
    }
}