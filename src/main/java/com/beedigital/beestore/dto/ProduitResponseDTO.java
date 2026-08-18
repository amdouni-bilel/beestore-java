package com.beedigital.beestore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduitResponseDTO {

    // Contient uniquement les données qu’on veut exposer au client
    // On n’expose jamais l’entité JPA directement

    private Long id;
    private String nom;
    private String description;
    private Double prix;
    private Integer stock;
    private String categorie;
    private Boolean actif;
    private LocalDateTime dateCreation;

}
