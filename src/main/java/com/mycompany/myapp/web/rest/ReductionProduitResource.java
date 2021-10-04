package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ReductionProduit;
import com.mycompany.myapp.repository.ReductionProduitRepository;
import com.mycompany.myapp.service.ReductionProduitService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ReductionProduit}.
 */
@RestController
@RequestMapping("/api")
public class ReductionProduitResource {

    private final Logger log = LoggerFactory.getLogger(ReductionProduitResource.class);

    private static final String ENTITY_NAME = "reductionProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReductionProduitService reductionProduitService;

    private final ReductionProduitRepository reductionProduitRepository;

    public ReductionProduitResource(
        ReductionProduitService reductionProduitService,
        ReductionProduitRepository reductionProduitRepository
    ) {
        this.reductionProduitService = reductionProduitService;
        this.reductionProduitRepository = reductionProduitRepository;
    }

    /**
     * {@code POST  /reduction-produits} : Create a new reductionProduit.
     *
     * @param reductionProduit the reductionProduit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reductionProduit, or with status {@code 400 (Bad Request)} if the reductionProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reduction-produits")
    public ResponseEntity<ReductionProduit> createReductionProduit(@RequestBody ReductionProduit reductionProduit)
        throws URISyntaxException {
        log.debug("REST request to save ReductionProduit : {}", reductionProduit);
        if (reductionProduit.getId() != null) {
            throw new BadRequestAlertException("A new reductionProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReductionProduit result = reductionProduitService.save(reductionProduit);
        return ResponseEntity
            .created(new URI("/api/reduction-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reduction-produits/:id} : Updates an existing reductionProduit.
     *
     * @param id the id of the reductionProduit to save.
     * @param reductionProduit the reductionProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reductionProduit,
     * or with status {@code 400 (Bad Request)} if the reductionProduit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reductionProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reduction-produits/{id}")
    public ResponseEntity<ReductionProduit> updateReductionProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReductionProduit reductionProduit
    ) throws URISyntaxException {
        log.debug("REST request to update ReductionProduit : {}, {}", id, reductionProduit);
        if (reductionProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reductionProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reductionProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReductionProduit result = reductionProduitService.save(reductionProduit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reductionProduit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reduction-produits/:id} : Partial updates given fields of an existing reductionProduit, field will ignore if it is null
     *
     * @param id the id of the reductionProduit to save.
     * @param reductionProduit the reductionProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reductionProduit,
     * or with status {@code 400 (Bad Request)} if the reductionProduit is not valid,
     * or with status {@code 404 (Not Found)} if the reductionProduit is not found,
     * or with status {@code 500 (Internal Server Error)} if the reductionProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reduction-produits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReductionProduit> partialUpdateReductionProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReductionProduit reductionProduit
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReductionProduit partially : {}, {}", id, reductionProduit);
        if (reductionProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reductionProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reductionProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReductionProduit> result = reductionProduitService.partialUpdate(reductionProduit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reductionProduit.getId().toString())
        );
    }

    /**
     * {@code GET  /reduction-produits} : get all the reductionProduits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reductionProduits in body.
     */
    @GetMapping("/reduction-produits")
    public List<ReductionProduit> getAllReductionProduits() {
        log.debug("REST request to get all ReductionProduits");
        return reductionProduitService.findAll();
    }

    /**
     * {@code GET  /reduction-produits/:id} : get the "id" reductionProduit.
     *
     * @param id the id of the reductionProduit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reductionProduit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reduction-produits/{id}")
    public ResponseEntity<ReductionProduit> getReductionProduit(@PathVariable Long id) {
        log.debug("REST request to get ReductionProduit : {}", id);
        Optional<ReductionProduit> reductionProduit = reductionProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reductionProduit);
    }

    /**
     * {@code DELETE  /reduction-produits/:id} : delete the "id" reductionProduit.
     *
     * @param id the id of the reductionProduit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reduction-produits/{id}")
    public ResponseEntity<Void> deleteReductionProduit(@PathVariable Long id) {
        log.debug("REST request to delete ReductionProduit : {}", id);
        reductionProduitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
