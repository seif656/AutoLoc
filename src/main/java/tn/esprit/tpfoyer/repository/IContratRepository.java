package tn.esprit.tpfoyer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpfoyer.domain.Contrat;

public interface IContratRepository extends JpaRepository<Contrat, Long> {
    List<Contrat> findByReservation_Vehicule_IdVehiculeOrderByIdContratAsc(Long idVehicule);
}
