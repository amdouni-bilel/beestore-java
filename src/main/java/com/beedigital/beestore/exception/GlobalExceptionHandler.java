package com.beedigital.beestore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // Structure de réponse d’erreur uniforme
    record ErrorResponse(int status, String message,
                         LocalDateTime timestamp, Object details) {}

    // Gestion des erreurs de validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        log.warn("[VALIDATION] {} erreur(s) de validation", errors.size());
        return ResponseEntity.badRequest().body(
                new ErrorResponse(400, "Données invalides",
                        LocalDateTime.now(), errors));
    }

    // Gestion : produit non trouvé → 404
    @ExceptionHandler(ProduitNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ProduitNotFoundException ex) {
        log.info("[NOT_FOUND] {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(404, ex.getMessage(),
                        LocalDateTime.now(), null));
    }

    // Gestion générique → 500
    // On retourne un message générique au client ; le détail complet
    // reste dans les logs serveur (log.error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("[ERREUR] Erreur inattendue", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(500,
                        "Une erreur interne est survenue. Contactez l'administrateur.",
                        LocalDateTime.now(), null));
    }

}

