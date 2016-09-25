package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lville;
import com.mycompany.myapp.repository.LvilleRepository;

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
 * Test class for the LvilleResource REST controller.
 *
 * @see LvilleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LvilleResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_REGION = "AAAAA";
    private static final String UPDATED_REGION = "BBBBB";

    @Inject
    private LvilleRepository lvilleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLvilleMockMvc;

    private Lville lville;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LvilleResource lvilleResource = new LvilleResource();
        ReflectionTestUtils.setField(lvilleResource, "lvilleRepository", lvilleRepository);
        this.restLvilleMockMvc = MockMvcBuilders.standaloneSetup(lvilleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lville createEntity(EntityManager em) {
        Lville lville = new Lville()
                .nom(DEFAULT_NOM)
                .region(DEFAULT_REGION);
        return lville;
    }

    @Before
    public void initTest() {
        lville = createEntity(em);
    }

    @Test
    @Transactional
    public void createLville() throws Exception {
        int databaseSizeBeforeCreate = lvilleRepository.findAll().size();

        // Create the Lville

        restLvilleMockMvc.perform(post("/api/lvilles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lville)))
                .andExpect(status().isCreated());

        // Validate the Lville in the database
        List<Lville> lvilles = lvilleRepository.findAll();
        assertThat(lvilles).hasSize(databaseSizeBeforeCreate + 1);
        Lville testLville = lvilles.get(lvilles.size() - 1);
        assertThat(testLville.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLville.getRegion()).isEqualTo(DEFAULT_REGION);
    }

    @Test
    @Transactional
    public void checkRegionIsRequired() throws Exception {
        int databaseSizeBeforeTest = lvilleRepository.findAll().size();
        // set the field null
        lville.setRegion(null);

        // Create the Lville, which fails.

        restLvilleMockMvc.perform(post("/api/lvilles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lville)))
                .andExpect(status().isBadRequest());

        List<Lville> lvilles = lvilleRepository.findAll();
        assertThat(lvilles).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLvilles() throws Exception {
        // Initialize the database
        lvilleRepository.saveAndFlush(lville);

        // Get all the lvilles
        restLvilleMockMvc.perform(get("/api/lvilles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lville.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].region").value(hasItem(DEFAULT_REGION.toString())));
    }

    @Test
    @Transactional
    public void getLville() throws Exception {
        // Initialize the database
        lvilleRepository.saveAndFlush(lville);

        // Get the lville
        restLvilleMockMvc.perform(get("/api/lvilles/{id}", lville.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lville.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.region").value(DEFAULT_REGION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLville() throws Exception {
        // Get the lville
        restLvilleMockMvc.perform(get("/api/lvilles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLville() throws Exception {
        // Initialize the database
        lvilleRepository.saveAndFlush(lville);
        int databaseSizeBeforeUpdate = lvilleRepository.findAll().size();

        // Update the lville
        Lville updatedLville = lvilleRepository.findOne(lville.getId());
        updatedLville
                .nom(UPDATED_NOM)
                .region(UPDATED_REGION);

        restLvilleMockMvc.perform(put("/api/lvilles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLville)))
                .andExpect(status().isOk());

        // Validate the Lville in the database
        List<Lville> lvilles = lvilleRepository.findAll();
        assertThat(lvilles).hasSize(databaseSizeBeforeUpdate);
        Lville testLville = lvilles.get(lvilles.size() - 1);
        assertThat(testLville.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLville.getRegion()).isEqualTo(UPDATED_REGION);
    }

    @Test
    @Transactional
    public void deleteLville() throws Exception {
        // Initialize the database
        lvilleRepository.saveAndFlush(lville);
        int databaseSizeBeforeDelete = lvilleRepository.findAll().size();

        // Get the lville
        restLvilleMockMvc.perform(delete("/api/lvilles/{id}", lville.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lville> lvilles = lvilleRepository.findAll();
        assertThat(lvilles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
