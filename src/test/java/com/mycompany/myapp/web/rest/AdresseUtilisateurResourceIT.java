package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AdresseUtilisateur;
import com.mycompany.myapp.repository.AdresseUtilisateurRepository;
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
 * Integration tests for the {@link AdresseUtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdresseUtilisateurResourceIT {

    private static final String DEFAULT_ADRESSE_LIGNE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_LIGNE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE_LIGNE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE_LIGNE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODE_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_PAYS = "AAAAAAAAAA";
    private static final String UPDATED_PAYS = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/adresse-utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdresseUtilisateurRepository adresseUtilisateurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdresseUtilisateurMockMvc;

    private AdresseUtilisateur adresseUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdresseUtilisateur createEntity(EntityManager em) {
        AdresseUtilisateur adresseUtilisateur = new AdresseUtilisateur()
            .adresseLigne1(DEFAULT_ADRESSE_LIGNE_1)
            .adresseLigne2(DEFAULT_ADRESSE_LIGNE_2)
            .ville(DEFAULT_VILLE)
            .codePostal(DEFAULT_CODE_POSTAL)
            .pays(DEFAULT_PAYS)
            .telephone(DEFAULT_TELEPHONE);
        return adresseUtilisateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdresseUtilisateur createUpdatedEntity(EntityManager em) {
        AdresseUtilisateur adresseUtilisateur = new AdresseUtilisateur()
            .adresseLigne1(UPDATED_ADRESSE_LIGNE_1)
            .adresseLigne2(UPDATED_ADRESSE_LIGNE_2)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE);
        return adresseUtilisateur;
    }

    @BeforeEach
    public void initTest() {
        adresseUtilisateur = createEntity(em);
    }

    @Test
    @Transactional
    void createAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = adresseUtilisateurRepository.findAll().size();
        // Create the AdresseUtilisateur
        restAdresseUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isCreated());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeCreate + 1);
        AdresseUtilisateur testAdresseUtilisateur = adresseUtilisateurList.get(adresseUtilisateurList.size() - 1);
        assertThat(testAdresseUtilisateur.getAdresseLigne1()).isEqualTo(DEFAULT_ADRESSE_LIGNE_1);
        assertThat(testAdresseUtilisateur.getAdresseLigne2()).isEqualTo(DEFAULT_ADRESSE_LIGNE_2);
        assertThat(testAdresseUtilisateur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testAdresseUtilisateur.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testAdresseUtilisateur.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testAdresseUtilisateur.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void createAdresseUtilisateurWithExistingId() throws Exception {
        // Create the AdresseUtilisateur with an existing ID
        adresseUtilisateur.setId(1L);

        int databaseSizeBeforeCreate = adresseUtilisateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdresseUtilisateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdresseUtilisateurs() throws Exception {
        // Initialize the database
        adresseUtilisateurRepository.saveAndFlush(adresseUtilisateur);

        // Get all the adresseUtilisateurList
        restAdresseUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adresseUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].adresseLigne1").value(hasItem(DEFAULT_ADRESSE_LIGNE_1)))
            .andExpect(jsonPath("$.[*].adresseLigne2").value(hasItem(DEFAULT_ADRESSE_LIGNE_2)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].codePostal").value(hasItem(DEFAULT_CODE_POSTAL)))
            .andExpect(jsonPath("$.[*].pays").value(hasItem(DEFAULT_PAYS)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    void getAdresseUtilisateur() throws Exception {
        // Initialize the database
        adresseUtilisateurRepository.saveAndFlush(adresseUtilisateur);

        // Get the adresseUtilisateur
        restAdresseUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, adresseUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adresseUtilisateur.getId().intValue()))
            .andExpect(jsonPath("$.adresseLigne1").value(DEFAULT_ADRESSE_LIGNE_1))
            .andExpect(jsonPath("$.adresseLigne2").value(DEFAULT_ADRESSE_LIGNE_2))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.codePostal").value(DEFAULT_CODE_POSTAL))
            .andExpect(jsonPath("$.pays").value(DEFAULT_PAYS))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE));
    }

    @Test
    @Transactional
    void getNonExistingAdresseUtilisateur() throws Exception {
        // Get the adresseUtilisateur
        restAdresseUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdresseUtilisateur() throws Exception {
        // Initialize the database
        adresseUtilisateurRepository.saveAndFlush(adresseUtilisateur);

        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();

        // Update the adresseUtilisateur
        AdresseUtilisateur updatedAdresseUtilisateur = adresseUtilisateurRepository.findById(adresseUtilisateur.getId()).get();
        // Disconnect from session so that the updates on updatedAdresseUtilisateur are not directly saved in db
        em.detach(updatedAdresseUtilisateur);
        updatedAdresseUtilisateur
            .adresseLigne1(UPDATED_ADRESSE_LIGNE_1)
            .adresseLigne2(UPDATED_ADRESSE_LIGNE_2)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE);

        restAdresseUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdresseUtilisateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdresseUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
        AdresseUtilisateur testAdresseUtilisateur = adresseUtilisateurList.get(adresseUtilisateurList.size() - 1);
        assertThat(testAdresseUtilisateur.getAdresseLigne1()).isEqualTo(UPDATED_ADRESSE_LIGNE_1);
        assertThat(testAdresseUtilisateur.getAdresseLigne2()).isEqualTo(UPDATED_ADRESSE_LIGNE_2);
        assertThat(testAdresseUtilisateur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAdresseUtilisateur.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testAdresseUtilisateur.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testAdresseUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void putNonExistingAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();
        adresseUtilisateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresseUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adresseUtilisateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();
        adresseUtilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();
        adresseUtilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdresseUtilisateurWithPatch() throws Exception {
        // Initialize the database
        adresseUtilisateurRepository.saveAndFlush(adresseUtilisateur);

        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();

        // Update the adresseUtilisateur using partial update
        AdresseUtilisateur partialUpdatedAdresseUtilisateur = new AdresseUtilisateur();
        partialUpdatedAdresseUtilisateur.setId(adresseUtilisateur.getId());

        partialUpdatedAdresseUtilisateur
            .adresseLigne1(UPDATED_ADRESSE_LIGNE_1)
            .adresseLigne2(UPDATED_ADRESSE_LIGNE_2)
            .ville(UPDATED_VILLE)
            .telephone(UPDATED_TELEPHONE);

        restAdresseUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdresseUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdresseUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
        AdresseUtilisateur testAdresseUtilisateur = adresseUtilisateurList.get(adresseUtilisateurList.size() - 1);
        assertThat(testAdresseUtilisateur.getAdresseLigne1()).isEqualTo(UPDATED_ADRESSE_LIGNE_1);
        assertThat(testAdresseUtilisateur.getAdresseLigne2()).isEqualTo(UPDATED_ADRESSE_LIGNE_2);
        assertThat(testAdresseUtilisateur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAdresseUtilisateur.getCodePostal()).isEqualTo(DEFAULT_CODE_POSTAL);
        assertThat(testAdresseUtilisateur.getPays()).isEqualTo(DEFAULT_PAYS);
        assertThat(testAdresseUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void fullUpdateAdresseUtilisateurWithPatch() throws Exception {
        // Initialize the database
        adresseUtilisateurRepository.saveAndFlush(adresseUtilisateur);

        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();

        // Update the adresseUtilisateur using partial update
        AdresseUtilisateur partialUpdatedAdresseUtilisateur = new AdresseUtilisateur();
        partialUpdatedAdresseUtilisateur.setId(adresseUtilisateur.getId());

        partialUpdatedAdresseUtilisateur
            .adresseLigne1(UPDATED_ADRESSE_LIGNE_1)
            .adresseLigne2(UPDATED_ADRESSE_LIGNE_2)
            .ville(UPDATED_VILLE)
            .codePostal(UPDATED_CODE_POSTAL)
            .pays(UPDATED_PAYS)
            .telephone(UPDATED_TELEPHONE);

        restAdresseUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdresseUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdresseUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
        AdresseUtilisateur testAdresseUtilisateur = adresseUtilisateurList.get(adresseUtilisateurList.size() - 1);
        assertThat(testAdresseUtilisateur.getAdresseLigne1()).isEqualTo(UPDATED_ADRESSE_LIGNE_1);
        assertThat(testAdresseUtilisateur.getAdresseLigne2()).isEqualTo(UPDATED_ADRESSE_LIGNE_2);
        assertThat(testAdresseUtilisateur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testAdresseUtilisateur.getCodePostal()).isEqualTo(UPDATED_CODE_POSTAL);
        assertThat(testAdresseUtilisateur.getPays()).isEqualTo(UPDATED_PAYS);
        assertThat(testAdresseUtilisateur.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void patchNonExistingAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();
        adresseUtilisateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdresseUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adresseUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();
        adresseUtilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdresseUtilisateur() throws Exception {
        int databaseSizeBeforeUpdate = adresseUtilisateurRepository.findAll().size();
        adresseUtilisateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdresseUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adresseUtilisateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdresseUtilisateur in the database
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdresseUtilisateur() throws Exception {
        // Initialize the database
        adresseUtilisateurRepository.saveAndFlush(adresseUtilisateur);

        int databaseSizeBeforeDelete = adresseUtilisateurRepository.findAll().size();

        // Delete the adresseUtilisateur
        restAdresseUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, adresseUtilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdresseUtilisateur> adresseUtilisateurList = adresseUtilisateurRepository.findAll();
        assertThat(adresseUtilisateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
