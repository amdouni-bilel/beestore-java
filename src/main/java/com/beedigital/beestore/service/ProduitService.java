package com.beedigital.beestore.service;

import com.beedigital.beestore.dto.ProduitRequestDTO;
import com.beedigital.beestore.dto.ProduitResponseDTO;
import java.util.List;

public interface ProduitService {

    ProduitResponseDTO creerProduit(ProduitRequestDTO requestDTO);

    List<ProduitResponseDTO> getTousProduits();

    ProduitResponseDTO getProduitById(Long id);

    ProduitResponseDTO modifierProduit(Long id, ProduitRequestDTO requestDTO);

    void supprimerProduit(Long id);

    List<ProduitResponseDTO> rechercherParCategorie(String categorie);

    List<ProduitResponseDTO> rechercherParNom(String keyword);

}
