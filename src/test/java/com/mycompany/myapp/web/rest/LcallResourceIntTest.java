package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lcall;
import com.mycompany.myapp.domain.Luser;
import com.mycompany.myapp.repository.LcallRepository;

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
 * Test class for the LcallResource REST controller.
 *
 * @see LcallResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LcallResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_EMITEUR = "AAAAA";
    private static final String UPDATED_EMITEUR = "BBBBB";
    private static final String DEFAULT_RECEPTEUR = "AAAAA";
    private static final String UPDATED_RECEPTEUR = "BBBBB";

    private static final ZonedDateTime DEFAULT_CALLTIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CALLTIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CALLTIME_STR = dateTimeFormatter.format(DEFAULT_CALLTIME);

    @Inject
    private LcallRepository lcallRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLcallMockMvc;

    private Lcall lcall;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LcallResource lcallResource = new LcallResource();
        ReflectionTestUtils.setField(lcallResource, "lcallRepository", lcallRepository);
        this.restLcallMockMvc = MockMvcBuilders.standaloneSetup(lcallResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lcall createEntity(EntityManager em) {
        Lcall lcall = new Lcall()
                .emiteur(DEFAULT_EMITEUR)
                .recepteur(DEFAULT_RECEPTEUR)
                .calltime(DEFAULT_CALLTIME);
        // Add required entity
        Luser luser = LuserResourceIntTest.createEntity(em);
        em.persist(luser);
        em.flush();
        lcall.setLuser(luser);
        return lcall;
    }

    @Before
    public void initTest() {
        lcall = createEntity(em);
    }

    @Test
    @Transactional
    public void createLcall() throws Exception {
        int databaseSizeBeforeCreate = lcallRepository.findAll().size();

        // Create the Lcall

        restLcallMockMvc.perform(post("/api/lcalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcall)))
                .andExpect(status().isCreated());

        // Validate the Lcall in the database
        List<Lcall> lcalls = lcallRepository.findAll();
        assertThat(lcalls).hasSize(databaseSizeBeforeCreate + 1);
        Lcall testLcall = lcalls.get(lcalls.size() - 1);
        assertThat(testLcall.getEmiteur()).isEqualTo(DEFAULT_EMITEUR);
        assertThat(testLcall.getRecepteur()).isEqualTo(DEFAULT_RECEPTEUR);
        assertThat(testLcall.getCalltime()).isEqualTo(DEFAULT_CALLTIME);
    }

    @Test
    @Transactional
    public void checkEmiteurIsRequired() throws Exception {
        int databaseSizeBeforeTest = lcallRepository.findAll().size();
        // set the field null
        lcall.setEmiteur(null);

        // Create the Lcall, which fails.

        restLcallMockMvc.perform(post("/api/lcalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcall)))
                .andExpect(status().isBadRequest());

        List<Lcall> lcalls = lcallRepository.findAll();
        assertThat(lcalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRecepteurIsRequired() throws Exception {
        int databaseSizeBeforeTest = lcallRepository.findAll().size();
        // set the field null
        lcall.setRecepteur(null);

        // Create the Lcall, which fails.

        restLcallMockMvc.perform(post("/api/lcalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcall)))
                .andExpect(status().isBadRequest());

        List<Lcall> lcalls = lcallRepository.findAll();
        assertThat(lcalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCalltimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lcallRepository.findAll().size();
        // set the field null
        lcall.setCalltime(null);

        // Create the Lcall, which fails.

        restLcallMockMvc.perform(post("/api/lcalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcall)))
                .andExpect(status().isBadRequest());

        List<Lcall> lcalls = lcallRepository.findAll();
        assertThat(lcalls).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLcalls() throws Exception {
        // Initialize the database
        lcallRepository.saveAndFlush(lcall);

        // Get all the lcalls
        restLcallMockMvc.perform(get("/api/lcalls?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lcall.getId().intValue())))
                .andExpect(jsonPath("$.[*].emiteur").value(hasItem(DEFAULT_EMITEUR.toString())))
                .andExpect(jsonPath("$.[*].recepteur").value(hasItem(DEFAULT_RECEPTEUR.toString())))
                .andExpect(jsonPath("$.[*].calltime").value(hasItem(DEFAULT_CALLTIME_STR)));
    }

    @Test
    @Transactional
    public void getLcall() throws Exception {
        // Initialize the database
        lcallRepository.saveAndFlush(lcall);

        // Get the lcall
        restLcallMockMvc.perform(get("/api/lcalls/{id}", lcall.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lcall.getId().intValue()))
            .andExpect(jsonPath("$.emiteur").value(DEFAULT_EMITEUR.toString()))
            .andExpect(jsonPath("$.recepteur").value(DEFAULT_RECEPTEUR.toString()))
            .andExpect(jsonPath("$.calltime").value(DEFAULT_CALLTIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingLcall() throws Exception {
        // Get the lcall
        restLcallMockMvc.perform(get("/api/lcalls/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLcall() throws Exception {
        // Initialize the database
        lcallRepository.saveAndFlush(lcall);
        int databaseSizeBeforeUpdate = lcallRepository.findAll().size();

        // Update the lcall
        Lcall updatedLcall = lcallRepository.findOne(lcall.getId());
        updatedLcall
                .emiteur(UPDATED_EMITEUR)
                .recepteur(UPDATED_RECEPTEUR)
                .calltime(UPDATED_CALLTIME);

        restLcallMockMvc.perform(put("/api/lcalls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLcall)))
                .andExpect(status().isOk());

        // Validate the Lcall in the database
        List<Lcall> lcalls = lcallRepository.findAll();
        assertThat(lcalls).hasSize(databaseSizeBeforeUpdate);
        Lcall testLcall = lcalls.get(lcalls.size() - 1);
        assertThat(testLcall.getEmiteur()).isEqualTo(UPDATED_EMITEUR);
        assertThat(testLcall.getRecepteur()).isEqualTo(UPDATED_RECEPTEUR);
        assertThat(testLcall.getCalltime()).isEqualTo(UPDATED_CALLTIME);
    }

    @Test
    @Transactional
    public void deleteLcall() throws Exception {
        // Initialize the database
        lcallRepository.saveAndFlush(lcall);
        int databaseSizeBeforeDelete = lcallRepository.findAll().size();

        // Get the lcall
        restLcallMockMvc.perform(delete("/api/lcalls/{id}", lcall.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lcall> lcalls = lcallRepository.findAll();
        assertThat(lcalls).hasSize(databaseSizeBeforeDelete - 1);
    }
}
