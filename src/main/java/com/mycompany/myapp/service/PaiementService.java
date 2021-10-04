package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Paiement;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Paiement}.
 */
public interface PaiementService {
    /**
     * Save a paiement.
     *
     * @param paiement the entity to save.
     * @return the persisted entity.
     */
    Paiement save(Paiement paiement);

    /**
     * Partially updates a paiement.
     *
     * @param paiement the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Paiement> partialUpdate(Paiement paiement);

    /**
     * Get all the paiements.
     *
     * @return the list of entities.
     */
    List<Paiement> findAll();

    /**
     * Get the "id" paiement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Paiement> findOne(Long id);

    /**
     * Delete the "id" paiement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
