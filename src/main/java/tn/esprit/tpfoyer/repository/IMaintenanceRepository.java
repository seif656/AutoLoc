package tn.esprit.tpfoyer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.tpfoyer.domain.Maintenance;

public interface IMaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByVehicule_IdVehiculeOrderByIdMaintenanceAsc(Long idVehicule);
}
