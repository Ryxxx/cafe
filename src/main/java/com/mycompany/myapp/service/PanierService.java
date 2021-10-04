package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Panier;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Panier}.
 */
public interface PanierService {
    /**
     * Save a panier.
     *
     * @param panier the entity to save.
     * @return the persisted entity.
     */
    Panier save(Panier panier);

    /**
     * Partially updates a panier.
     *
     * @param panier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Panier> partialUpdate(Panier panier);

    /**
     * Get all the paniers.
     *
     * @return the list of entities.
     */
    List<Panier> findAll();

    /**
     * Get the "id" panier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Panier> findOne(Long id);

    /**
     * Delete the "id" panier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
