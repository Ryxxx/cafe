package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.StockProduit;
import com.mycompany.myapp.repository.StockProduitRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link StockProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StockProduitResourceIT {

    private static final Long DEFAULT_QUANTITE = 1L;
    private static final Long UPDATED_QUANTITE = 2L;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_SUPPRESSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SUPPRESSION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/stock-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StockProduitRepository stockProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockProduitMockMvc;

    private StockProduit stockProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockProduit createEntity(EntityManager em) {
        StockProduit stockProduit = new StockProduit()
            .quantite(DEFAULT_QUANTITE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateSuppression(DEFAULT_DATE_SUPPRESSION);
        return stockProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StockProduit createUpdatedEntity(EntityManager em) {
        StockProduit stockProduit = new StockProduit()
            .quantite(UPDATED_QUANTITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);
        return stockProduit;
    }

    @BeforeEach
    public void initTest() {
        stockProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createStockProduit() throws Exception {
        int databaseSizeBeforeCreate = stockProduitRepository.findAll().size();
        // Create the StockProduit
        restStockProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockProduit)))
            .andExpect(status().isCreated());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeCreate + 1);
        StockProduit testStockProduit = stockProduitList.get(stockProduitList.size() - 1);
        assertThat(testStockProduit.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testStockProduit.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testStockProduit.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testStockProduit.getDateSuppression()).isEqualTo(DEFAULT_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void createStockProduitWithExistingId() throws Exception {
        // Create the StockProduit with an existing ID
        stockProduit.setId(1L);

        int databaseSizeBeforeCreate = stockProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockProduitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockProduit)))
            .andExpect(status().isBadRequest());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStockProduits() throws Exception {
        // Initialize the database
        stockProduitRepository.saveAndFlush(stockProduit);

        // Get all the stockProduitList
        restStockProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE.intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dateSuppression").value(hasItem(DEFAULT_DATE_SUPPRESSION.toString())));
    }

    @Test
    @Transactional
    void getStockProduit() throws Exception {
        // Initialize the database
        stockProduitRepository.saveAndFlush(stockProduit);

        // Get the stockProduit
        restStockProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, stockProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stockProduit.getId().intValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE.intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.dateSuppression").value(DEFAULT_DATE_SUPPRESSION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingStockProduit() throws Exception {
        // Get the stockProduit
        restStockProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStockProduit() throws Exception {
        // Initialize the database
        stockProduitRepository.saveAndFlush(stockProduit);

        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();

        // Update the stockProduit
        StockProduit updatedStockProduit = stockProduitRepository.findById(stockProduit.getId()).get();
        // Disconnect from session so that the updates on updatedStockProduit are not directly saved in db
        em.detach(updatedStockProduit);
        updatedStockProduit
            .quantite(UPDATED_QUANTITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restStockProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStockProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStockProduit))
            )
            .andExpect(status().isOk());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
        StockProduit testStockProduit = stockProduitList.get(stockProduitList.size() - 1);
        assertThat(testStockProduit.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testStockProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testStockProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testStockProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void putNonExistingStockProduit() throws Exception {
        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();
        stockProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stockProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStockProduit() throws Exception {
        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();
        stockProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stockProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStockProduit() throws Exception {
        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();
        stockProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockProduitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stockProduit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStockProduitWithPatch() throws Exception {
        // Initialize the database
        stockProduitRepository.saveAndFlush(stockProduit);

        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();

        // Update the stockProduit using partial update
        StockProduit partialUpdatedStockProduit = new StockProduit();
        partialUpdatedStockProduit.setId(stockProduit.getId());

        partialUpdatedStockProduit.quantite(UPDATED_QUANTITE);

        restStockProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockProduit))
            )
            .andExpect(status().isOk());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
        StockProduit testStockProduit = stockProduitList.get(stockProduitList.size() - 1);
        assertThat(testStockProduit.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testStockProduit.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testStockProduit.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testStockProduit.getDateSuppression()).isEqualTo(DEFAULT_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void fullUpdateStockProduitWithPatch() throws Exception {
        // Initialize the database
        stockProduitRepository.saveAndFlush(stockProduit);

        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();

        // Update the stockProduit using partial update
        StockProduit partialUpdatedStockProduit = new StockProduit();
        partialUpdatedStockProduit.setId(stockProduit.getId());

        partialUpdatedStockProduit
            .quantite(UPDATED_QUANTITE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restStockProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStockProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStockProduit))
            )
            .andExpect(status().isOk());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
        StockProduit testStockProduit = stockProduitList.get(stockProduitList.size() - 1);
        assertThat(testStockProduit.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testStockProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testStockProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testStockProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void patchNonExistingStockProduit() throws Exception {
        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();
        stockProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stockProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStockProduit() throws Exception {
        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();
        stockProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stockProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStockProduit() throws Exception {
        int databaseSizeBeforeUpdate = stockProduitRepository.findAll().size();
        stockProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStockProduitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stockProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StockProduit in the database
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStockProduit() throws Exception {
        // Initialize the database
        stockProduitRepository.saveAndFlush(stockProduit);

        int databaseSizeBeforeDelete = stockProduitRepository.findAll().size();

        // Delete the stockProduit
        restStockProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, stockProduit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StockProduit> stockProduitList = stockProduitRepository.findAll();
        assertThat(stockProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
