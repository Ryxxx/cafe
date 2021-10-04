package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.ReductionProduit;
import com.mycompany.myapp.repository.ReductionProduitRepository;
import com.mycompany.myapp.service.ReductionProduitService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReductionProduit}.
 */
@Service
@Transactional
public class ReductionProduitServiceImpl implements ReductionProduitService {

    private final Logger log = LoggerFactory.getLogger(ReductionProduitServiceImpl.class);

    private final ReductionProduitRepository reductionProduitRepository;

    public ReductionProduitServiceImpl(ReductionProduitRepository reductionProduitRepository) {
        this.reductionProduitRepository = reductionProduitRepository;
    }

    @Override
    public ReductionProduit save(ReductionProduit reductionProduit) {
        log.debug("Request to save ReductionProduit : {}", reductionProduit);
        return reductionProduitRepository.save(reductionProduit);
    }

    @Override
    public Optional<ReductionProduit> partialUpdate(ReductionProduit reductionProduit) {
        log.debug("Request to partially update ReductionProduit : {}", reductionProduit);

        return reductionProduitRepository
            .findById(reductionProduit.getId())
            .map(
                existingReductionProduit -> {
                    if (reductionProduit.getNom() != null) {
                        existingReductionProduit.setNom(reductionProduit.getNom());
                    }
                    if (reductionProduit.getDescription() != null) {
                        existingReductionProduit.setDescription(reductionProduit.getDescription());
                    }
                    if (reductionProduit.getPourcentageReduction() != null) {
                        existingReductionProduit.setPourcentageReduction(reductionProduit.getPourcentageReduction());
                    }
                    if (reductionProduit.getActif() != null) {
                        existingReductionProduit.setActif(reductionProduit.getActif());
                    }
                    if (reductionProduit.getDateCreation() != null) {
                        existingReductionProduit.setDateCreation(reductionProduit.getDateCreation());
                    }
                    if (reductionProduit.getDateModification() != null) {
                        existingReductionProduit.setDateModification(reductionProduit.getDateModification());
                    }
                    if (reductionProduit.getDateSuppression() != null) {
                        existingReductionProduit.setDateSuppression(reductionProduit.getDateSuppression());
                    }

                    return existingReductionProduit;
                }
            )
            .map(reductionProduitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReductionProduit> findAll() {
        log.debug("Request to get all ReductionProduits");
        return reductionProduitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReductionProduit> findOne(Long id) {
        log.debug("Request to get ReductionProduit : {}", id);
        return reductionProduitRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReductionProduit : {}", id);
        reductionProduitRepository.deleteById(id);
    }
}
