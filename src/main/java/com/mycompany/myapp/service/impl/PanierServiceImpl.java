package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Panier;
import com.mycompany.myapp.repository.PanierRepository;
import com.mycompany.myapp.service.PanierService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Panier}.
 */
@Service
@Transactional
public class PanierServiceImpl implements PanierService {

    private final Logger log = LoggerFactory.getLogger(PanierServiceImpl.class);

    private final PanierRepository panierRepository;

    public PanierServiceImpl(PanierRepository panierRepository) {
        this.panierRepository = panierRepository;
    }

    @Override
    public Panier save(Panier panier) {
        log.debug("Request to save Panier : {}", panier);
        return panierRepository.save(panier);
    }

    @Override
    public Optional<Panier> partialUpdate(Panier panier) {
        log.debug("Request to partially update Panier : {}", panier);

        return panierRepository
            .findById(panier.getId())
            .map(
                existingPanier -> {
                    if (panier.getQuantite() != null) {
                        existingPanier.setQuantite(panier.getQuantite());
                    }
                    if (panier.getDateCreation() != null) {
                        existingPanier.setDateCreation(panier.getDateCreation());
                    }
                    if (panier.getDateModification() != null) {
                        existingPanier.setDateModification(panier.getDateModification());
                    }

                    return existingPanier;
                }
            )
            .map(panierRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Panier> findAll() {
        log.debug("Request to get all Paniers");
        return panierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Panier> findOne(Long id) {
        log.debug("Request to get Panier : {}", id);
        return panierRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Panier : {}", id);
        panierRepository.deleteById(id);
    }
}
