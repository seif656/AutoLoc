package tn.esprit.tpfoyer.service;

import tn.esprit.tpfoyer.web.dto.AffectationEquipementResponse;

import tn.esprit.tpfoyer.domain.Vehicule;
import java.util.List;

public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);
    Vehicule findById(Long id);
    List<Vehicule> findAll();
    Vehicule update(Long id, Vehicule vehicule);
    void deleteById(Long id);
    AffectationEquipementResponse affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);
    List<Vehicule> findByContratId(Long idContrat);
    List<Vehicule> findByMaintenanceId(Long idMaintenance);
}
