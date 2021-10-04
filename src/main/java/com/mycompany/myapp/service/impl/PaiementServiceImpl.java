package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Paiement;
import com.mycompany.myapp.repository.PaiementRepository;
import com.mycompany.myapp.service.PaiementService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Paiement}.
 */
@Service
@Transactional
public class PaiementServiceImpl implements PaiementService {

    private final Logger log = LoggerFactory.getLogger(PaiementServiceImpl.class);

    private final PaiementRepository paiementRepository;

    public PaiementServiceImpl(PaiementRepository paiementRepository) {
        this.paiementRepository = paiementRepository;
    }

    @Override
    public Paiement save(Paiement paiement) {
        log.debug("Request to save Paiement : {}", paiement);
        return paiementRepository.save(paiement);
    }

    @Override
    public Optional<Paiement> partialUpdate(Paiement paiement) {
        log.debug("Request to partially update Paiement : {}", paiement);

        return paiementRepository
            .findById(paiement.getId())
            .map(
                existingPaiement -> {
                    if (paiement.getQuantite() != null) {
                        existingPaiement.setQuantite(paiement.getQuantite());
                    }
                    if (paiement.getProvider() != null) {
                        existingPaiement.setProvider(paiement.getProvider());
                    }
                    if (paiement.getStatut() != null) {
                        existingPaiement.setStatut(paiement.getStatut());
                    }
                    if (paiement.getDateCreation() != null) {
                        existingPaiement.setDateCreation(paiement.getDateCreation());
                    }
                    if (paiement.getDateModification() != null) {
                        existingPaiement.setDateModification(paiement.getDateModification());
                    }

                    return existingPaiement;
                }
            )
            .map(paiementRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paiement> findAll() {
        log.debug("Request to get all Paiements");
        return paiementRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paiement> findOne(Long id) {
        log.debug("Request to get Paiement : {}", id);
        return paiementRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Paiement : {}", id);
        paiementRepository.deleteById(id);
    }
}
