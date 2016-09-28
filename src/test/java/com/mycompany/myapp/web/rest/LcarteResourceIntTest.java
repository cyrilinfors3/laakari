package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lcarte;
import com.mycompany.myapp.domain.Lpoint;
import com.mycompany.myapp.repository.LcarteRepository;

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
 * Test class for the LcarteResource REST controller.
 *
 * @see LcarteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LcarteResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Inject
    private LcarteRepository lcarteRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLcarteMockMvc;

    private Lcarte lcarte;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LcarteResource lcarteResource = new LcarteResource();
        ReflectionTestUtils.setField(lcarteResource, "lcarteRepository", lcarteRepository);
        this.restLcarteMockMvc = MockMvcBuilders.standaloneSetup(lcarteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lcarte createEntity(EntityManager em) {
        Lcarte lcarte = new Lcarte()
                .code(DEFAULT_CODE);
        // Add required entity
        Lpoint lpoint = LpointResourceIntTest.createEntity(em);
        em.persist(lpoint);
        em.flush();
        lcarte.getLpoints().add(lpoint);
        return lcarte;
    }

    @Before
    public void initTest() {
        lcarte = createEntity(em);
    }

    @Test
    @Transactional
    public void createLcarte() throws Exception {
        int databaseSizeBeforeCreate = lcarteRepository.findAll().size();

        // Create the Lcarte

        restLcarteMockMvc.perform(post("/api/lcartes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcarte)))
                .andExpect(status().isCreated());

        // Validate the Lcarte in the database
        List<Lcarte> lcartes = lcarteRepository.findAll();
        assertThat(lcartes).hasSize(databaseSizeBeforeCreate + 1);
        Lcarte testLcarte = lcartes.get(lcartes.size() - 1);
        assertThat(testLcarte.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void getAllLcartes() throws Exception {
        // Initialize the database
        lcarteRepository.saveAndFlush(lcarte);

        // Get all the lcartes
        restLcarteMockMvc.perform(get("/api/lcartes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lcarte.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getLcarte() throws Exception {
        // Initialize the database
        lcarteRepository.saveAndFlush(lcarte);

        // Get the lcarte
        restLcarteMockMvc.perform(get("/api/lcartes/{id}", lcarte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lcarte.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLcarte() throws Exception {
        // Get the lcarte
        restLcarteMockMvc.perform(get("/api/lcartes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLcarte() throws Exception {
        // Initialize the database
        lcarteRepository.saveAndFlush(lcarte);
        int databaseSizeBeforeUpdate = lcarteRepository.findAll().size();

        // Update the lcarte
        Lcarte updatedLcarte = lcarteRepository.findOne(lcarte.getId());
        updatedLcarte
                .code(UPDATED_CODE);

        restLcarteMockMvc.perform(put("/api/lcartes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLcarte)))
                .andExpect(status().isOk());

        // Validate the Lcarte in the database
        List<Lcarte> lcartes = lcarteRepository.findAll();
        assertThat(lcartes).hasSize(databaseSizeBeforeUpdate);
        Lcarte testLcarte = lcartes.get(lcartes.size() - 1);
        assertThat(testLcarte.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void deleteLcarte() throws Exception {
        // Initialize the database
        lcarteRepository.saveAndFlush(lcarte);
        int databaseSizeBeforeDelete = lcarteRepository.findAll().size();

        // Get the lcarte
        restLcarteMockMvc.perform(delete("/api/lcartes/{id}", lcarte.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lcarte> lcartes = lcarteRepository.findAll();
        assertThat(lcartes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
