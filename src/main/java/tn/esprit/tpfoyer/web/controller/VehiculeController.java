package tn.esprit.tpfoyer.web.controller;

import jakarta.validation.Valid;
import tn.esprit.tpfoyer.web.dto.AffectationEquipementRequest;
import tn.esprit.tpfoyer.web.dto.AffectationEquipementResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.tpfoyer.domain.Vehicule;
import tn.esprit.tpfoyer.service.IVehiculeService;

import java.util.List;

@RestController
@RequestMapping("/vehicules")
@RequiredArgsConstructor
@Tag(name = "Vehicules", description = "Gestion : Vehicule")
public class VehiculeController {
    private final IVehiculeService vehiculeService;

    @Operation(summary = "Lister les vehicules")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @GetMapping
    public List<Vehicule> findAll() {
        return vehiculeService.findAll();
    }

    @Operation(summary = "Consulter : Vehicule")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @GetMapping("/{id}")
    public Vehicule findById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        return vehiculeService.findById(id);
    }

    @Operation(summary = "Créer : Vehicule")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicule create(@RequestBody Vehicule body) {
        return vehiculeService.create(body);
    }

    @Operation(summary = "Modifier : Vehicule")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PutMapping("/{id}")
    public Vehicule update(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id,
            @RequestBody Vehicule body) {
        return vehiculeService.update(id, body);
    }

    @Operation(summary = "Supprimer : Vehicule")
    @ApiResponse(responseCode = "204", description = "Ressource supprimée, sans corps", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        vehiculeService.deleteById(id);
    }

    @Operation(summary = "Affecter un véhicule à un équipement",
            description = "Ajoute le lien véhicule-équipement sans doublon et retourne un DTO.")
    @ApiResponse(responseCode = "200", description = "Véhicule affecté à l'équipement")
    @ApiResponse(responseCode = "400", description = "Identifiants obligatoires et strictement positifs",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Véhicule ou équipement introuvable",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PutMapping("/affectation-equipement")
    public AffectationEquipementResponse affecterVehiculeAEquipement(
            @Valid @RequestBody AffectationEquipementRequest request) {
        return vehiculeService.affecterVehiculeAEquipement(request.idVehicule(), request.idEquipement());
    }

    @Operation(summary = "Lister les véhicules par identifiant de contrat", description = "Retourne une liste vide si aucune association n'existe.")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @ApiResponse(responseCode = "404", description = "Ressource recherchée introuvable",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @GetMapping("/contrat/{idContrat}")
    public List<Vehicule> findByContratId(
            @Parameter(description = "Identifiant recherché") @PathVariable("idContrat") Long idContrat) {
        return vehiculeService.findByContratId(idContrat);
    }
}
