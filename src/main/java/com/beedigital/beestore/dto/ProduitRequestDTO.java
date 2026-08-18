package com.beedigital.beestore.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProduitRequestDTO {

    // Validation des données d’entrée avant traitement

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100,
            message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @Size(max = 500,
            message = "La description ne peut pas dépasser 500 caractères")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être strictement positif")
    private Double prix;

    @NotNull(message = "Le stock est obligatoire")
    @PositiveOrZero(message = "Le stock ne peut pas être négatif")
    private Integer stock;

    @Size(max = 100,
            message = "La catégorie ne peut pas dépasser 100 caractères")
    private String categorie;

    private Boolean actif = true;

}

