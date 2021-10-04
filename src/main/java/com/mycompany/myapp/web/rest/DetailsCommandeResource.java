package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DetailsCommande;
import com.mycompany.myapp.repository.DetailsCommandeRepository;
import com.mycompany.myapp.service.DetailsCommandeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DetailsCommande}.
 */
@RestController
@RequestMapping("/api")
public class DetailsCommandeResource {

    private final Logger log = LoggerFactory.getLogger(DetailsCommandeResource.class);

    private static final String ENTITY_NAME = "detailsCommande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailsCommandeService detailsCommandeService;

    private final DetailsCommandeRepository detailsCommandeRepository;

    public DetailsCommandeResource(DetailsCommandeService detailsCommandeService, DetailsCommandeRepository detailsCommandeRepository) {
        this.detailsCommandeService = detailsCommandeService;
        this.detailsCommandeRepository = detailsCommandeRepository;
    }

    /**
     * {@code POST  /details-commandes} : Create a new detailsCommande.
     *
     * @param detailsCommande the detailsCommande to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailsCommande, or with status {@code 400 (Bad Request)} if the detailsCommande has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/details-commandes")
    public ResponseEntity<DetailsCommande> createDetailsCommande(@RequestBody DetailsCommande detailsCommande) throws URISyntaxException {
        log.debug("REST request to save DetailsCommande : {}", detailsCommande);
        if (detailsCommande.getId() != null) {
            throw new BadRequestAlertException("A new detailsCommande cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailsCommande result = detailsCommandeService.save(detailsCommande);
        return ResponseEntity
            .created(new URI("/api/details-commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /details-commandes/:id} : Updates an existing detailsCommande.
     *
     * @param id the id of the detailsCommande to save.
     * @param detailsCommande the detailsCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailsCommande,
     * or with status {@code 400 (Bad Request)} if the detailsCommande is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailsCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/details-commandes/{id}")
    public ResponseEntity<DetailsCommande> updateDetailsCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailsCommande detailsCommande
    ) throws URISyntaxException {
        log.debug("REST request to update DetailsCommande : {}, {}", id, detailsCommande);
        if (detailsCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailsCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailsCommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailsCommande result = detailsCommandeService.save(detailsCommande);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailsCommande.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /details-commandes/:id} : Partial updates given fields of an existing detailsCommande, field will ignore if it is null
     *
     * @param id the id of the detailsCommande to save.
     * @param detailsCommande the detailsCommande to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailsCommande,
     * or with status {@code 400 (Bad Request)} if the detailsCommande is not valid,
     * or with status {@code 404 (Not Found)} if the detailsCommande is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailsCommande couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/details-commandes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DetailsCommande> partialUpdateDetailsCommande(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailsCommande detailsCommande
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailsCommande partially : {}, {}", id, detailsCommande);
        if (detailsCommande.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailsCommande.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailsCommandeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailsCommande> result = detailsCommandeService.partialUpdate(detailsCommande);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailsCommande.getId().toString())
        );
    }

    /**
     * {@code GET  /details-commandes} : get all the detailsCommandes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailsCommandes in body.
     */
    @GetMapping("/details-commandes")
    public List<DetailsCommande> getAllDetailsCommandes() {
        log.debug("REST request to get all DetailsCommandes");
        return detailsCommandeService.findAll();
    }

    /**
     * {@code GET  /details-commandes/:id} : get the "id" detailsCommande.
     *
     * @param id the id of the detailsCommande to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailsCommande, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/details-commandes/{id}")
    public ResponseEntity<DetailsCommande> getDetailsCommande(@PathVariable Long id) {
        log.debug("REST request to get DetailsCommande : {}", id);
        Optional<DetailsCommande> detailsCommande = detailsCommandeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailsCommande);
    }

    /**
     * {@code DELETE  /details-commandes/:id} : delete the "id" detailsCommande.
     *
     * @param id the id of the detailsCommande to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/details-commandes/{id}")
    public ResponseEntity<Void> deleteDetailsCommande(@PathVariable Long id) {
        log.debug("REST request to delete DetailsCommande : {}", id);
        detailsCommandeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
