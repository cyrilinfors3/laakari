package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lzone;
import com.mycompany.myapp.repository.LzoneRepository;

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
 * Test class for the LzoneResource REST controller.
 *
 * @see LzoneResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LzoneResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATECREATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATECREATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATECREATION_STR = dateTimeFormatter.format(DEFAULT_DATECREATION);

    private static final ZonedDateTime DEFAULT_DATEMODIF = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATEMODIF = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATEMODIF_STR = dateTimeFormatter.format(DEFAULT_DATEMODIF);
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    @Inject
    private LzoneRepository lzoneRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLzoneMockMvc;

    private Lzone lzone;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LzoneResource lzoneResource = new LzoneResource();
        ReflectionTestUtils.setField(lzoneResource, "lzoneRepository", lzoneRepository);
        this.restLzoneMockMvc = MockMvcBuilders.standaloneSetup(lzoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lzone createEntity(EntityManager em) {
        Lzone lzone = new Lzone()
                .datecreation(DEFAULT_DATECREATION)
                .datemodif(DEFAULT_DATEMODIF)
                .type(DEFAULT_TYPE);
        return lzone;
    }

    @Before
    public void initTest() {
        lzone = createEntity(em);
    }

    @Test
    @Transactional
    public void createLzone() throws Exception {
        int databaseSizeBeforeCreate = lzoneRepository.findAll().size();

        // Create the Lzone

        restLzoneMockMvc.perform(post("/api/lzones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lzone)))
                .andExpect(status().isCreated());

        // Validate the Lzone in the database
        List<Lzone> lzones = lzoneRepository.findAll();
        assertThat(lzones).hasSize(databaseSizeBeforeCreate + 1);
        Lzone testLzone = lzones.get(lzones.size() - 1);
        assertThat(testLzone.getDatecreation()).isEqualTo(DEFAULT_DATECREATION);
        assertThat(testLzone.getDatemodif()).isEqualTo(DEFAULT_DATEMODIF);
        assertThat(testLzone.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void checkDatecreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = lzoneRepository.findAll().size();
        // set the field null
        lzone.setDatecreation(null);

        // Create the Lzone, which fails.

        restLzoneMockMvc.perform(post("/api/lzones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lzone)))
                .andExpect(status().isBadRequest());

        List<Lzone> lzones = lzoneRepository.findAll();
        assertThat(lzones).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lzoneRepository.findAll().size();
        // set the field null
        lzone.setType(null);

        // Create the Lzone, which fails.

        restLzoneMockMvc.perform(post("/api/lzones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lzone)))
                .andExpect(status().isBadRequest());

        List<Lzone> lzones = lzoneRepository.findAll();
        assertThat(lzones).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLzones() throws Exception {
        // Initialize the database
        lzoneRepository.saveAndFlush(lzone);

        // Get all the lzones
        restLzoneMockMvc.perform(get("/api/lzones?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lzone.getId().intValue())))
                .andExpect(jsonPath("$.[*].datecreation").value(hasItem(DEFAULT_DATECREATION_STR)))
                .andExpect(jsonPath("$.[*].datemodif").value(hasItem(DEFAULT_DATEMODIF_STR)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getLzone() throws Exception {
        // Initialize the database
        lzoneRepository.saveAndFlush(lzone);

        // Get the lzone
        restLzoneMockMvc.perform(get("/api/lzones/{id}", lzone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lzone.getId().intValue()))
            .andExpect(jsonPath("$.datecreation").value(DEFAULT_DATECREATION_STR))
            .andExpect(jsonPath("$.datemodif").value(DEFAULT_DATEMODIF_STR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLzone() throws Exception {
        // Get the lzone
        restLzoneMockMvc.perform(get("/api/lzones/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLzone() throws Exception {
        // Initialize the database
        lzoneRepository.saveAndFlush(lzone);
        int databaseSizeBeforeUpdate = lzoneRepository.findAll().size();

        // Update the lzone
        Lzone updatedLzone = lzoneRepository.findOne(lzone.getId());
        updatedLzone
                .datecreation(UPDATED_DATECREATION)
                .datemodif(UPDATED_DATEMODIF)
                .type(UPDATED_TYPE);

        restLzoneMockMvc.perform(put("/api/lzones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLzone)))
                .andExpect(status().isOk());

        // Validate the Lzone in the database
        List<Lzone> lzones = lzoneRepository.findAll();
        assertThat(lzones).hasSize(databaseSizeBeforeUpdate);
        Lzone testLzone = lzones.get(lzones.size() - 1);
        assertThat(testLzone.getDatecreation()).isEqualTo(UPDATED_DATECREATION);
        assertThat(testLzone.getDatemodif()).isEqualTo(UPDATED_DATEMODIF);
        assertThat(testLzone.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteLzone() throws Exception {
        // Initialize the database
        lzoneRepository.saveAndFlush(lzone);
        int databaseSizeBeforeDelete = lzoneRepository.findAll().size();

        // Get the lzone
        restLzoneMockMvc.perform(delete("/api/lzones/{id}", lzone.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lzone> lzones = lzoneRepository.findAll();
        assertThat(lzones).hasSize(databaseSizeBeforeDelete - 1);
    }
}
