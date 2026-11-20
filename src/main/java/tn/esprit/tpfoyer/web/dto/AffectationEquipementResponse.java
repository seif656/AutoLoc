package tn.esprit.tpfoyer.web.dto;

public record AffectationEquipementResponse(
        Long idVehicule,
        Long idEquipement,
        String immatriculation,
        String libelle
) {}
