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
import tn.esprit.tpfoyer.domain.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Gestion : Reservation")
public class ReservationController {
    private final IReservationService reservationService;

    @Operation(summary = "Lister les reservations")
    @ApiResponse(responseCode = "200", description = "Liste retournée")
    @GetMapping
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @Operation(summary = "Consulter : Reservation")
    @ApiResponse(responseCode = "200", description = "Ressource trouvée")
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @GetMapping("/{id}")
    public Reservation findById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        return reservationService.findById(id);
    }

    @Operation(summary = "Créer : Reservation")
    @ApiResponse(responseCode = "201", description = "Ressource créée")
    @ApiResponse(responseCode = "400", description = "Données invalides ou identifiant fourni", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestBody Reservation body) {
        return reservationService.create(body);
    }

    @Operation(summary = "Modifier : Reservation")
    @ApiResponse(responseCode = "200", description = "Ressource modifiée")
    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @PutMapping("/{id}")
    public Reservation update(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id,
            @RequestBody Reservation body) {
        return reservationService.update(id, body);
    }

    @Operation(summary = "Supprimer : Reservation")
    @ApiResponse(responseCode = "204", description = "Ressource supprimée, sans corps", content = @Content)
    @ApiResponse(responseCode = "404", description = "Ressource introuvable", content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ApiError.class)))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@Parameter(description = "Identifiant de la ressource") @PathVariable("id") Long id) {
        reservationService.deleteById(id);
    }
}
