package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.StockProduit;
import com.mycompany.myapp.repository.StockProduitRepository;
import com.mycompany.myapp.service.StockProduitService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.StockProduit}.
 */
@RestController
@RequestMapping("/api")
public class StockProduitResource {

    private final Logger log = LoggerFactory.getLogger(StockProduitResource.class);

    private static final String ENTITY_NAME = "stockProduit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StockProduitService stockProduitService;

    private final StockProduitRepository stockProduitRepository;

    public StockProduitResource(StockProduitService stockProduitService, StockProduitRepository stockProduitRepository) {
        this.stockProduitService = stockProduitService;
        this.stockProduitRepository = stockProduitRepository;
    }

    /**
     * {@code POST  /stock-produits} : Create a new stockProduit.
     *
     * @param stockProduit the stockProduit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stockProduit, or with status {@code 400 (Bad Request)} if the stockProduit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stock-produits")
    public ResponseEntity<StockProduit> createStockProduit(@RequestBody StockProduit stockProduit) throws URISyntaxException {
        log.debug("REST request to save StockProduit : {}", stockProduit);
        if (stockProduit.getId() != null) {
            throw new BadRequestAlertException("A new stockProduit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockProduit result = stockProduitService.save(stockProduit);
        return ResponseEntity
            .created(new URI("/api/stock-produits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stock-produits/:id} : Updates an existing stockProduit.
     *
     * @param id the id of the stockProduit to save.
     * @param stockProduit the stockProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockProduit,
     * or with status {@code 400 (Bad Request)} if the stockProduit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stockProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stock-produits/{id}")
    public ResponseEntity<StockProduit> updateStockProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockProduit stockProduit
    ) throws URISyntaxException {
        log.debug("REST request to update StockProduit : {}, {}", id, stockProduit);
        if (stockProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StockProduit result = stockProduitService.save(stockProduit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockProduit.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stock-produits/:id} : Partial updates given fields of an existing stockProduit, field will ignore if it is null
     *
     * @param id the id of the stockProduit to save.
     * @param stockProduit the stockProduit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stockProduit,
     * or with status {@code 400 (Bad Request)} if the stockProduit is not valid,
     * or with status {@code 404 (Not Found)} if the stockProduit is not found,
     * or with status {@code 500 (Internal Server Error)} if the stockProduit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stock-produits/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<StockProduit> partialUpdateStockProduit(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StockProduit stockProduit
    ) throws URISyntaxException {
        log.debug("REST request to partial update StockProduit partially : {}, {}", id, stockProduit);
        if (stockProduit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stockProduit.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stockProduitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StockProduit> result = stockProduitService.partialUpdate(stockProduit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockProduit.getId().toString())
        );
    }

    /**
     * {@code GET  /stock-produits} : get all the stockProduits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stockProduits in body.
     */
    @GetMapping("/stock-produits")
    public List<StockProduit> getAllStockProduits() {
        log.debug("REST request to get all StockProduits");
        return stockProduitService.findAll();
    }

    /**
     * {@code GET  /stock-produits/:id} : get the "id" stockProduit.
     *
     * @param id the id of the stockProduit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stockProduit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stock-produits/{id}")
    public ResponseEntity<StockProduit> getStockProduit(@PathVariable Long id) {
        log.debug("REST request to get StockProduit : {}", id);
        Optional<StockProduit> stockProduit = stockProduitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockProduit);
    }

    /**
     * {@code DELETE  /stock-produits/:id} : delete the "id" stockProduit.
     *
     * @param id the id of the stockProduit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stock-produits/{id}")
    public ResponseEntity<Void> deleteStockProduit(@PathVariable Long id) {
        log.debug("REST request to delete StockProduit : {}", id);
        stockProduitService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
