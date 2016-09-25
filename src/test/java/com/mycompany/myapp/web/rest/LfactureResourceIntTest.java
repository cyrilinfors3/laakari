package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lfacture;
import com.mycompany.myapp.repository.LfactureRepository;

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
 * Test class for the LfactureResource REST controller.
 *
 * @see LfactureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LfactureResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CODEBILL = "AAAAA";
    private static final String UPDATED_CODEBILL = "BBBBB";

    private static final Float DEFAULT_TOTAL = 1F;
    private static final Float UPDATED_TOTAL = 2F;

    private static final ZonedDateTime DEFAULT_FDATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FDATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FDATE_STR = dateTimeFormatter.format(DEFAULT_FDATE);

    @Inject
    private LfactureRepository lfactureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLfactureMockMvc;

    private Lfacture lfacture;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LfactureResource lfactureResource = new LfactureResource();
        ReflectionTestUtils.setField(lfactureResource, "lfactureRepository", lfactureRepository);
        this.restLfactureMockMvc = MockMvcBuilders.standaloneSetup(lfactureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lfacture createEntity(EntityManager em) {
        Lfacture lfacture = new Lfacture()
                .codebill(DEFAULT_CODEBILL)
                .total(DEFAULT_TOTAL)
                .fdate(DEFAULT_FDATE);
        return lfacture;
    }

    @Before
    public void initTest() {
        lfacture = createEntity(em);
    }

    @Test
    @Transactional
    public void createLfacture() throws Exception {
        int databaseSizeBeforeCreate = lfactureRepository.findAll().size();

        // Create the Lfacture

        restLfactureMockMvc.perform(post("/api/lfactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lfacture)))
                .andExpect(status().isCreated());

        // Validate the Lfacture in the database
        List<Lfacture> lfactures = lfactureRepository.findAll();
        assertThat(lfactures).hasSize(databaseSizeBeforeCreate + 1);
        Lfacture testLfacture = lfactures.get(lfactures.size() - 1);
        assertThat(testLfacture.getCodebill()).isEqualTo(DEFAULT_CODEBILL);
        assertThat(testLfacture.getTotal()).isEqualTo(DEFAULT_TOTAL);
        assertThat(testLfacture.getFdate()).isEqualTo(DEFAULT_FDATE);
    }

    @Test
    @Transactional
    public void checkTotalIsRequired() throws Exception {
        int databaseSizeBeforeTest = lfactureRepository.findAll().size();
        // set the field null
        lfacture.setTotal(null);

        // Create the Lfacture, which fails.

        restLfactureMockMvc.perform(post("/api/lfactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lfacture)))
                .andExpect(status().isBadRequest());

        List<Lfacture> lfactures = lfactureRepository.findAll();
        assertThat(lfactures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lfactureRepository.findAll().size();
        // set the field null
        lfacture.setFdate(null);

        // Create the Lfacture, which fails.

        restLfactureMockMvc.perform(post("/api/lfactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lfacture)))
                .andExpect(status().isBadRequest());

        List<Lfacture> lfactures = lfactureRepository.findAll();
        assertThat(lfactures).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLfactures() throws Exception {
        // Initialize the database
        lfactureRepository.saveAndFlush(lfacture);

        // Get all the lfactures
        restLfactureMockMvc.perform(get("/api/lfactures?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lfacture.getId().intValue())))
                .andExpect(jsonPath("$.[*].codebill").value(hasItem(DEFAULT_CODEBILL.toString())))
                .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.doubleValue())))
                .andExpect(jsonPath("$.[*].fdate").value(hasItem(DEFAULT_FDATE_STR)));
    }

    @Test
    @Transactional
    public void getLfacture() throws Exception {
        // Initialize the database
        lfactureRepository.saveAndFlush(lfacture);

        // Get the lfacture
        restLfactureMockMvc.perform(get("/api/lfactures/{id}", lfacture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lfacture.getId().intValue()))
            .andExpect(jsonPath("$.codebill").value(DEFAULT_CODEBILL.toString()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.doubleValue()))
            .andExpect(jsonPath("$.fdate").value(DEFAULT_FDATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingLfacture() throws Exception {
        // Get the lfacture
        restLfactureMockMvc.perform(get("/api/lfactures/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLfacture() throws Exception {
        // Initialize the database
        lfactureRepository.saveAndFlush(lfacture);
        int databaseSizeBeforeUpdate = lfactureRepository.findAll().size();

        // Update the lfacture
        Lfacture updatedLfacture = lfactureRepository.findOne(lfacture.getId());
        updatedLfacture
                .codebill(UPDATED_CODEBILL)
                .total(UPDATED_TOTAL)
                .fdate(UPDATED_FDATE);

        restLfactureMockMvc.perform(put("/api/lfactures")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLfacture)))
                .andExpect(status().isOk());

        // Validate the Lfacture in the database
        List<Lfacture> lfactures = lfactureRepository.findAll();
        assertThat(lfactures).hasSize(databaseSizeBeforeUpdate);
        Lfacture testLfacture = lfactures.get(lfactures.size() - 1);
        assertThat(testLfacture.getCodebill()).isEqualTo(UPDATED_CODEBILL);
        assertThat(testLfacture.getTotal()).isEqualTo(UPDATED_TOTAL);
        assertThat(testLfacture.getFdate()).isEqualTo(UPDATED_FDATE);
    }

    @Test
    @Transactional
    public void deleteLfacture() throws Exception {
        // Initialize the database
        lfactureRepository.saveAndFlush(lfacture);
        int databaseSizeBeforeDelete = lfactureRepository.findAll().size();

        // Get the lfacture
        restLfactureMockMvc.perform(delete("/api/lfactures/{id}", lfacture.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lfacture> lfactures = lfactureRepository.findAll();
        assertThat(lfactures).hasSize(databaseSizeBeforeDelete - 1);
    }
}
