package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CategorieProduit;
import com.mycompany.myapp.repository.CategorieProduitRepository;
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
 * Integration tests for the {@link CategorieProduitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategorieProduitResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_SUPPRESSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_SUPPRESSION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/categorie-produits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategorieProduitRepository categorieProduitRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieProduitMockMvc;

    private CategorieProduit categorieProduit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieProduit createEntity(EntityManager em) {
        CategorieProduit categorieProduit = new CategorieProduit()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dateSuppression(DEFAULT_DATE_SUPPRESSION);
        return categorieProduit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieProduit createUpdatedEntity(EntityManager em) {
        CategorieProduit categorieProduit = new CategorieProduit()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);
        return categorieProduit;
    }

    @BeforeEach
    public void initTest() {
        categorieProduit = createEntity(em);
    }

    @Test
    @Transactional
    void createCategorieProduit() throws Exception {
        int databaseSizeBeforeCreate = categorieProduitRepository.findAll().size();
        // Create the CategorieProduit
        restCategorieProduitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isCreated());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeCreate + 1);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCategorieProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategorieProduit.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testCategorieProduit.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
        assertThat(testCategorieProduit.getDateSuppression()).isEqualTo(DEFAULT_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void createCategorieProduitWithExistingId() throws Exception {
        // Create the CategorieProduit with an existing ID
        categorieProduit.setId(1L);

        int databaseSizeBeforeCreate = categorieProduitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieProduitMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCategorieProduits() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get all the categorieProduitList
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieProduit.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dateSuppression").value(hasItem(DEFAULT_DATE_SUPPRESSION.toString())));
    }

    @Test
    @Transactional
    void getCategorieProduit() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        // Get the categorieProduit
        restCategorieProduitMockMvc
            .perform(get(ENTITY_API_URL_ID, categorieProduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorieProduit.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.dateSuppression").value(DEFAULT_DATE_SUPPRESSION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCategorieProduit() throws Exception {
        // Get the categorieProduit
        restCategorieProduitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCategorieProduit() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();

        // Update the categorieProduit
        CategorieProduit updatedCategorieProduit = categorieProduitRepository.findById(categorieProduit.getId()).get();
        // Disconnect from session so that the updates on updatedCategorieProduit are not directly saved in db
        em.detach(updatedCategorieProduit);
        updatedCategorieProduit
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategorieProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategorieProduit))
            )
            .andExpect(status().isOk());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCategorieProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategorieProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testCategorieProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testCategorieProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void putNonExistingCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieProduit.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategorieProduitWithPatch() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();

        // Update the categorieProduit using partial update
        CategorieProduit partialUpdatedCategorieProduit = new CategorieProduit();
        partialUpdatedCategorieProduit.setId(categorieProduit.getId());

        partialUpdatedCategorieProduit
            .nom(UPDATED_NOM)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieProduit))
            )
            .andExpect(status().isOk());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCategorieProduit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCategorieProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testCategorieProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testCategorieProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void fullUpdateCategorieProduitWithPatch() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();

        // Update the categorieProduit using partial update
        CategorieProduit partialUpdatedCategorieProduit = new CategorieProduit();
        partialUpdatedCategorieProduit.setId(categorieProduit.getId());

        partialUpdatedCategorieProduit
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dateSuppression(UPDATED_DATE_SUPPRESSION);

        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategorieProduit))
            )
            .andExpect(status().isOk());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
        CategorieProduit testCategorieProduit = categorieProduitList.get(categorieProduitList.size() - 1);
        assertThat(testCategorieProduit.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCategorieProduit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCategorieProduit.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testCategorieProduit.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
        assertThat(testCategorieProduit.getDateSuppression()).isEqualTo(UPDATED_DATE_SUPPRESSION);
    }

    @Test
    @Transactional
    void patchNonExistingCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorieProduit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorieProduit() throws Exception {
        int databaseSizeBeforeUpdate = categorieProduitRepository.findAll().size();
        categorieProduit.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieProduitMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categorieProduit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieProduit in the database
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategorieProduit() throws Exception {
        // Initialize the database
        categorieProduitRepository.saveAndFlush(categorieProduit);

        int databaseSizeBeforeDelete = categorieProduitRepository.findAll().size();

        // Delete the categorieProduit
        restCategorieProduitMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorieProduit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategorieProduit> categorieProduitList = categorieProduitRepository.findAll();
        assertThat(categorieProduitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
