package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lregion;
import com.mycompany.myapp.repository.LregionRepository;

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
 * Test class for the LregionResource REST controller.
 *
 * @see LregionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LregionResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_CAPITAL = "AAAAA";
    private static final String UPDATED_CAPITAL = "BBBBB";

    @Inject
    private LregionRepository lregionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLregionMockMvc;

    private Lregion lregion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LregionResource lregionResource = new LregionResource();
        ReflectionTestUtils.setField(lregionResource, "lregionRepository", lregionRepository);
        this.restLregionMockMvc = MockMvcBuilders.standaloneSetup(lregionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lregion createEntity(EntityManager em) {
        Lregion lregion = new Lregion()
                .nom(DEFAULT_NOM)
                .capital(DEFAULT_CAPITAL);
        return lregion;
    }

    @Before
    public void initTest() {
        lregion = createEntity(em);
    }

    @Test
    @Transactional
    public void createLregion() throws Exception {
        int databaseSizeBeforeCreate = lregionRepository.findAll().size();

        // Create the Lregion

        restLregionMockMvc.perform(post("/api/lregions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lregion)))
                .andExpect(status().isCreated());

        // Validate the Lregion in the database
        List<Lregion> lregions = lregionRepository.findAll();
        assertThat(lregions).hasSize(databaseSizeBeforeCreate + 1);
        Lregion testLregion = lregions.get(lregions.size() - 1);
        assertThat(testLregion.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLregion.getCapital()).isEqualTo(DEFAULT_CAPITAL);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = lregionRepository.findAll().size();
        // set the field null
        lregion.setNom(null);

        // Create the Lregion, which fails.

        restLregionMockMvc.perform(post("/api/lregions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lregion)))
                .andExpect(status().isBadRequest());

        List<Lregion> lregions = lregionRepository.findAll();
        assertThat(lregions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLregions() throws Exception {
        // Initialize the database
        lregionRepository.saveAndFlush(lregion);

        // Get all the lregions
        restLregionMockMvc.perform(get("/api/lregions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lregion.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].capital").value(hasItem(DEFAULT_CAPITAL.toString())));
    }

    @Test
    @Transactional
    public void getLregion() throws Exception {
        // Initialize the database
        lregionRepository.saveAndFlush(lregion);

        // Get the lregion
        restLregionMockMvc.perform(get("/api/lregions/{id}", lregion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lregion.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.capital").value(DEFAULT_CAPITAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLregion() throws Exception {
        // Get the lregion
        restLregionMockMvc.perform(get("/api/lregions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLregion() throws Exception {
        // Initialize the database
        lregionRepository.saveAndFlush(lregion);
        int databaseSizeBeforeUpdate = lregionRepository.findAll().size();

        // Update the lregion
        Lregion updatedLregion = lregionRepository.findOne(lregion.getId());
        updatedLregion
                .nom(UPDATED_NOM)
                .capital(UPDATED_CAPITAL);

        restLregionMockMvc.perform(put("/api/lregions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLregion)))
                .andExpect(status().isOk());

        // Validate the Lregion in the database
        List<Lregion> lregions = lregionRepository.findAll();
        assertThat(lregions).hasSize(databaseSizeBeforeUpdate);
        Lregion testLregion = lregions.get(lregions.size() - 1);
        assertThat(testLregion.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLregion.getCapital()).isEqualTo(UPDATED_CAPITAL);
    }

    @Test
    @Transactional
    public void deleteLregion() throws Exception {
        // Initialize the database
        lregionRepository.saveAndFlush(lregion);
        int databaseSizeBeforeDelete = lregionRepository.findAll().size();

        // Get the lregion
        restLregionMockMvc.perform(delete("/api/lregions/{id}", lregion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lregion> lregions = lregionRepository.findAll();
        assertThat(lregions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
