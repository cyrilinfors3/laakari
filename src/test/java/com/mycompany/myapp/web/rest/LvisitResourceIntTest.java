package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lvisit;
import com.mycompany.myapp.domain.Lcallbox;
import com.mycompany.myapp.repository.LvisitRepository;

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
 * Test class for the LvisitResource REST controller.
 *
 * @see LvisitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LvisitResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_CONTENTV = "AAAAA";
    private static final String UPDATED_CONTENTV = "BBBBB";
    private static final String DEFAULT_DURRATIONV = "AAAAA";
    private static final String UPDATED_DURRATIONV = "BBBBB";
    private static final String DEFAULT_STAFF = "AAAAA";
    private static final String UPDATED_STAFF = "BBBBB";

    @Inject
    private LvisitRepository lvisitRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLvisitMockMvc;

    private Lvisit lvisit;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LvisitResource lvisitResource = new LvisitResource();
        ReflectionTestUtils.setField(lvisitResource, "lvisitRepository", lvisitRepository);
        this.restLvisitMockMvc = MockMvcBuilders.standaloneSetup(lvisitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lvisit createEntity(EntityManager em) {
        Lvisit lvisit = new Lvisit()
                .date(DEFAULT_DATE)
                .contentv(DEFAULT_CONTENTV)
                .durrationv(DEFAULT_DURRATIONV)
                .staff(DEFAULT_STAFF);
        // Add required entity
        Lcallbox lcallbox = LcallboxResourceIntTest.createEntity(em);
        em.persist(lcallbox);
        em.flush();
        lvisit.setLcallbox(lcallbox);
        return lvisit;
    }

    @Before
    public void initTest() {
        lvisit = createEntity(em);
    }

    @Test
    @Transactional
    public void createLvisit() throws Exception {
        int databaseSizeBeforeCreate = lvisitRepository.findAll().size();

        // Create the Lvisit

        restLvisitMockMvc.perform(post("/api/lvisits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lvisit)))
                .andExpect(status().isCreated());

        // Validate the Lvisit in the database
        List<Lvisit> lvisits = lvisitRepository.findAll();
        assertThat(lvisits).hasSize(databaseSizeBeforeCreate + 1);
        Lvisit testLvisit = lvisits.get(lvisits.size() - 1);
        assertThat(testLvisit.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testLvisit.getContentv()).isEqualTo(DEFAULT_CONTENTV);
        assertThat(testLvisit.getDurrationv()).isEqualTo(DEFAULT_DURRATIONV);
        assertThat(testLvisit.getStaff()).isEqualTo(DEFAULT_STAFF);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lvisitRepository.findAll().size();
        // set the field null
        lvisit.setDate(null);

        // Create the Lvisit, which fails.

        restLvisitMockMvc.perform(post("/api/lvisits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lvisit)))
                .andExpect(status().isBadRequest());

        List<Lvisit> lvisits = lvisitRepository.findAll();
        assertThat(lvisits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContentvIsRequired() throws Exception {
        int databaseSizeBeforeTest = lvisitRepository.findAll().size();
        // set the field null
        lvisit.setContentv(null);

        // Create the Lvisit, which fails.

        restLvisitMockMvc.perform(post("/api/lvisits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lvisit)))
                .andExpect(status().isBadRequest());

        List<Lvisit> lvisits = lvisitRepository.findAll();
        assertThat(lvisits).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLvisits() throws Exception {
        // Initialize the database
        lvisitRepository.saveAndFlush(lvisit);

        // Get all the lvisits
        restLvisitMockMvc.perform(get("/api/lvisits?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lvisit.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].contentv").value(hasItem(DEFAULT_CONTENTV.toString())))
                .andExpect(jsonPath("$.[*].durrationv").value(hasItem(DEFAULT_DURRATIONV.toString())))
                .andExpect(jsonPath("$.[*].staff").value(hasItem(DEFAULT_STAFF.toString())));
    }

    @Test
    @Transactional
    public void getLvisit() throws Exception {
        // Initialize the database
        lvisitRepository.saveAndFlush(lvisit);

        // Get the lvisit
        restLvisitMockMvc.perform(get("/api/lvisits/{id}", lvisit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lvisit.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.contentv").value(DEFAULT_CONTENTV.toString()))
            .andExpect(jsonPath("$.durrationv").value(DEFAULT_DURRATIONV.toString()))
            .andExpect(jsonPath("$.staff").value(DEFAULT_STAFF.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLvisit() throws Exception {
        // Get the lvisit
        restLvisitMockMvc.perform(get("/api/lvisits/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLvisit() throws Exception {
        // Initialize the database
        lvisitRepository.saveAndFlush(lvisit);
        int databaseSizeBeforeUpdate = lvisitRepository.findAll().size();

        // Update the lvisit
        Lvisit updatedLvisit = lvisitRepository.findOne(lvisit.getId());
        updatedLvisit
                .date(UPDATED_DATE)
                .contentv(UPDATED_CONTENTV)
                .durrationv(UPDATED_DURRATIONV)
                .staff(UPDATED_STAFF);

        restLvisitMockMvc.perform(put("/api/lvisits")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLvisit)))
                .andExpect(status().isOk());

        // Validate the Lvisit in the database
        List<Lvisit> lvisits = lvisitRepository.findAll();
        assertThat(lvisits).hasSize(databaseSizeBeforeUpdate);
        Lvisit testLvisit = lvisits.get(lvisits.size() - 1);
        assertThat(testLvisit.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testLvisit.getContentv()).isEqualTo(UPDATED_CONTENTV);
        assertThat(testLvisit.getDurrationv()).isEqualTo(UPDATED_DURRATIONV);
        assertThat(testLvisit.getStaff()).isEqualTo(UPDATED_STAFF);
    }

    @Test
    @Transactional
    public void deleteLvisit() throws Exception {
        // Initialize the database
        lvisitRepository.saveAndFlush(lvisit);
        int databaseSizeBeforeDelete = lvisitRepository.findAll().size();

        // Get the lvisit
        restLvisitMockMvc.perform(delete("/api/lvisits/{id}", lvisit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lvisit> lvisits = lvisitRepository.findAll();
        assertThat(lvisits).hasSize(databaseSizeBeforeDelete - 1);
    }
}
