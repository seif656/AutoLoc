package tn.esprit.tpfoyer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpfoyer.domain.Vehicule;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
}