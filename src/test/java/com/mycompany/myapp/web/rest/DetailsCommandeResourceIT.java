package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DetailsCommande;
import com.mycompany.myapp.repository.DetailsCommandeRepository;
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
 * Integration tests for the {@link DetailsCommandeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailsCommandeResourceIT {

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/details-commandes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailsCommandeRepository detailsCommandeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailsCommandeMockMvc;

    private DetailsCommande detailsCommande;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailsCommande createEntity(EntityManager em) {
        DetailsCommande detailsCommande = new DetailsCommande()
            .total(DEFAULT_TOTAL)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION);
        return detailsCommande;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailsCommande createUpdatedEntity(EntityManager em) {
        DetailsCommande detailsCommande = new DetailsCommande()
            .total(UPDATED_TOTAL)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        return detailsCommande;
    }

    @BeforeEach
    public void initTest() {
        detailsCommande = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailsCommande() throws Exception {
        int databaseSizeBeforeCreate = detailsCommandeRepository.findAll().size();
        // Create the DetailsCommande
        restDetailsCommandeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isCreated());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeCreate + 1);
        DetailsCommande testDetailsCommande = detailsCommandeList.get(detailsCommandeList.size() - 1);
        assertThat(testDetailsCommande.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testDetailsCommande.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testDetailsCommande.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void createDetailsCommandeWithExistingId() throws Exception {
        // Create the DetailsCommande with an existing ID
        detailsCommande.setId(1L);

        int databaseSizeBeforeCreate = detailsCommandeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailsCommandeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetailsCommandes() throws Exception {
        // Initialize the database
        detailsCommandeRepository.saveAndFlush(detailsCommande);

        // Get all the detailsCommandeList
        restDetailsCommandeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailsCommande.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())));
    }

    @Test
    @Transactional
    void getDetailsCommande() throws Exception {
        // Initialize the database
        detailsCommandeRepository.saveAndFlush(detailsCommande);

        // Get the detailsCommande
        restDetailsCommandeMockMvc
            .perform(get(ENTITY_API_URL_ID, detailsCommande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailsCommande.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDetailsCommande() throws Exception {
        // Get the detailsCommande
        restDetailsCommandeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailsCommande() throws Exception {
        // Initialize the database
        detailsCommandeRepository.saveAndFlush(detailsCommande);

        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();

        // Update the detailsCommande
        DetailsCommande updatedDetailsCommande = detailsCommandeRepository.findById(detailsCommande.getId()).get();
        // Disconnect from session so that the updates on updatedDetailsCommande are not directly saved in db
        em.detach(updatedDetailsCommande);
        updatedDetailsCommande.total(UPDATED_TOTAL).dateCreation(UPDATED_DATE_CREATION).dateModification(UPDATED_DATE_MODIFICATION);

        restDetailsCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetailsCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetailsCommande))
            )
            .andExpect(status().isOk());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
        DetailsCommande testDetailsCommande = detailsCommandeList.get(detailsCommandeList.size() - 1);
        assertThat(testDetailsCommande.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDetailsCommande.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testDetailsCommande.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void putNonExistingDetailsCommande() throws Exception {
        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();
        detailsCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailsCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailsCommande.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailsCommande() throws Exception {
        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();
        detailsCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailsCommandeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailsCommande() throws Exception {
        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();
        detailsCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailsCommandeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailsCommandeWithPatch() throws Exception {
        // Initialize the database
        detailsCommandeRepository.saveAndFlush(detailsCommande);

        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();

        // Update the detailsCommande using partial update
        DetailsCommande partialUpdatedDetailsCommande = new DetailsCommande();
        partialUpdatedDetailsCommande.setId(detailsCommande.getId());

        partialUpdatedDetailsCommande.total(UPDATED_TOTAL);

        restDetailsCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailsCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailsCommande))
            )
            .andExpect(status().isOk());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
        DetailsCommande testDetailsCommande = detailsCommandeList.get(detailsCommandeList.size() - 1);
        assertThat(testDetailsCommande.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDetailsCommande.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testDetailsCommande.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void fullUpdateDetailsCommandeWithPatch() throws Exception {
        // Initialize the database
        detailsCommandeRepository.saveAndFlush(detailsCommande);

        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();

        // Update the detailsCommande using partial update
        DetailsCommande partialUpdatedDetailsCommande = new DetailsCommande();
        partialUpdatedDetailsCommande.setId(detailsCommande.getId());

        partialUpdatedDetailsCommande.total(UPDATED_TOTAL).dateCreation(UPDATED_DATE_CREATION).dateModification(UPDATED_DATE_MODIFICATION);

        restDetailsCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailsCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailsCommande))
            )
            .andExpect(status().isOk());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
        DetailsCommande testDetailsCommande = detailsCommandeList.get(detailsCommandeList.size() - 1);
        assertThat(testDetailsCommande.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testDetailsCommande.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testDetailsCommande.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void patchNonExistingDetailsCommande() throws Exception {
        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();
        detailsCommande.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailsCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailsCommande.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailsCommande() throws Exception {
        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();
        detailsCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailsCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailsCommande() throws Exception {
        int databaseSizeBeforeUpdate = detailsCommandeRepository.findAll().size();
        detailsCommande.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailsCommandeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailsCommande))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailsCommande in the database
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailsCommande() throws Exception {
        // Initialize the database
        detailsCommandeRepository.saveAndFlush(detailsCommande);

        int databaseSizeBeforeDelete = detailsCommandeRepository.findAll().size();

        // Delete the detailsCommande
        restDetailsCommandeMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailsCommande.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailsCommande> detailsCommandeList = detailsCommandeRepository.findAll();
        assertThat(detailsCommandeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
