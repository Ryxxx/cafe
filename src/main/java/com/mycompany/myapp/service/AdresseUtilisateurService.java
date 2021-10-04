package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AdresseUtilisateur;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AdresseUtilisateur}.
 */
public interface AdresseUtilisateurService {
    /**
     * Save a adresseUtilisateur.
     *
     * @param adresseUtilisateur the entity to save.
     * @return the persisted entity.
     */
    AdresseUtilisateur save(AdresseUtilisateur adresseUtilisateur);

    /**
     * Partially updates a adresseUtilisateur.
     *
     * @param adresseUtilisateur the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AdresseUtilisateur> partialUpdate(AdresseUtilisateur adresseUtilisateur);

    /**
     * Get all the adresseUtilisateurs.
     *
     * @return the list of entities.
     */
    List<AdresseUtilisateur> findAll();

    /**
     * Get the "id" adresseUtilisateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AdresseUtilisateur> findOne(Long id);

    /**
     * Delete the "id" adresseUtilisateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
