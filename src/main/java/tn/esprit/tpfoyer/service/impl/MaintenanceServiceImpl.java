package tn.esprit.tpfoyer.service.impl;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer.domain.Vehicule;
import tn.esprit.tpfoyer.repository.IVehiculeRepository;
import tn.esprit.tpfoyer.web.dto.AffectationMaintenanceResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.domain.Maintenance;
import tn.esprit.tpfoyer.exception.ResourceNotFoundException;
import tn.esprit.tpfoyer.repository.IMaintenanceRepository;
import tn.esprit.tpfoyer.service.IMaintenanceService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final IMaintenanceRepository maintenanceRepository;
    private final IVehiculeRepository vehiculeRepository;

    private void validate(Maintenance maintenance) {
        if (maintenance == null) {
            throw new IllegalArgumentException("La maintenance est obligatoire");
        }
        if (maintenance.getDateDebut() == null || maintenance.getDateFin() == null
                || maintenance.getDateFin().isBefore(maintenance.getDateDebut())) {
            throw new IllegalArgumentException("Les dates de maintenance sont invalides");
        }
    }

    @Override
    public Maintenance create(Maintenance maintenance) {
        validate(maintenance);
        if (maintenance.getIdMaintenance() != null) {
            throw new IllegalArgumentException("Une nouvelle maintenance ne doit pas avoir d'identifiant");
        }
        return maintenanceRepository.save(maintenance);
    }

    @Override
    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance", id));
    }

    @Override
    public List<Maintenance> findAll() {
        return maintenanceRepository.findAll();
    }

    @Override
    public Maintenance update(Long id, Maintenance maintenance) {
        validate(maintenance);
        Maintenance existing = findById(id);
        existing.setDateDebut(maintenance.getDateDebut());
        existing.setDateFin(maintenance.getDateFin());
        existing.setDescription(maintenance.getDescription());
        return maintenanceRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        maintenanceRepository.delete(findById(id));
    }

    @Override
    @Transactional
    public AffectationMaintenanceResponse affecterMaintenanceAVehicule(Long idMaintenance, Long idVehicule) {
        Maintenance maintenance = findById(idMaintenance);
        Vehicule vehicule = vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", idVehicule));
        maintenance.setVehicule(vehicule);
        maintenanceRepository.save(maintenance);
        return new AffectationMaintenanceResponse(
                maintenance.getIdMaintenance(), vehicule.getIdVehicule(), vehicule.getImmatriculation());
    }
}
