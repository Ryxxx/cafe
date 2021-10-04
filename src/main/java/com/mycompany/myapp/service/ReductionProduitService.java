package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ReductionProduit;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ReductionProduit}.
 */
public interface ReductionProduitService {
    /**
     * Save a reductionProduit.
     *
     * @param reductionProduit the entity to save.
     * @return the persisted entity.
     */
    ReductionProduit save(ReductionProduit reductionProduit);

    /**
     * Partially updates a reductionProduit.
     *
     * @param reductionProduit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReductionProduit> partialUpdate(ReductionProduit reductionProduit);

    /**
     * Get all the reductionProduits.
     *
     * @return the list of entities.
     */
    List<ReductionProduit> findAll();

    /**
     * Get the "id" reductionProduit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReductionProduit> findOne(Long id);

    /**
     * Delete the "id" reductionProduit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
