package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lroute;
import com.mycompany.myapp.repository.LrouteRepository;

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
 * Test class for the LrouteResource REST controller.
 *
 * @see LrouteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LrouteResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_QUATIER = "AAAAA";
    private static final String UPDATED_QUATIER = "BBBBB";
    private static final String DEFAULT_VILLE = "AAAAA";
    private static final String UPDATED_VILLE = "BBBBB";

    @Inject
    private LrouteRepository lrouteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLrouteMockMvc;

    private Lroute lroute;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LrouteResource lrouteResource = new LrouteResource();
        ReflectionTestUtils.setField(lrouteResource, "lrouteRepository", lrouteRepository);
        this.restLrouteMockMvc = MockMvcBuilders.standaloneSetup(lrouteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lroute createEntity(EntityManager em) {
        Lroute lroute = new Lroute()
                .nom(DEFAULT_NOM)
                .quatier(DEFAULT_QUATIER)
                .ville(DEFAULT_VILLE);
        return lroute;
    }

    @Before
    public void initTest() {
        lroute = createEntity(em);
    }

    @Test
    @Transactional
    public void createLroute() throws Exception {
        int databaseSizeBeforeCreate = lrouteRepository.findAll().size();

        // Create the Lroute

        restLrouteMockMvc.perform(post("/api/lroutes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lroute)))
                .andExpect(status().isCreated());

        // Validate the Lroute in the database
        List<Lroute> lroutes = lrouteRepository.findAll();
        assertThat(lroutes).hasSize(databaseSizeBeforeCreate + 1);
        Lroute testLroute = lroutes.get(lroutes.size() - 1);
        assertThat(testLroute.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLroute.getQuatier()).isEqualTo(DEFAULT_QUATIER);
        assertThat(testLroute.getVille()).isEqualTo(DEFAULT_VILLE);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = lrouteRepository.findAll().size();
        // set the field null
        lroute.setNom(null);

        // Create the Lroute, which fails.

        restLrouteMockMvc.perform(post("/api/lroutes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lroute)))
                .andExpect(status().isBadRequest());

        List<Lroute> lroutes = lrouteRepository.findAll();
        assertThat(lroutes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLroutes() throws Exception {
        // Initialize the database
        lrouteRepository.saveAndFlush(lroute);

        // Get all the lroutes
        restLrouteMockMvc.perform(get("/api/lroutes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lroute.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].quatier").value(hasItem(DEFAULT_QUATIER.toString())))
                .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())));
    }

    @Test
    @Transactional
    public void getLroute() throws Exception {
        // Initialize the database
        lrouteRepository.saveAndFlush(lroute);

        // Get the lroute
        restLrouteMockMvc.perform(get("/api/lroutes/{id}", lroute.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lroute.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.quatier").value(DEFAULT_QUATIER.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLroute() throws Exception {
        // Get the lroute
        restLrouteMockMvc.perform(get("/api/lroutes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLroute() throws Exception {
        // Initialize the database
        lrouteRepository.saveAndFlush(lroute);
        int databaseSizeBeforeUpdate = lrouteRepository.findAll().size();

        // Update the lroute
        Lroute updatedLroute = lrouteRepository.findOne(lroute.getId());
        updatedLroute
                .nom(UPDATED_NOM)
                .quatier(UPDATED_QUATIER)
                .ville(UPDATED_VILLE);

        restLrouteMockMvc.perform(put("/api/lroutes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLroute)))
                .andExpect(status().isOk());

        // Validate the Lroute in the database
        List<Lroute> lroutes = lrouteRepository.findAll();
        assertThat(lroutes).hasSize(databaseSizeBeforeUpdate);
        Lroute testLroute = lroutes.get(lroutes.size() - 1);
        assertThat(testLroute.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLroute.getQuatier()).isEqualTo(UPDATED_QUATIER);
        assertThat(testLroute.getVille()).isEqualTo(UPDATED_VILLE);
    }

    @Test
    @Transactional
    public void deleteLroute() throws Exception {
        // Initialize the database
        lrouteRepository.saveAndFlush(lroute);
        int databaseSizeBeforeDelete = lrouteRepository.findAll().size();

        // Get the lroute
        restLrouteMockMvc.perform(delete("/api/lroutes/{id}", lroute.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lroute> lroutes = lrouteRepository.findAll();
        assertThat(lroutes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
