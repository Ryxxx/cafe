package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.SessionPanier;
import com.mycompany.myapp.repository.SessionPanierRepository;
import com.mycompany.myapp.service.SessionPanierService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SessionPanier}.
 */
@Service
@Transactional
public class SessionPanierServiceImpl implements SessionPanierService {

    private final Logger log = LoggerFactory.getLogger(SessionPanierServiceImpl.class);

    private final SessionPanierRepository sessionPanierRepository;

    public SessionPanierServiceImpl(SessionPanierRepository sessionPanierRepository) {
        this.sessionPanierRepository = sessionPanierRepository;
    }

    @Override
    public SessionPanier save(SessionPanier sessionPanier) {
        log.debug("Request to save SessionPanier : {}", sessionPanier);
        return sessionPanierRepository.save(sessionPanier);
    }

    @Override
    public Optional<SessionPanier> partialUpdate(SessionPanier sessionPanier) {
        log.debug("Request to partially update SessionPanier : {}", sessionPanier);

        return sessionPanierRepository
            .findById(sessionPanier.getId())
            .map(
                existingSessionPanier -> {
                    if (sessionPanier.getTotal() != null) {
                        existingSessionPanier.setTotal(sessionPanier.getTotal());
                    }
                    if (sessionPanier.getDateCreation() != null) {
                        existingSessionPanier.setDateCreation(sessionPanier.getDateCreation());
                    }
                    if (sessionPanier.getDateModification() != null) {
                        existingSessionPanier.setDateModification(sessionPanier.getDateModification());
                    }

                    return existingSessionPanier;
                }
            )
            .map(sessionPanierRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionPanier> findAll() {
        log.debug("Request to get all SessionPaniers");
        return sessionPanierRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SessionPanier> findOne(Long id) {
        log.debug("Request to get SessionPanier : {}", id);
        return sessionPanierRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SessionPanier : {}", id);
        sessionPanierRepository.deleteById(id);
    }
}
