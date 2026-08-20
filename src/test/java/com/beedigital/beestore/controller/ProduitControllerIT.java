package com.beedigital.beestore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ProduitControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/produits avec des données valides doit retourner 201")
    void creerProduit_donneesValides_doitRetourner201() throws Exception {
        String json = """
                {
                  "nom": "Clavier mécanique",
                  "prix": 89.90,
                  "stock": 25,
                  "categorie": "Informatique"
                }""";

        mockMvc.perform(post("/api/v1/produits")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nom").value("Clavier mécanique"));
    }

    @Test
    @DisplayName("POST /api/v1/produits avec un nom vide doit retourner 400")
    void creerProduit_nomVide_doitRetourner400() throws Exception {
        String json = """
                { "nom": "", "prix": 10.0, "stock": 1 }""";

        mockMvc.perform(post("/api/v1/produits")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.nom").exists());
    }

    @Test
    @DisplayName("GET /api/v1/produits/{id} sur un ID inconnu doit retourner 404")
    void getProduitById_idInconnu_doitRetourner404() throws Exception {
        mockMvc.perform(get("/api/v1/produits/{id}", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Cycle complet : créer, lire, modifier, supprimer un produit")
    void cycleCompletCRUD() throws Exception {
        // CREATE
        String creation = """
                { "nom": "Souris", "prix": 19.90, "stock": 50 }""";
        String reponse = mockMvc.perform(post("/api/v1/produits")
                        .contentType("application/json").content(creation))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(reponse).get("id").asLong();

        // READ
        mockMvc.perform(get("/api/v1/produits/{id}", id))
                .andExpect(status().isOk());

        // UPDATE
        String modification = """
                { "nom": "Souris sans fil", "prix": 24.90, "stock": 40 }""";
        mockMvc.perform(put("/api/v1/produits/{id}", id)
                        .contentType("application/json").content(modification))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Souris sans fil"));

        // DELETE
        mockMvc.perform(delete("/api/v1/produits/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/produits/{id}", id))
                .andExpect(status().isNotFound());
    }
}
