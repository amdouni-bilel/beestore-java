package com.beedigital.beestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "produits")
@Data                // Lombok : génère getters, setters, toString, equals, hashCode
@NoArgsConstructor   // Lombok : constructeur vide (requis par JPA)
@AllArgsConstructor  // Lombok : constructeur avec tous les champs
@Builder             // Lombok : pattern Builder
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "prix", nullable = false)
    private Double prix;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "categorie", length = 100)
    private String categorie;

    @Column(name = "actif")
    private Boolean actif = true;

    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    // Appelée automatiquement par JPA avant le premier INSERT
    @PrePersist
    protected void onCreate() {
        this.dateCreation = LocalDateTime.now();
        if (this.actif == null) this.actif = true;
    }

}
