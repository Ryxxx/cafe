package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Produit;
import com.mycompany.myapp.repository.ProduitRepository;
import com.mycompany.myapp.service.ProduitService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Produit}.
 */
@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final Logger log = LoggerFactory.getLogger(ProduitServiceImpl.class);

    private final ProduitRepository produitRepository;

    public ProduitServiceImpl(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @Override
    public Produit save(Produit produit) {
        log.debug("Request to save Produit : {}", produit);
        return produitRepository.save(produit);
    }

    @Override
    public Optional<Produit> partialUpdate(Produit produit) {
        log.debug("Request to partially update Produit : {}", produit);

        return produitRepository
            .findById(produit.getId())
            .map(
                existingProduit -> {
                    if (produit.getNom() != null) {
                        existingProduit.setNom(produit.getNom());
                    }
                    if (produit.getDescription() != null) {
                        existingProduit.setDescription(produit.getDescription());
                    }
                    if (produit.getSku() != null) {
                        existingProduit.setSku(produit.getSku());
                    }
                    if (produit.getPrix() != null) {
                        existingProduit.setPrix(produit.getPrix());
                    }
                    if (produit.getReductionId() != null) {
                        existingProduit.setReductionId(produit.getReductionId());
                    }
                    if (produit.getDateCreation() != null) {
                        existingProduit.setDateCreation(produit.getDateCreation());
                    }
                    if (produit.getDateModification() != null) {
                        existingProduit.setDateModification(produit.getDateModification());
                    }
                    if (produit.getDateSuppression() != null) {
                        existingProduit.setDateSuppression(produit.getDateSuppression());
                    }

                    return existingProduit;
                }
            )
            .map(produitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> findAll() {
        log.debug("Request to get all Produits");
        return produitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Produit> findOne(Long id) {
        log.debug("Request to get Produit : {}", id);
        return produitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Produit : {}", id);
        produitRepository.deleteById(id);
    }
}
