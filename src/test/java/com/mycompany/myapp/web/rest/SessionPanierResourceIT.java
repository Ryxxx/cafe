package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SessionPanier;
import com.mycompany.myapp.repository.SessionPanierRepository;
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
 * Integration tests for the {@link SessionPanierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SessionPanierResourceIT {

    private static final Double DEFAULT_TOTAL = 1D;
    private static final Double UPDATED_TOTAL = 2D;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_MODIFICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_MODIFICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/session-paniers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SessionPanierRepository sessionPanierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSessionPanierMockMvc;

    private SessionPanier sessionPanier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionPanier createEntity(EntityManager em) {
        SessionPanier sessionPanier = new SessionPanier()
            .total(DEFAULT_TOTAL)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION);
        return sessionPanier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SessionPanier createUpdatedEntity(EntityManager em) {
        SessionPanier sessionPanier = new SessionPanier()
            .total(UPDATED_TOTAL)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION);
        return sessionPanier;
    }

    @BeforeEach
    public void initTest() {
        sessionPanier = createEntity(em);
    }

    @Test
    @Transactional
    void createSessionPanier() throws Exception {
        int databaseSizeBeforeCreate = sessionPanierRepository.findAll().size();
        // Create the SessionPanier
        restSessionPanierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionPanier)))
            .andExpect(status().isCreated());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeCreate + 1);
        SessionPanier testSessionPanier = sessionPanierList.get(sessionPanierList.size() - 1);
        assertThat(testSessionPanier.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testSessionPanier.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testSessionPanier.getDateModification()).isEqualTo(DEFAULT_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void createSessionPanierWithExistingId() throws Exception {
        // Create the SessionPanier with an existing ID
        sessionPanier.setId(1L);

        int databaseSizeBeforeCreate = sessionPanierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSessionPanierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionPanier)))
            .andExpect(status().isBadRequest());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSessionPaniers() throws Exception {
        // Initialize the database
        sessionPanierRepository.saveAndFlush(sessionPanier);

        // Get all the sessionPanierList
        restSessionPanierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sessionPanier.getId().intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())));
    }

    @Test
    @Transactional
    void getSessionPanier() throws Exception {
        // Initialize the database
        sessionPanierRepository.saveAndFlush(sessionPanier);

        // Get the sessionPanier
        restSessionPanierMockMvc
            .perform(get(ENTITY_API_URL_ID, sessionPanier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sessionPanier.getId().intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSessionPanier() throws Exception {
        // Get the sessionPanier
        restSessionPanierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSessionPanier() throws Exception {
        // Initialize the database
        sessionPanierRepository.saveAndFlush(sessionPanier);

        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();

        // Update the sessionPanier
        SessionPanier updatedSessionPanier = sessionPanierRepository.findById(sessionPanier.getId()).get();
        // Disconnect from session so that the updates on updatedSessionPanier are not directly saved in db
        em.detach(updatedSessionPanier);
        updatedSessionPanier.total(UPDATED_TOTAL).dateCreation(UPDATED_DATE_CREATION).dateModification(UPDATED_DATE_MODIFICATION);

        restSessionPanierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSessionPanier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSessionPanier))
            )
            .andExpect(status().isOk());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
        SessionPanier testSessionPanier = sessionPanierList.get(sessionPanierList.size() - 1);
        assertThat(testSessionPanier.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testSessionPanier.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSessionPanier.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void putNonExistingSessionPanier() throws Exception {
        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();
        sessionPanier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionPanierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sessionPanier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionPanier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSessionPanier() throws Exception {
        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();
        sessionPanier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionPanierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sessionPanier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSessionPanier() throws Exception {
        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();
        sessionPanier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionPanierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sessionPanier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSessionPanierWithPatch() throws Exception {
        // Initialize the database
        sessionPanierRepository.saveAndFlush(sessionPanier);

        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();

        // Update the sessionPanier using partial update
        SessionPanier partialUpdatedSessionPanier = new SessionPanier();
        partialUpdatedSessionPanier.setId(sessionPanier.getId());

        partialUpdatedSessionPanier.dateModification(UPDATED_DATE_MODIFICATION);

        restSessionPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionPanier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionPanier))
            )
            .andExpect(status().isOk());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
        SessionPanier testSessionPanier = sessionPanierList.get(sessionPanierList.size() - 1);
        assertThat(testSessionPanier.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testSessionPanier.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testSessionPanier.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void fullUpdateSessionPanierWithPatch() throws Exception {
        // Initialize the database
        sessionPanierRepository.saveAndFlush(sessionPanier);

        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();

        // Update the sessionPanier using partial update
        SessionPanier partialUpdatedSessionPanier = new SessionPanier();
        partialUpdatedSessionPanier.setId(sessionPanier.getId());

        partialUpdatedSessionPanier.total(UPDATED_TOTAL).dateCreation(UPDATED_DATE_CREATION).dateModification(UPDATED_DATE_MODIFICATION);

        restSessionPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSessionPanier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSessionPanier))
            )
            .andExpect(status().isOk());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
        SessionPanier testSessionPanier = sessionPanierList.get(sessionPanierList.size() - 1);
        assertThat(testSessionPanier.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testSessionPanier.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testSessionPanier.getDateModification()).isEqualTo(UPDATED_DATE_MODIFICATION);
    }

    @Test
    @Transactional
    void patchNonExistingSessionPanier() throws Exception {
        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();
        sessionPanier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSessionPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sessionPanier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionPanier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSessionPanier() throws Exception {
        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();
        sessionPanier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionPanierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sessionPanier))
            )
            .andExpect(status().isBadRequest());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSessionPanier() throws Exception {
        int databaseSizeBeforeUpdate = sessionPanierRepository.findAll().size();
        sessionPanier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSessionPanierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sessionPanier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SessionPanier in the database
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSessionPanier() throws Exception {
        // Initialize the database
        sessionPanierRepository.saveAndFlush(sessionPanier);

        int databaseSizeBeforeDelete = sessionPanierRepository.findAll().size();

        // Delete the sessionPanier
        restSessionPanierMockMvc
            .perform(delete(ENTITY_API_URL_ID, sessionPanier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SessionPanier> sessionPanierList = sessionPanierRepository.findAll();
        assertThat(sessionPanierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
