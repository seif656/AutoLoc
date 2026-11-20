package tn.esprit.tpfoyer.web.controller;

import jakarta.validation.Valid;
import tn.esprit.tpfoyer.web.dto.AffectationMaintenanceRequest;
import tn.esprit.tpfoyer.web.dto.AffectationMaintenanceResponse;

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
import tn.esprit.tpfoyer.domain.Maintenance;
import tn.esprit.tpfoyer.service.IMaintenanceService;

import java.util.List;

@RestController
@RequestMapping("/maintenances")
@RequiredArgsConstructor
@Tag(name = "Maintenances", description = "Gestion : Maintenance")
public class MaintenanceController {
    private final IMaintenanceService maintenanceService;

    @Operation(summary = "Lister les maintenances")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @GetMapping
    public List<Maintenance> findAll() {
        return maintenanceService.findAll();
    }

    @Operation(summary = "Consulter : Maintenance")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @GetMapping("/{id}")
    public Maintenance findById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        return maintenanceService.findById(id);
    }

    @Operation(summary = "Créer : Maintenance")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Maintenance create(@RequestBody Maintenance body) {
        return maintenanceService.create(body);
    }

    @Operation(summary = "Modifier : Maintenance")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PutMapping("/{id}")
    public Maintenance update(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id,
            @RequestBody Maintenance body) {
        return maintenanceService.update(id, body);
    }

    @Operation(summary = "Supprimer : Maintenance")
    @ApiResponse(responseCode = "204", description = "Ressource supprimée, sans corps", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        maintenanceService.deleteById(id);
    }

    @Operation(summary = "Affecter une maintenance à un véhicule",
            description = "Affecte une maintenance existante à un véhicule existant et retourne un DTO.")
    @ApiResponse(responseCode = "200", description = "Maintenance affectée")
    @ApiResponse(responseCode = "400", description = "Identifiants obligatoires et strictement positifs",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Maintenance ou véhicule introuvable",
            content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PutMapping("/affectation-vehicule")
    public AffectationMaintenanceResponse affecterMaintenanceAVehicule(
            @Valid @RequestBody AffectationMaintenanceRequest request) {
        return maintenanceService.affecterMaintenanceAVehicule(request.idMaintenance(), request.idVehicule());
    }
}
