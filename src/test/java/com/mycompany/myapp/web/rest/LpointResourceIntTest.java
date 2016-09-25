package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lpoint;
import com.mycompany.myapp.repository.LpointRepository;

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
 * Test class for the LpointResource REST controller.
 *
 * @see LpointResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LpointResourceIntTest {


    private static final Float DEFAULT_LATI = 1F;
    private static final Float UPDATED_LATI = 2F;

    private static final Float DEFAULT_LONGI = 1F;
    private static final Float UPDATED_LONGI = 2F;

    @Inject
    private LpointRepository lpointRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLpointMockMvc;

    private Lpoint lpoint;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LpointResource lpointResource = new LpointResource();
        ReflectionTestUtils.setField(lpointResource, "lpointRepository", lpointRepository);
        this.restLpointMockMvc = MockMvcBuilders.standaloneSetup(lpointResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lpoint createEntity(EntityManager em) {
        Lpoint lpoint = new Lpoint()
                .lati(DEFAULT_LATI)
                .longi(DEFAULT_LONGI);
        return lpoint;
    }

    @Before
    public void initTest() {
        lpoint = createEntity(em);
    }

    @Test
    @Transactional
    public void createLpoint() throws Exception {
        int databaseSizeBeforeCreate = lpointRepository.findAll().size();

        // Create the Lpoint

        restLpointMockMvc.perform(post("/api/lpoints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lpoint)))
                .andExpect(status().isCreated());

        // Validate the Lpoint in the database
        List<Lpoint> lpoints = lpointRepository.findAll();
        assertThat(lpoints).hasSize(databaseSizeBeforeCreate + 1);
        Lpoint testLpoint = lpoints.get(lpoints.size() - 1);
        assertThat(testLpoint.getLati()).isEqualTo(DEFAULT_LATI);
        assertThat(testLpoint.getLongi()).isEqualTo(DEFAULT_LONGI);
    }

    @Test
    @Transactional
    public void checkLatiIsRequired() throws Exception {
        int databaseSizeBeforeTest = lpointRepository.findAll().size();
        // set the field null
        lpoint.setLati(null);

        // Create the Lpoint, which fails.

        restLpointMockMvc.perform(post("/api/lpoints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lpoint)))
                .andExpect(status().isBadRequest());

        List<Lpoint> lpoints = lpointRepository.findAll();
        assertThat(lpoints).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLongiIsRequired() throws Exception {
        int databaseSizeBeforeTest = lpointRepository.findAll().size();
        // set the field null
        lpoint.setLongi(null);

        // Create the Lpoint, which fails.

        restLpointMockMvc.perform(post("/api/lpoints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lpoint)))
                .andExpect(status().isBadRequest());

        List<Lpoint> lpoints = lpointRepository.findAll();
        assertThat(lpoints).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLpoints() throws Exception {
        // Initialize the database
        lpointRepository.saveAndFlush(lpoint);

        // Get all the lpoints
        restLpointMockMvc.perform(get("/api/lpoints?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lpoint.getId().intValue())))
                .andExpect(jsonPath("$.[*].lati").value(hasItem(DEFAULT_LATI.doubleValue())))
                .andExpect(jsonPath("$.[*].longi").value(hasItem(DEFAULT_LONGI.doubleValue())));
    }

    @Test
    @Transactional
    public void getLpoint() throws Exception {
        // Initialize the database
        lpointRepository.saveAndFlush(lpoint);

        // Get the lpoint
        restLpointMockMvc.perform(get("/api/lpoints/{id}", lpoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lpoint.getId().intValue()))
            .andExpect(jsonPath("$.lati").value(DEFAULT_LATI.doubleValue()))
            .andExpect(jsonPath("$.longi").value(DEFAULT_LONGI.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLpoint() throws Exception {
        // Get the lpoint
        restLpointMockMvc.perform(get("/api/lpoints/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLpoint() throws Exception {
        // Initialize the database
        lpointRepository.saveAndFlush(lpoint);
        int databaseSizeBeforeUpdate = lpointRepository.findAll().size();

        // Update the lpoint
        Lpoint updatedLpoint = lpointRepository.findOne(lpoint.getId());
        updatedLpoint
                .lati(UPDATED_LATI)
                .longi(UPDATED_LONGI);

        restLpointMockMvc.perform(put("/api/lpoints")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLpoint)))
                .andExpect(status().isOk());

        // Validate the Lpoint in the database
        List<Lpoint> lpoints = lpointRepository.findAll();
        assertThat(lpoints).hasSize(databaseSizeBeforeUpdate);
        Lpoint testLpoint = lpoints.get(lpoints.size() - 1);
        assertThat(testLpoint.getLati()).isEqualTo(UPDATED_LATI);
        assertThat(testLpoint.getLongi()).isEqualTo(UPDATED_LONGI);
    }

    @Test
    @Transactional
    public void deleteLpoint() throws Exception {
        // Initialize the database
        lpointRepository.saveAndFlush(lpoint);
        int databaseSizeBeforeDelete = lpointRepository.findAll().size();

        // Get the lpoint
        restLpointMockMvc.perform(delete("/api/lpoints/{id}", lpoint.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lpoint> lpoints = lpointRepository.findAll();
        assertThat(lpoints).hasSize(databaseSizeBeforeDelete - 1);
    }
}
