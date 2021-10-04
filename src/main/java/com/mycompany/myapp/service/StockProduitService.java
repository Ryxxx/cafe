package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.StockProduit;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link StockProduit}.
 */
public interface StockProduitService {
    /**
     * Save a stockProduit.
     *
     * @param stockProduit the entity to save.
     * @return the persisted entity.
     */
    StockProduit save(StockProduit stockProduit);

    /**
     * Partially updates a stockProduit.
     *
     * @param stockProduit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StockProduit> partialUpdate(StockProduit stockProduit);

    /**
     * Get all the stockProduits.
     *
     * @return the list of entities.
     */
    List<StockProduit> findAll();

    /**
     * Get the "id" stockProduit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StockProduit> findOne(Long id);

    /**
     * Delete the "id" stockProduit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
