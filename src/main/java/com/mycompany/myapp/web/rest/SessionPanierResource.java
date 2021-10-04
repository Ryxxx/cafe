package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SessionPanier;
import com.mycompany.myapp.repository.SessionPanierRepository;
import com.mycompany.myapp.service.SessionPanierService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SessionPanier}.
 */
@RestController
@RequestMapping("/api")
public class SessionPanierResource {

    private final Logger log = LoggerFactory.getLogger(SessionPanierResource.class);

    private static final String ENTITY_NAME = "sessionPanier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SessionPanierService sessionPanierService;

    private final SessionPanierRepository sessionPanierRepository;

    public SessionPanierResource(SessionPanierService sessionPanierService, SessionPanierRepository sessionPanierRepository) {
        this.sessionPanierService = sessionPanierService;
        this.sessionPanierRepository = sessionPanierRepository;
    }

    /**
     * {@code POST  /session-paniers} : Create a new sessionPanier.
     *
     * @param sessionPanier the sessionPanier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sessionPanier, or with status {@code 400 (Bad Request)} if the sessionPanier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/session-paniers")
    public ResponseEntity<SessionPanier> createSessionPanier(@RequestBody SessionPanier sessionPanier) throws URISyntaxException {
        log.debug("REST request to save SessionPanier : {}", sessionPanier);
        if (sessionPanier.getId() != null) {
            throw new BadRequestAlertException("A new sessionPanier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SessionPanier result = sessionPanierService.save(sessionPanier);
        return ResponseEntity
            .created(new URI("/api/session-paniers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /session-paniers/:id} : Updates an existing sessionPanier.
     *
     * @param id the id of the sessionPanier to save.
     * @param sessionPanier the sessionPanier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionPanier,
     * or with status {@code 400 (Bad Request)} if the sessionPanier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sessionPanier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/session-paniers/{id}")
    public ResponseEntity<SessionPanier> updateSessionPanier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionPanier sessionPanier
    ) throws URISyntaxException {
        log.debug("REST request to update SessionPanier : {}, {}", id, sessionPanier);
        if (sessionPanier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionPanier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionPanierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SessionPanier result = sessionPanierService.save(sessionPanier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionPanier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /session-paniers/:id} : Partial updates given fields of an existing sessionPanier, field will ignore if it is null
     *
     * @param id the id of the sessionPanier to save.
     * @param sessionPanier the sessionPanier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sessionPanier,
     * or with status {@code 400 (Bad Request)} if the sessionPanier is not valid,
     * or with status {@code 404 (Not Found)} if the sessionPanier is not found,
     * or with status {@code 500 (Internal Server Error)} if the sessionPanier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/session-paniers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<SessionPanier> partialUpdateSessionPanier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SessionPanier sessionPanier
    ) throws URISyntaxException {
        log.debug("REST request to partial update SessionPanier partially : {}, {}", id, sessionPanier);
        if (sessionPanier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sessionPanier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sessionPanierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SessionPanier> result = sessionPanierService.partialUpdate(sessionPanier);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sessionPanier.getId().toString())
        );
    }

    /**
     * {@code GET  /session-paniers} : get all the sessionPaniers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sessionPaniers in body.
     */
    @GetMapping("/session-paniers")
    public List<SessionPanier> getAllSessionPaniers() {
        log.debug("REST request to get all SessionPaniers");
        return sessionPanierService.findAll();
    }

    /**
     * {@code GET  /session-paniers/:id} : get the "id" sessionPanier.
     *
     * @param id the id of the sessionPanier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sessionPanier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/session-paniers/{id}")
    public ResponseEntity<SessionPanier> getSessionPanier(@PathVariable Long id) {
        log.debug("REST request to get SessionPanier : {}", id);
        Optional<SessionPanier> sessionPanier = sessionPanierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sessionPanier);
    }

    /**
     * {@code DELETE  /session-paniers/:id} : delete the "id" sessionPanier.
     *
     * @param id the id of the sessionPanier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/session-paniers/{id}")
    public ResponseEntity<Void> deleteSessionPanier(@PathVariable Long id) {
        log.debug("REST request to delete SessionPanier : {}", id);
        sessionPanierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
