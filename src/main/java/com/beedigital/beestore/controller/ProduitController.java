package com.beedigital.beestore.controller;

import com.beedigital.beestore.dto.ProduitRequestDTO;
import com.beedigital.beestore.dto.ProduitResponseDTO;
import com.beedigital.beestore.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/produits")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Produits", description = "API de gestion des produits BeeStore")
public class ProduitController {

    private final ProduitService produitService;

    // POST /api/v1/produits
    @PostMapping
    @Operation(summary = "Créer un nouveau produit")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produit créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<ProduitResponseDTO> creerProduit(
            @Valid @RequestBody ProduitRequestDTO requestDTO) {
        // @Valid déclenche la validation AVANT d’appeler le service
        log.info("[CONTROLLER] POST /api/v1/produits");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produitService.creerProduit(requestDTO));
    }

    // GET /api/v1/produits
    @GetMapping
    @Operation(summary = "Récupérer tous les produits")
    public ResponseEntity<List<ProduitResponseDTO>> getTousProduits() {
        log.info("[CONTROLLER] GET /api/v1/produits");
        return ResponseEntity.ok(produitService.getTousProduits());
    }

    // GET /api/v1/produits/{id}
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit trouvé"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable")
    })
    public ResponseEntity<ProduitResponseDTO> getProduitById(
            @Parameter(description = "ID du produit")
            @PathVariable Long id) {
        log.info("[CONTROLLER] GET /api/v1/produits/{}", id);
        return ResponseEntity.ok(produitService.getProduitById(id));
    }

    // PUT /api/v1/produits/{id}
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un produit existant")
    public ResponseEntity<ProduitResponseDTO> modifierProduit(
            @PathVariable Long id,
            @Valid @RequestBody ProduitRequestDTO requestDTO) {
        log.info("[CONTROLLER] PUT /api/v1/produits/{}", id);
        return ResponseEntity.ok(produitService.modifierProduit(id, requestDTO));
    }

    // DELETE /api/v1/produits/{id}
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<Void> supprimerProduit(@PathVariable Long id) {
        log.info("[CONTROLLER] DELETE /api/v1/produits/{}", id);
        produitService.supprimerProduit(id);
        return ResponseEntity.noContent().build();
    }

    // GET /api/v1/produits/search?categorie=...&nom=...
    @GetMapping("/search")
    @Operation(summary = "Rechercher des produits par catégorie ou nom")
    public ResponseEntity<List<ProduitResponseDTO>> rechercher(
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) String nom) {
        if (categorie != null) {
            return ResponseEntity.ok(
                    produitService.rechercherParCategorie(categorie));
        }
        if (nom != null) {
            return ResponseEntity.ok(
                    produitService.rechercherParNom(nom));
        }
        return ResponseEntity.ok(produitService.getTousProduits());
    }

}
