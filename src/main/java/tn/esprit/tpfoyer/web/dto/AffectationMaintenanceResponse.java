package tn.esprit.tpfoyer.web.dto;

public record AffectationMaintenanceResponse(
        Long idMaintenance,
        Long idVehicule,
        String immatriculation
) {}
