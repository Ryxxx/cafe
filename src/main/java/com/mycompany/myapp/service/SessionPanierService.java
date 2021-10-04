package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SessionPanier;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link SessionPanier}.
 */
public interface SessionPanierService {
    /**
     * Save a sessionPanier.
     *
     * @param sessionPanier the entity to save.
     * @return the persisted entity.
     */
    SessionPanier save(SessionPanier sessionPanier);

    /**
     * Partially updates a sessionPanier.
     *
     * @param sessionPanier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SessionPanier> partialUpdate(SessionPanier sessionPanier);

    /**
     * Get all the sessionPaniers.
     *
     * @return the list of entities.
     */
    List<SessionPanier> findAll();

    /**
     * Get the "id" sessionPanier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SessionPanier> findOne(Long id);

    /**
     * Delete the "id" sessionPanier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
