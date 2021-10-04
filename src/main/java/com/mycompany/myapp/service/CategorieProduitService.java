package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.CategorieProduit;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CategorieProduit}.
 */
public interface CategorieProduitService {
    /**
     * Save a categorieProduit.
     *
     * @param categorieProduit the entity to save.
     * @return the persisted entity.
     */
    CategorieProduit save(CategorieProduit categorieProduit);

    /**
     * Partially updates a categorieProduit.
     *
     * @param categorieProduit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategorieProduit> partialUpdate(CategorieProduit categorieProduit);

    /**
     * Get all the categorieProduits.
     *
     * @return the list of entities.
     */
    List<CategorieProduit> findAll();

    /**
     * Get the "id" categorieProduit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategorieProduit> findOne(Long id);

    /**
     * Delete the "id" categorieProduit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
