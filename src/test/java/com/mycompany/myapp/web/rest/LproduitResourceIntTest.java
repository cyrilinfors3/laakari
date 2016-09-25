package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lproduit;
import com.mycompany.myapp.repository.LproduitRepository;

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
 * Test class for the LproduitResource REST controller.
 *
 * @see LproduitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LproduitResourceIntTest {

    private static final String DEFAULT_CODEP = "AAAAA";
    private static final String UPDATED_CODEP = "BBBBB";
    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    private static final Float DEFAULT_PRIX = 1F;
    private static final Float UPDATED_PRIX = 2F;

    @Inject
    private LproduitRepository lproduitRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLproduitMockMvc;

    private Lproduit lproduit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LproduitResource lproduitResource = new LproduitResource();
        ReflectionTestUtils.setField(lproduitResource, "lproduitRepository", lproduitRepository);
        this.restLproduitMockMvc = MockMvcBuilders.standaloneSetup(lproduitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lproduit createEntity(EntityManager em) {
        Lproduit lproduit = new Lproduit()
                .codep(DEFAULT_CODEP)
                .libelle(DEFAULT_LIBELLE)
                .prix(DEFAULT_PRIX);
        return lproduit;
    }

    @Before
    public void initTest() {
        lproduit = createEntity(em);
    }

    @Test
    @Transactional
    public void createLproduit() throws Exception {
        int databaseSizeBeforeCreate = lproduitRepository.findAll().size();

        // Create the Lproduit

        restLproduitMockMvc.perform(post("/api/lproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lproduit)))
                .andExpect(status().isCreated());

        // Validate the Lproduit in the database
        List<Lproduit> lproduits = lproduitRepository.findAll();
        assertThat(lproduits).hasSize(databaseSizeBeforeCreate + 1);
        Lproduit testLproduit = lproduits.get(lproduits.size() - 1);
        assertThat(testLproduit.getCodep()).isEqualTo(DEFAULT_CODEP);
        assertThat(testLproduit.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testLproduit.getPrix()).isEqualTo(DEFAULT_PRIX);
    }

    @Test
    @Transactional
    public void checkCodepIsRequired() throws Exception {
        int databaseSizeBeforeTest = lproduitRepository.findAll().size();
        // set the field null
        lproduit.setCodep(null);

        // Create the Lproduit, which fails.

        restLproduitMockMvc.perform(post("/api/lproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lproduit)))
                .andExpect(status().isBadRequest());

        List<Lproduit> lproduits = lproduitRepository.findAll();
        assertThat(lproduits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lproduitRepository.findAll().size();
        // set the field null
        lproduit.setLibelle(null);

        // Create the Lproduit, which fails.

        restLproduitMockMvc.perform(post("/api/lproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lproduit)))
                .andExpect(status().isBadRequest());

        List<Lproduit> lproduits = lproduitRepository.findAll();
        assertThat(lproduits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixIsRequired() throws Exception {
        int databaseSizeBeforeTest = lproduitRepository.findAll().size();
        // set the field null
        lproduit.setPrix(null);

        // Create the Lproduit, which fails.

        restLproduitMockMvc.perform(post("/api/lproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lproduit)))
                .andExpect(status().isBadRequest());

        List<Lproduit> lproduits = lproduitRepository.findAll();
        assertThat(lproduits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLproduits() throws Exception {
        // Initialize the database
        lproduitRepository.saveAndFlush(lproduit);

        // Get all the lproduits
        restLproduitMockMvc.perform(get("/api/lproduits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lproduit.getId().intValue())))
                .andExpect(jsonPath("$.[*].codep").value(hasItem(DEFAULT_CODEP.toString())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @Test
    @Transactional
    public void getLproduit() throws Exception {
        // Initialize the database
        lproduitRepository.saveAndFlush(lproduit);

        // Get the lproduit
        restLproduitMockMvc.perform(get("/api/lproduits/{id}", lproduit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lproduit.getId().intValue()))
            .andExpect(jsonPath("$.codep").value(DEFAULT_CODEP.toString()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLproduit() throws Exception {
        // Get the lproduit
        restLproduitMockMvc.perform(get("/api/lproduits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLproduit() throws Exception {
        // Initialize the database
        lproduitRepository.saveAndFlush(lproduit);
        int databaseSizeBeforeUpdate = lproduitRepository.findAll().size();

        // Update the lproduit
        Lproduit updatedLproduit = lproduitRepository.findOne(lproduit.getId());
        updatedLproduit
                .codep(UPDATED_CODEP)
                .libelle(UPDATED_LIBELLE)
                .prix(UPDATED_PRIX);

        restLproduitMockMvc.perform(put("/api/lproduits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLproduit)))
                .andExpect(status().isOk());

        // Validate the Lproduit in the database
        List<Lproduit> lproduits = lproduitRepository.findAll();
        assertThat(lproduits).hasSize(databaseSizeBeforeUpdate);
        Lproduit testLproduit = lproduits.get(lproduits.size() - 1);
        assertThat(testLproduit.getCodep()).isEqualTo(UPDATED_CODEP);
        assertThat(testLproduit.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testLproduit.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    public void deleteLproduit() throws Exception {
        // Initialize the database
        lproduitRepository.saveAndFlush(lproduit);
        int databaseSizeBeforeDelete = lproduitRepository.findAll().size();

        // Get the lproduit
        restLproduitMockMvc.perform(delete("/api/lproduits/{id}", lproduit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lproduit> lproduits = lproduitRepository.findAll();
        assertThat(lproduits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
