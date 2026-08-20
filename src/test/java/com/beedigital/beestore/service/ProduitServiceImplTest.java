package com.beedigital.beestore.service;

import com.beedigital.beestore.dto.ProduitRequestDTO;
import com.beedigital.beestore.dto.ProduitResponseDTO;
import com.beedigital.beestore.entity.Produit;
import com.beedigital.beestore.exception.ProduitNotFoundException;
import com.beedigital.beestore.repository.ProduitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitServiceImpl produitService;

    @Test
    @DisplayName("creerProduit() doit sauvegarder et retourner le produit créé")
    void creerProduit_doitSauvegarderEtRetournerLeProduit() {
        // GIVEN
        ProduitRequestDTO requestDTO = new ProduitRequestDTO();
        requestDTO.setNom("Laptop Pro");
        requestDTO.setPrix(1299.99);
        requestDTO.setStock(10);

        Produit produitSauvegarde = Produit.builder()
                .id(1L).nom("Laptop Pro").prix(1299.99).stock(10).actif(true)
                .build();
        when(produitRepository.save(any(Produit.class))).thenReturn(produitSauvegarde);

        // WHEN
        ProduitResponseDTO resultat = produitService.creerProduit(requestDTO);

        // THEN
        assertThat(resultat.getId()).isEqualTo(1L);
        assertThat(resultat.getNom()).isEqualTo("Laptop Pro");
        verify(produitRepository, times(1)).save(any(Produit.class));
    }

    @Test
    @DisplayName("getProduitById() doit retourner le produit s'il existe")
    void getProduitById_produitExistant_doitRetournerLeProduit() {
        Produit produit = Produit.builder().id(1L).nom("Laptop Pro").build();
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));

        ProduitResponseDTO resultat = produitService.getProduitById(1L);

        assertThat(resultat.getNom()).isEqualTo("Laptop Pro");
    }

    @Test
    @DisplayName("getProduitById() doit lever ProduitNotFoundException si absent")
    void getProduitById_produitAbsent_doitLeverException() {
        when(produitRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produitService.getProduitById(99L))
                .isInstanceOf(ProduitNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("supprimerProduit() doit lever une exception si le produit n'existe pas")
    void supprimerProduit_produitAbsent_doitLeverException() {
        when(produitRepository.existsById(42L)).thenReturn(false);

        assertThatThrownBy(() -> produitService.supprimerProduit(42L))
                .isInstanceOf(ProduitNotFoundException.class);

        verify(produitRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("getTousProduits() doit retourner la liste mappée en DTO")
    void getTousProduits_doitRetournerListeDeDTO() {
        when(produitRepository.findAll()).thenReturn(List.of(
                Produit.builder().id(1L).nom("A").build(),
                Produit.builder().id(2L).nom("B").build()
        ));

        List<ProduitResponseDTO> resultat = produitService.getTousProduits();

        assertThat(resultat).hasSize(2);
    }
}
