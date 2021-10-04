package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AdresseUtilisateur;
import com.mycompany.myapp.repository.AdresseUtilisateurRepository;
import com.mycompany.myapp.service.AdresseUtilisateurService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.AdresseUtilisateur}.
 */
@RestController
@RequestMapping("/api")
public class AdresseUtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(AdresseUtilisateurResource.class);

    private static final String ENTITY_NAME = "adresseUtilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdresseUtilisateurService adresseUtilisateurService;

    private final AdresseUtilisateurRepository adresseUtilisateurRepository;

    public AdresseUtilisateurResource(
        AdresseUtilisateurService adresseUtilisateurService,
        AdresseUtilisateurRepository adresseUtilisateurRepository
    ) {
        this.adresseUtilisateurService = adresseUtilisateurService;
        this.adresseUtilisateurRepository = adresseUtilisateurRepository;
    }

    /**
     * {@code POST  /adresse-utilisateurs} : Create a new adresseUtilisateur.
     *
     * @param adresseUtilisateur the adresseUtilisateur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adresseUtilisateur, or with status {@code 400 (Bad Request)} if the adresseUtilisateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/adresse-utilisateurs")
    public ResponseEntity<AdresseUtilisateur> createAdresseUtilisateur(@RequestBody AdresseUtilisateur adresseUtilisateur)
        throws URISyntaxException {
        log.debug("REST request to save AdresseUtilisateur : {}", adresseUtilisateur);
        if (adresseUtilisateur.getId() != null) {
            throw new BadRequestAlertException("A new adresseUtilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdresseUtilisateur result = adresseUtilisateurService.save(adresseUtilisateur);
        return ResponseEntity
            .created(new URI("/api/adresse-utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /adresse-utilisateurs/:id} : Updates an existing adresseUtilisateur.
     *
     * @param id the id of the adresseUtilisateur to save.
     * @param adresseUtilisateur the adresseUtilisateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adresseUtilisateur,
     * or with status {@code 400 (Bad Request)} if the adresseUtilisateur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adresseUtilisateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/adresse-utilisateurs/{id}")
    public ResponseEntity<AdresseUtilisateur> updateAdresseUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdresseUtilisateur adresseUtilisateur
    ) throws URISyntaxException {
        log.debug("REST request to update AdresseUtilisateur : {}, {}", id, adresseUtilisateur);
        if (adresseUtilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adresseUtilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adresseUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdresseUtilisateur result = adresseUtilisateurService.save(adresseUtilisateur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adresseUtilisateur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /adresse-utilisateurs/:id} : Partial updates given fields of an existing adresseUtilisateur, field will ignore if it is null
     *
     * @param id the id of the adresseUtilisateur to save.
     * @param adresseUtilisateur the adresseUtilisateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adresseUtilisateur,
     * or with status {@code 400 (Bad Request)} if the adresseUtilisateur is not valid,
     * or with status {@code 404 (Not Found)} if the adresseUtilisateur is not found,
     * or with status {@code 500 (Internal Server Error)} if the adresseUtilisateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/adresse-utilisateurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AdresseUtilisateur> partialUpdateAdresseUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AdresseUtilisateur adresseUtilisateur
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdresseUtilisateur partially : {}, {}", id, adresseUtilisateur);
        if (adresseUtilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adresseUtilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adresseUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdresseUtilisateur> result = adresseUtilisateurService.partialUpdate(adresseUtilisateur);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adresseUtilisateur.getId().toString())
        );
    }

    /**
     * {@code GET  /adresse-utilisateurs} : get all the adresseUtilisateurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adresseUtilisateurs in body.
     */
    @GetMapping("/adresse-utilisateurs")
    public List<AdresseUtilisateur> getAllAdresseUtilisateurs() {
        log.debug("REST request to get all AdresseUtilisateurs");
        return adresseUtilisateurService.findAll();
    }

    /**
     * {@code GET  /adresse-utilisateurs/:id} : get the "id" adresseUtilisateur.
     *
     * @param id the id of the adresseUtilisateur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adresseUtilisateur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/adresse-utilisateurs/{id}")
    public ResponseEntity<AdresseUtilisateur> getAdresseUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get AdresseUtilisateur : {}", id);
        Optional<AdresseUtilisateur> adresseUtilisateur = adresseUtilisateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adresseUtilisateur);
    }

    /**
     * {@code DELETE  /adresse-utilisateurs/:id} : delete the "id" adresseUtilisateur.
     *
     * @param id the id of the adresseUtilisateur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/adresse-utilisateurs/{id}")
    public ResponseEntity<Void> deleteAdresseUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete AdresseUtilisateur : {}", id);
        adresseUtilisateurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
