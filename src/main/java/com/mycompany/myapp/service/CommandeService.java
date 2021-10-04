package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Commande;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Commande}.
 */
public interface CommandeService {
    /**
     * Save a commande.
     *
     * @param commande the entity to save.
     * @return the persisted entity.
     */
    Commande save(Commande commande);

    /**
     * Partially updates a commande.
     *
     * @param commande the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Commande> partialUpdate(Commande commande);

    /**
     * Get all the commandes.
     *
     * @return the list of entities.
     */
    List<Commande> findAll();

    /**
     * Get the "id" commande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Commande> findOne(Long id);

    /**
     * Delete the "id" commande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
