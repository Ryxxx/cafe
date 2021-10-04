package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ReductionProduit;
import com.mycompany.myapp.repository.ReductionProduitRepository;
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
 * Integration tests for the {@link ReductionProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReductionProduitResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_POURCENTAGE_REDUCTION = 1D;
    private static final Double UPDATED_POURCENTAGE_REDUCTION = 2D;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_SUPPRESSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SUPPRESSION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/reduction-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReductionProduitRepository reductionProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReductionProduitMockMvc;

    private ReductionProduit reductionProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReductionProduit createEntity(EntityManager em) {
        ReductionProduit reductionProduit = new ReductionProduit()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .pourcentageReduction(DEFAULT_POURCENTAGE_REDUCTION)
            .actif(DEFAULT_ACTIF)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateSuppression(DEFAULT_DATE_SUPPRESSION);
        return reductionProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReductionProduit createUpdatedEntity(EntityManager em) {
        ReductionProduit reductionProduit = new ReductionProduit()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .pourcentageReduction(UPDATED_POURCENTAGE_REDUCTION)
            .actif(UPDATED_ACTIF)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);
        return reductionProduit;
    }

    @BeforeEach
    public void initTest() {
        reductionProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createReductionProduit() throws Exception {
        int databaseSizeBeforeCreate = reductionProduitRepository.findAll().size();
        // Create the ReductionProduit
        restReductionProduitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isCreated());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeCreate + 1);
        ReductionProduit testReductionProduit = reductionProduitList.get(reductionProduitList.size() - 1);
        assertThat(testReductionProduit.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testReductionProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testReductionProduit.getPourcentageReduction()).isEqualTo(DEFAULT_POURCENTAGE_REDUCTION);
        assertThat(testReductionProduit.getActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testReductionProduit.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testReductionProduit.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testReductionProduit.getDateSuppression()).isEqualTo(DEFAULT_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void createReductionProduitWithExistingId() throws Exception {
        // Create the ReductionProduit with an existing ID
        reductionProduit.setId(1L);

        int databaseSizeBeforeCreate = reductionProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReductionProduitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReductionProduits() throws Exception {
        // Initialize the database
        reductionProduitRepository.saveAndFlush(reductionProduit);

        // Get all the reductionProduitList
        restReductionProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reductionProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pourcentageReduction").value(hasItem(DEFAULT_POURCENTAGE_REDUCTION.doubleValue())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dateSuppression").value(hasItem(DEFAULT_DATE_SUPPRESSION.toString())));
    }

    @Test
    @Transactional
    void getReductionProduit() throws Exception {
        // Initialize the database
        reductionProduitRepository.saveAndFlush(reductionProduit);

        // Get the reductionProduit
        restReductionProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, reductionProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reductionProduit.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.pourcentageReduction").value(DEFAULT_POURCENTAGE_REDUCTION.doubleValue()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF.booleanValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.dateSuppression").value(DEFAULT_DATE_SUPPRESSION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReductionProduit() throws Exception {
        // Get the reductionProduit
        restReductionProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReductionProduit() throws Exception {
        // Initialize the database
        reductionProduitRepository.saveAndFlush(reductionProduit);

        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();

        // Update the reductionProduit
        ReductionProduit updatedReductionProduit = reductionProduitRepository.findById(reductionProduit.getId()).get();
        // Disconnect from session so that the updates on updatedReductionProduit are not directly saved in db
        em.detach(updatedReductionProduit);
        updatedReductionProduit
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .pourcentageReduction(UPDATED_POURCENTAGE_REDUCTION)
            .actif(UPDATED_ACTIF)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restReductionProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReductionProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReductionProduit))
            )
            .andExpect(status().isOk());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
        ReductionProduit testReductionProduit = reductionProduitList.get(reductionProduitList.size() - 1);
        assertThat(testReductionProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testReductionProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReductionProduit.getPourcentageReduction()).isEqualTo(UPDATED_POURCENTAGE_REDUCTION);
        assertThat(testReductionProduit.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testReductionProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testReductionProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testReductionProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void putNonExistingReductionProduit() throws Exception {
        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();
        reductionProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReductionProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reductionProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReductionProduit() throws Exception {
        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();
        reductionProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReductionProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReductionProduit() throws Exception {
        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();
        reductionProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReductionProduitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReductionProduitWithPatch() throws Exception {
        // Initialize the database
        reductionProduitRepository.saveAndFlush(reductionProduit);

        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();

        // Update the reductionProduit using partial update
        ReductionProduit partialUpdatedReductionProduit = new ReductionProduit();
        partialUpdatedReductionProduit.setId(reductionProduit.getId());

        partialUpdatedReductionProduit.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).dateModification(UPDATED_DATE_MODIFICATION);

        restReductionProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReductionProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReductionProduit))
            )
            .andExpect(status().isOk());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
        ReductionProduit testReductionProduit = reductionProduitList.get(reductionProduitList.size() - 1);
        assertThat(testReductionProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testReductionProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReductionProduit.getPourcentageReduction()).isEqualTo(DEFAULT_POURCENTAGE_REDUCTION);
        assertThat(testReductionProduit.getActif()).isEqualTo(DEFAULT_ACTIF);
        assertThat(testReductionProduit.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testReductionProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testReductionProduit.getDateSuppression()).isEqualTo(DEFAULT_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void fullUpdateReductionProduitWithPatch() throws Exception {
        // Initialize the database
        reductionProduitRepository.saveAndFlush(reductionProduit);

        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();

        // Update the reductionProduit using partial update
        ReductionProduit partialUpdatedReductionProduit = new ReductionProduit();
        partialUpdatedReductionProduit.setId(reductionProduit.getId());

        partialUpdatedReductionProduit
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .pourcentageReduction(UPDATED_POURCENTAGE_REDUCTION)
            .actif(UPDATED_ACTIF)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restReductionProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReductionProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReductionProduit))
            )
            .andExpect(status().isOk());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
        ReductionProduit testReductionProduit = reductionProduitList.get(reductionProduitList.size() - 1);
        assertThat(testReductionProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testReductionProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testReductionProduit.getPourcentageReduction()).isEqualTo(UPDATED_POURCENTAGE_REDUCTION);
        assertThat(testReductionProduit.getActif()).isEqualTo(UPDATED_ACTIF);
        assertThat(testReductionProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testReductionProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testReductionProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void patchNonExistingReductionProduit() throws Exception {
        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();
        reductionProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReductionProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reductionProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReductionProduit() throws Exception {
        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();
        reductionProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReductionProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReductionProduit() throws Exception {
        int databaseSizeBeforeUpdate = reductionProduitRepository.findAll().size();
        reductionProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReductionProduitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reductionProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReductionProduit in the database
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReductionProduit() throws Exception {
        // Initialize the database
        reductionProduitRepository.saveAndFlush(reductionProduit);

        int databaseSizeBeforeDelete = reductionProduitRepository.findAll().size();

        // Delete the reductionProduit
        restReductionProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, reductionProduit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReductionProduit> reductionProduitList = reductionProduitRepository.findAll();
        assertThat(reductionProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
