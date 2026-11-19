package tn.esprit.tpfoyer.web.controller;

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
import tn.esprit.tpfoyer.domain.Agence;
import tn.esprit.tpfoyer.service.IAgenceService;

import java.util.List;

@RestController
@RequestMapping("/agences")
@RequiredArgsConstructor
@Tag(name = "Agences", description = "Gestion : Agence")
public class AgenceController {
    private final IAgenceService agenceService;

    @Operation(summary = "Lister les agences")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @GetMapping
    public List<Agence> findAll() {
        return agenceService.findAll();
    }

    @Operation(summary = "Consulter : Agence")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @GetMapping("/{id}")
    public Agence findById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        return agenceService.findById(id);
    }

    @Operation(summary = "Créer : Agence")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Agence create(@RequestBody Agence body) {
        return agenceService.create(body);
    }

    @Operation(summary = "Modifier : Agence")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PutMapping("/{id}")
    public Agence update(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id,
            @RequestBody Agence body) {
        return agenceService.update(id, body);
    }

    @Operation(summary = "Supprimer : Agence")
    @ApiResponse(responseCode = "204", description = "Ressource supprimée, sans corps", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        agenceService.deleteById(id);
    }
}
