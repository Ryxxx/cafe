package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CategorieProduit;
import com.mycompany.myapp.repository.CategorieProduitRepository;
import com.mycompany.myapp.service.CategorieProduitService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CategorieProduit}.
 */
@RestController
@RequestMapping("/api")
public class CategorieProduitResource {

    private final Logger log = LoggerFactory.getLogger(CategorieProduitResource.class);

    private static final String ENTITY_NAME = "categorieProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategorieProduitService categorieProduitService;

    private final CategorieProduitRepository categorieProduitRepository;

    public CategorieProduitResource(
        CategorieProduitService categorieProduitService,
        CategorieProduitRepository categorieProduitRepository
    ) {
        this.categorieProduitService = categorieProduitService;
        this.categorieProduitRepository = categorieProduitRepository;
    }

    /**
     * {@code POST  /categorie-produits} : Create a new categorieProduit.
     *
     * @param categorieProduit the categorieProduit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categorieProduit, or with status {@code 400 (Bad Request)} if the categorieProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/categorie-produits")
    public ResponseEntity<CategorieProduit> createCategorieProduit(@RequestBody CategorieProduit categorieProduit)
        throws URISyntaxException {
        log.debug("REST request to save CategorieProduit : {}", categorieProduit);
        if (categorieProduit.getId() != null) {
            throw new BadRequestAlertException("A new categorieProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorieProduit result = categorieProduitService.save(categorieProduit);
        return ResponseEntity
            .created(new URI("/api/categorie-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /categorie-produits/:id} : Updates an existing categorieProduit.
     *
     * @param id the id of the categorieProduit to save.
     * @param categorieProduit the categorieProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieProduit,
     * or with status {@code 400 (Bad Request)} if the categorieProduit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categorieProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/categorie-produits/{id}")
    public ResponseEntity<CategorieProduit> updateCategorieProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategorieProduit categorieProduit
    ) throws URISyntaxException {
        log.debug("REST request to update CategorieProduit : {}, {}", id, categorieProduit);
        if (categorieProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategorieProduit result = categorieProduitService.save(categorieProduit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieProduit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /categorie-produits/:id} : Partial updates given fields of an existing categorieProduit, field will ignore if it is null
     *
     * @param id the id of the categorieProduit to save.
     * @param categorieProduit the categorieProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categorieProduit,
     * or with status {@code 400 (Bad Request)} if the categorieProduit is not valid,
     * or with status {@code 404 (Not Found)} if the categorieProduit is not found,
     * or with status {@code 500 (Internal Server Error)} if the categorieProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/categorie-produits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CategorieProduit> partialUpdateCategorieProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CategorieProduit categorieProduit
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategorieProduit partially : {}, {}", id, categorieProduit);
        if (categorieProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorieProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategorieProduit> result = categorieProduitService.partialUpdate(categorieProduit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categorieProduit.getId().toString())
        );
    }

    /**
     * {@code GET  /categorie-produits} : get all the categorieProduits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categorieProduits in body.
     */
    @GetMapping("/categorie-produits")
    public List<CategorieProduit> getAllCategorieProduits() {
        log.debug("REST request to get all CategorieProduits");
        return categorieProduitService.findAll();
    }

    /**
     * {@code GET  /categorie-produits/:id} : get the "id" categorieProduit.
     *
     * @param id the id of the categorieProduit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categorieProduit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/categorie-produits/{id}")
    public ResponseEntity<CategorieProduit> getCategorieProduit(@PathVariable Long id) {
        log.debug("REST request to get CategorieProduit : {}", id);
        Optional<CategorieProduit> categorieProduit = categorieProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categorieProduit);
    }

    /**
     * {@code DELETE  /categorie-produits/:id} : delete the "id" categorieProduit.
     *
     * @param id the id of the categorieProduit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/categorie-produits/{id}")
    public ResponseEntity<Void> deleteCategorieProduit(@PathVariable Long id) {
        log.debug("REST request to delete CategorieProduit : {}", id);
        categorieProduitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
