package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.DetailsCommande;
import com.mycompany.myapp.repository.DetailsCommandeRepository;
import com.mycompany.myapp.service.DetailsCommandeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetailsCommande}.
 */
@Service
@Transactional
public class DetailsCommandeServiceImpl implements DetailsCommandeService {

    private final Logger log = LoggerFactory.getLogger(DetailsCommandeServiceImpl.class);

    private final DetailsCommandeRepository detailsCommandeRepository;

    public DetailsCommandeServiceImpl(DetailsCommandeRepository detailsCommandeRepository) {
        this.detailsCommandeRepository = detailsCommandeRepository;
    }

    @Override
    public DetailsCommande save(DetailsCommande detailsCommande) {
        log.debug("Request to save DetailsCommande : {}", detailsCommande);
        return detailsCommandeRepository.save(detailsCommande);
    }

    @Override
    public Optional<DetailsCommande> partialUpdate(DetailsCommande detailsCommande) {
        log.debug("Request to partially update DetailsCommande : {}", detailsCommande);

        return detailsCommandeRepository
            .findById(detailsCommande.getId())
            .map(
                existingDetailsCommande -> {
                    if (detailsCommande.getTotal() != null) {
                        existingDetailsCommande.setTotal(detailsCommande.getTotal());
                    }
                    if (detailsCommande.getDateCreation() != null) {
                        existingDetailsCommande.setDateCreation(detailsCommande.getDateCreation());
                    }
                    if (detailsCommande.getDateModification() != null) {
                        existingDetailsCommande.setDateModification(detailsCommande.getDateModification());
                    }

                    return existingDetailsCommande;
                }
            )
            .map(detailsCommandeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetailsCommande> findAll() {
        log.debug("Request to get all DetailsCommandes");
        return detailsCommandeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetailsCommande> findOne(Long id) {
        log.debug("Request to get DetailsCommande : {}", id);
        return detailsCommandeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DetailsCommande : {}", id);
        detailsCommandeRepository.deleteById(id);
    }
}
