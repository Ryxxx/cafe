package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DetailsCommande;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DetailsCommande}.
 */
public interface DetailsCommandeService {
    /**
     * Save a detailsCommande.
     *
     * @param detailsCommande the entity to save.
     * @return the persisted entity.
     */
    DetailsCommande save(DetailsCommande detailsCommande);

    /**
     * Partially updates a detailsCommande.
     *
     * @param detailsCommande the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DetailsCommande> partialUpdate(DetailsCommande detailsCommande);

    /**
     * Get all the detailsCommandes.
     *
     * @return the list of entities.
     */
    List<DetailsCommande> findAll();

    /**
     * Get the "id" detailsCommande.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DetailsCommande> findOne(Long id);

    /**
     * Delete the "id" detailsCommande.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
