package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AdresseUtilisateur;
import com.mycompany.myapp.repository.AdresseUtilisateurRepository;
import com.mycompany.myapp.service.AdresseUtilisateurService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AdresseUtilisateur}.
 */
@Service
@Transactional
public class AdresseUtilisateurServiceImpl implements AdresseUtilisateurService {

    private final Logger log = LoggerFactory.getLogger(AdresseUtilisateurServiceImpl.class);

    private final AdresseUtilisateurRepository adresseUtilisateurRepository;

    public AdresseUtilisateurServiceImpl(AdresseUtilisateurRepository adresseUtilisateurRepository) {
        this.adresseUtilisateurRepository = adresseUtilisateurRepository;
    }

    @Override
    public AdresseUtilisateur save(AdresseUtilisateur adresseUtilisateur) {
        log.debug("Request to save AdresseUtilisateur : {}", adresseUtilisateur);
        return adresseUtilisateurRepository.save(adresseUtilisateur);
    }

    @Override
    public Optional<AdresseUtilisateur> partialUpdate(AdresseUtilisateur adresseUtilisateur) {
        log.debug("Request to partially update AdresseUtilisateur : {}", adresseUtilisateur);

        return adresseUtilisateurRepository
            .findById(adresseUtilisateur.getId())
            .map(
                existingAdresseUtilisateur -> {
                    if (adresseUtilisateur.getAdresseLigne1() != null) {
                        existingAdresseUtilisateur.setAdresseLigne1(adresseUtilisateur.getAdresseLigne1());
                    }
                    if (adresseUtilisateur.getAdresseLigne2() != null) {
                        existingAdresseUtilisateur.setAdresseLigne2(adresseUtilisateur.getAdresseLigne2());
                    }
                    if (adresseUtilisateur.getVille() != null) {
                        existingAdresseUtilisateur.setVille(adresseUtilisateur.getVille());
                    }
                    if (adresseUtilisateur.getCodePostal() != null) {
                        existingAdresseUtilisateur.setCodePostal(adresseUtilisateur.getCodePostal());
                    }
                    if (adresseUtilisateur.getPays() != null) {
                        existingAdresseUtilisateur.setPays(adresseUtilisateur.getPays());
                    }
                    if (adresseUtilisateur.getTelephone() != null) {
                        existingAdresseUtilisateur.setTelephone(adresseUtilisateur.getTelephone());
                    }

                    return existingAdresseUtilisateur;
                }
            )
            .map(adresseUtilisateurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdresseUtilisateur> findAll() {
        log.debug("Request to get all AdresseUtilisateurs");
        return adresseUtilisateurRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdresseUtilisateur> findOne(Long id) {
        log.debug("Request to get AdresseUtilisateur : {}", id);
        return adresseUtilisateurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AdresseUtilisateur : {}", id);
        adresseUtilisateurRepository.deleteById(id);
    }
}
