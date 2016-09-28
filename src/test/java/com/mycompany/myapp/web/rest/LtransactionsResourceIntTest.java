package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Ltransactions;
import com.mycompany.myapp.repository.LtransactionsRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LtransactionsResource REST controller.
 *
 * @see LtransactionsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LtransactionsResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_QTE = 1;
    private static final Integer UPDATED_QTE = 2;

    private static final ZonedDateTime DEFAULT_DATET = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATET = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATET_STR = dateTimeFormatter.format(DEFAULT_DATET);
    private static final String DEFAULT_VENDEUR = "AAAAA";
    private static final String UPDATED_VENDEUR = "BBBBB";
    private static final String DEFAULT_ACHETEUR = "AAAAA";
    private static final String UPDATED_ACHETEUR = "BBBBB";

    @Inject
    private LtransactionsRepository ltransactionsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLtransactionsMockMvc;

    private Ltransactions ltransactions;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LtransactionsResource ltransactionsResource = new LtransactionsResource();
        ReflectionTestUtils.setField(ltransactionsResource, "ltransactionsRepository", ltransactionsRepository);
        this.restLtransactionsMockMvc = MockMvcBuilders.standaloneSetup(ltransactionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ltransactions createEntity(EntityManager em) {
        Ltransactions ltransactions = new Ltransactions()
                .qte(DEFAULT_QTE)
                .datet(DEFAULT_DATET)
                .vendeur(DEFAULT_VENDEUR)
                .acheteur(DEFAULT_ACHETEUR);
        return ltransactions;
    }

    @Before
    public void initTest() {
        ltransactions = createEntity(em);
    }

    @Test
    @Transactional
    public void createLtransactions() throws Exception {
        int databaseSizeBeforeCreate = ltransactionsRepository.findAll().size();

        // Create the Ltransactions

        restLtransactionsMockMvc.perform(post("/api/ltransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ltransactions)))
                .andExpect(status().isCreated());

        // Validate the Ltransactions in the database
        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeCreate + 1);
        Ltransactions testLtransactions = ltransactions.get(ltransactions.size() - 1);
        assertThat(testLtransactions.getQte()).isEqualTo(DEFAULT_QTE);
        assertThat(testLtransactions.getDatet()).isEqualTo(DEFAULT_DATET);
        assertThat(testLtransactions.getVendeur()).isEqualTo(DEFAULT_VENDEUR);
        assertThat(testLtransactions.getAcheteur()).isEqualTo(DEFAULT_ACHETEUR);
    }

    @Test
    @Transactional
    public void checkQteIsRequired() throws Exception {
        int databaseSizeBeforeTest = ltransactionsRepository.findAll().size();
        // set the field null
        ltransactions.setQte(null);

        // Create the Ltransactions, which fails.

        restLtransactionsMockMvc.perform(post("/api/ltransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ltransactions)))
                .andExpect(status().isBadRequest());

        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatetIsRequired() throws Exception {
        int databaseSizeBeforeTest = ltransactionsRepository.findAll().size();
        // set the field null
        ltransactions.setDatet(null);

        // Create the Ltransactions, which fails.

        restLtransactionsMockMvc.perform(post("/api/ltransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ltransactions)))
                .andExpect(status().isBadRequest());

        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVendeurIsRequired() throws Exception {
        int databaseSizeBeforeTest = ltransactionsRepository.findAll().size();
        // set the field null
        ltransactions.setVendeur(null);

        // Create the Ltransactions, which fails.

        restLtransactionsMockMvc.perform(post("/api/ltransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ltransactions)))
                .andExpect(status().isBadRequest());

        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAcheteurIsRequired() throws Exception {
        int databaseSizeBeforeTest = ltransactionsRepository.findAll().size();
        // set the field null
        ltransactions.setAcheteur(null);

        // Create the Ltransactions, which fails.

        restLtransactionsMockMvc.perform(post("/api/ltransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ltransactions)))
                .andExpect(status().isBadRequest());

        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLtransactions() throws Exception {
        // Initialize the database
        ltransactionsRepository.saveAndFlush(ltransactions);

        // Get all the ltransactions
        restLtransactionsMockMvc.perform(get("/api/ltransactions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ltransactions.getId().intValue())))
                .andExpect(jsonPath("$.[*].qte").value(hasItem(DEFAULT_QTE)))
                .andExpect(jsonPath("$.[*].datet").value(hasItem(DEFAULT_DATET_STR)))
                .andExpect(jsonPath("$.[*].vendeur").value(hasItem(DEFAULT_VENDEUR.toString())))
                .andExpect(jsonPath("$.[*].acheteur").value(hasItem(DEFAULT_ACHETEUR.toString())));
    }

    @Test
    @Transactional
    public void getLtransactions() throws Exception {
        // Initialize the database
        ltransactionsRepository.saveAndFlush(ltransactions);

        // Get the ltransactions
        restLtransactionsMockMvc.perform(get("/api/ltransactions/{id}", ltransactions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ltransactions.getId().intValue()))
            .andExpect(jsonPath("$.qte").value(DEFAULT_QTE))
            .andExpect(jsonPath("$.datet").value(DEFAULT_DATET_STR))
            .andExpect(jsonPath("$.vendeur").value(DEFAULT_VENDEUR.toString()))
            .andExpect(jsonPath("$.acheteur").value(DEFAULT_ACHETEUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLtransactions() throws Exception {
        // Get the ltransactions
        restLtransactionsMockMvc.perform(get("/api/ltransactions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLtransactions() throws Exception {
        // Initialize the database
        ltransactionsRepository.saveAndFlush(ltransactions);
        int databaseSizeBeforeUpdate = ltransactionsRepository.findAll().size();

        // Update the ltransactions
        Ltransactions updatedLtransactions = ltransactionsRepository.findOne(ltransactions.getId());
        updatedLtransactions
                .qte(UPDATED_QTE)
                .datet(UPDATED_DATET)
                .vendeur(UPDATED_VENDEUR)
                .acheteur(UPDATED_ACHETEUR);

        restLtransactionsMockMvc.perform(put("/api/ltransactions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLtransactions)))
                .andExpect(status().isOk());

        // Validate the Ltransactions in the database
        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeUpdate);
        Ltransactions testLtransactions = ltransactions.get(ltransactions.size() - 1);
        assertThat(testLtransactions.getQte()).isEqualTo(UPDATED_QTE);
        assertThat(testLtransactions.getDatet()).isEqualTo(UPDATED_DATET);
        assertThat(testLtransactions.getVendeur()).isEqualTo(UPDATED_VENDEUR);
        assertThat(testLtransactions.getAcheteur()).isEqualTo(UPDATED_ACHETEUR);
    }

    @Test
    @Transactional
    public void deleteLtransactions() throws Exception {
        // Initialize the database
        ltransactionsRepository.saveAndFlush(ltransactions);
        int databaseSizeBeforeDelete = ltransactionsRepository.findAll().size();

        // Get the ltransactions
        restLtransactionsMockMvc.perform(delete("/api/ltransactions/{id}", ltransactions.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ltransactions> ltransactions = ltransactionsRepository.findAll();
        assertThat(ltransactions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
