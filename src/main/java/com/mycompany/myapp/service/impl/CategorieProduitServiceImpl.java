package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CategorieProduit;
import com.mycompany.myapp.repository.CategorieProduitRepository;
import com.mycompany.myapp.service.CategorieProduitService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategorieProduit}.
 */
@Service
@Transactional
public class CategorieProduitServiceImpl implements CategorieProduitService {

    private final Logger log = LoggerFactory.getLogger(CategorieProduitServiceImpl.class);

    private final CategorieProduitRepository categorieProduitRepository;

    public CategorieProduitServiceImpl(CategorieProduitRepository categorieProduitRepository) {
        this.categorieProduitRepository = categorieProduitRepository;
    }

    @Override
    public CategorieProduit save(CategorieProduit categorieProduit) {
        log.debug("Request to save CategorieProduit : {}", categorieProduit);
        return categorieProduitRepository.save(categorieProduit);
    }

    @Override
    public Optional<CategorieProduit> partialUpdate(CategorieProduit categorieProduit) {
        log.debug("Request to partially update CategorieProduit : {}", categorieProduit);

        return categorieProduitRepository
            .findById(categorieProduit.getId())
            .map(
                existingCategorieProduit -> {
                    if (categorieProduit.getNom() != null) {
                        existingCategorieProduit.setNom(categorieProduit.getNom());
                    }
                    if (categorieProduit.getDescription() != null) {
                        existingCategorieProduit.setDescription(categorieProduit.getDescription());
                    }
                    if (categorieProduit.getDateCreation() != null) {
                        existingCategorieProduit.setDateCreation(categorieProduit.getDateCreation());
                    }
                    if (categorieProduit.getDateModification() != null) {
                        existingCategorieProduit.setDateModification(categorieProduit.getDateModification());
                    }
                    if (categorieProduit.getDateSuppression() != null) {
                        existingCategorieProduit.setDateSuppression(categorieProduit.getDateSuppression());
                    }

                    return existingCategorieProduit;
                }
            )
            .map(categorieProduitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorieProduit> findAll() {
        log.debug("Request to get all CategorieProduits");
        return categorieProduitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategorieProduit> findOne(Long id) {
        log.debug("Request to get CategorieProduit : {}", id);
        return categorieProduitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategorieProduit : {}", id);
        categorieProduitRepository.deleteById(id);
    }
}
