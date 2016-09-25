package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Larrondissement;
import com.mycompany.myapp.repository.LarrondissementRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LarrondissementResource REST controller.
 *
 * @see LarrondissementResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LarrondissementResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_VILLE = "AAAAA";
    private static final String UPDATED_VILLE = "BBBBB";

    @Inject
    private LarrondissementRepository larrondissementRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLarrondissementMockMvc;

    private Larrondissement larrondissement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LarrondissementResource larrondissementResource = new LarrondissementResource();
        ReflectionTestUtils.setField(larrondissementResource, "larrondissementRepository", larrondissementRepository);
        this.restLarrondissementMockMvc = MockMvcBuilders.standaloneSetup(larrondissementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Larrondissement createEntity(EntityManager em) {
        Larrondissement larrondissement = new Larrondissement()
                .nom(DEFAULT_NOM)
                .ville(DEFAULT_VILLE);
        return larrondissement;
    }

    @Before
    public void initTest() {
        larrondissement = createEntity(em);
    }

    @Test
    @Transactional
    public void createLarrondissement() throws Exception {
        int databaseSizeBeforeCreate = larrondissementRepository.findAll().size();

        // Create the Larrondissement

        restLarrondissementMockMvc.perform(post("/api/larrondissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(larrondissement)))
                .andExpect(status().isCreated());

        // Validate the Larrondissement in the database
        List<Larrondissement> larrondissements = larrondissementRepository.findAll();
        assertThat(larrondissements).hasSize(databaseSizeBeforeCreate + 1);
        Larrondissement testLarrondissement = larrondissements.get(larrondissements.size() - 1);
        assertThat(testLarrondissement.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLarrondissement.getVille()).isEqualTo(DEFAULT_VILLE);
    }

    @Test
    @Transactional
    public void checkVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = larrondissementRepository.findAll().size();
        // set the field null
        larrondissement.setVille(null);

        // Create the Larrondissement, which fails.

        restLarrondissementMockMvc.perform(post("/api/larrondissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(larrondissement)))
                .andExpect(status().isBadRequest());

        List<Larrondissement> larrondissements = larrondissementRepository.findAll();
        assertThat(larrondissements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLarrondissements() throws Exception {
        // Initialize the database
        larrondissementRepository.saveAndFlush(larrondissement);

        // Get all the larrondissements
        restLarrondissementMockMvc.perform(get("/api/larrondissements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(larrondissement.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())));
    }

    @Test
    @Transactional
    public void getLarrondissement() throws Exception {
        // Initialize the database
        larrondissementRepository.saveAndFlush(larrondissement);

        // Get the larrondissement
        restLarrondissementMockMvc.perform(get("/api/larrondissements/{id}", larrondissement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(larrondissement.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLarrondissement() throws Exception {
        // Get the larrondissement
        restLarrondissementMockMvc.perform(get("/api/larrondissements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLarrondissement() throws Exception {
        // Initialize the database
        larrondissementRepository.saveAndFlush(larrondissement);
        int databaseSizeBeforeUpdate = larrondissementRepository.findAll().size();

        // Update the larrondissement
        Larrondissement updatedLarrondissement = larrondissementRepository.findOne(larrondissement.getId());
        updatedLarrondissement
                .nom(UPDATED_NOM)
                .ville(UPDATED_VILLE);

        restLarrondissementMockMvc.perform(put("/api/larrondissements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLarrondissement)))
                .andExpect(status().isOk());

        // Validate the Larrondissement in the database
        List<Larrondissement> larrondissements = larrondissementRepository.findAll();
        assertThat(larrondissements).hasSize(databaseSizeBeforeUpdate);
        Larrondissement testLarrondissement = larrondissements.get(larrondissements.size() - 1);
        assertThat(testLarrondissement.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLarrondissement.getVille()).isEqualTo(UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void deleteLarrondissement() throws Exception {
        // Initialize the database
        larrondissementRepository.saveAndFlush(larrondissement);
        int databaseSizeBeforeDelete = larrondissementRepository.findAll().size();

        // Get the larrondissement
        restLarrondissementMockMvc.perform(delete("/api/larrondissements/{id}", larrondissement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Larrondissement> larrondissements = larrondissementRepository.findAll();
        assertThat(larrondissements).hasSize(databaseSizeBeforeDelete - 1);
    }
}
