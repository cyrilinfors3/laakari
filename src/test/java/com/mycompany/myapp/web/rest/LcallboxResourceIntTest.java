package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lcallbox;
import com.mycompany.myapp.domain.Lroute;
import com.mycompany.myapp.repository.LcallboxRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Sex;
/**
 * Test class for the LcallboxResource REST controller.
 *
 * @see LcallboxResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LcallboxResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";

    private static final LocalDate DEFAULT_DATEOFBIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOFBIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_QUATIER = "AAAAA";
    private static final String UPDATED_QUATIER = "BBBBB";
    private static final String DEFAULT_MASTERSIM = "AAAAA";
    private static final String UPDATED_MASTERSIM = "BBBBB";

    private static final Sex DEFAULT_SEX = Sex.h;
    private static final Sex UPDATED_SEX = Sex.f;

    @Inject
    private LcallboxRepository lcallboxRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLcallboxMockMvc;

    private Lcallbox lcallbox;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LcallboxResource lcallboxResource = new LcallboxResource();
        ReflectionTestUtils.setField(lcallboxResource, "lcallboxRepository", lcallboxRepository);
        this.restLcallboxMockMvc = MockMvcBuilders.standaloneSetup(lcallboxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lcallbox createEntity(EntityManager em) {
        Lcallbox lcallbox = new Lcallbox()
                .nom(DEFAULT_NOM)
                .prenom(DEFAULT_PRENOM)
                .dateofbirth(DEFAULT_DATEOFBIRTH)
                .quatier(DEFAULT_QUATIER)
                .mastersim(DEFAULT_MASTERSIM)
                .sex(DEFAULT_SEX);
        // Add required entity
        Lroute lroute = LrouteResourceIntTest.createEntity(em);
        em.persist(lroute);
        em.flush();
        lcallbox.setLroute(lroute);
        return lcallbox;
    }

    @Before
    public void initTest() {
        lcallbox = createEntity(em);
    }

    @Test
    @Transactional
    public void createLcallbox() throws Exception {
        int databaseSizeBeforeCreate = lcallboxRepository.findAll().size();

        // Create the Lcallbox

        restLcallboxMockMvc.perform(post("/api/lcallboxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcallbox)))
                .andExpect(status().isCreated());

        // Validate the Lcallbox in the database
        List<Lcallbox> lcallboxes = lcallboxRepository.findAll();
        assertThat(lcallboxes).hasSize(databaseSizeBeforeCreate + 1);
        Lcallbox testLcallbox = lcallboxes.get(lcallboxes.size() - 1);
        assertThat(testLcallbox.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLcallbox.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testLcallbox.getDateofbirth()).isEqualTo(DEFAULT_DATEOFBIRTH);
        assertThat(testLcallbox.getQuatier()).isEqualTo(DEFAULT_QUATIER);
        assertThat(testLcallbox.getMastersim()).isEqualTo(DEFAULT_MASTERSIM);
        assertThat(testLcallbox.getSex()).isEqualTo(DEFAULT_SEX);
    }

    @Test
    @Transactional
    public void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = lcallboxRepository.findAll().size();
        // set the field null
        lcallbox.setSex(null);

        // Create the Lcallbox, which fails.

        restLcallboxMockMvc.perform(post("/api/lcallboxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lcallbox)))
                .andExpect(status().isBadRequest());

        List<Lcallbox> lcallboxes = lcallboxRepository.findAll();
        assertThat(lcallboxes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLcallboxes() throws Exception {
        // Initialize the database
        lcallboxRepository.saveAndFlush(lcallbox);

        // Get all the lcallboxes
        restLcallboxMockMvc.perform(get("/api/lcallboxes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lcallbox.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].dateofbirth").value(hasItem(DEFAULT_DATEOFBIRTH.toString())))
                .andExpect(jsonPath("$.[*].quatier").value(hasItem(DEFAULT_QUATIER.toString())))
                .andExpect(jsonPath("$.[*].mastersim").value(hasItem(DEFAULT_MASTERSIM.toString())))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())));
    }

    @Test
    @Transactional
    public void getLcallbox() throws Exception {
        // Initialize the database
        lcallboxRepository.saveAndFlush(lcallbox);

        // Get the lcallbox
        restLcallboxMockMvc.perform(get("/api/lcallboxes/{id}", lcallbox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lcallbox.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.dateofbirth").value(DEFAULT_DATEOFBIRTH.toString()))
            .andExpect(jsonPath("$.quatier").value(DEFAULT_QUATIER.toString()))
            .andExpect(jsonPath("$.mastersim").value(DEFAULT_MASTERSIM.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLcallbox() throws Exception {
        // Get the lcallbox
        restLcallboxMockMvc.perform(get("/api/lcallboxes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLcallbox() throws Exception {
        // Initialize the database
        lcallboxRepository.saveAndFlush(lcallbox);
        int databaseSizeBeforeUpdate = lcallboxRepository.findAll().size();

        // Update the lcallbox
        Lcallbox updatedLcallbox = lcallboxRepository.findOne(lcallbox.getId());
        updatedLcallbox
                .nom(UPDATED_NOM)
                .prenom(UPDATED_PRENOM)
                .dateofbirth(UPDATED_DATEOFBIRTH)
                .quatier(UPDATED_QUATIER)
                .mastersim(UPDATED_MASTERSIM)
                .sex(UPDATED_SEX);

        restLcallboxMockMvc.perform(put("/api/lcallboxes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLcallbox)))
                .andExpect(status().isOk());

        // Validate the Lcallbox in the database
        List<Lcallbox> lcallboxes = lcallboxRepository.findAll();
        assertThat(lcallboxes).hasSize(databaseSizeBeforeUpdate);
        Lcallbox testLcallbox = lcallboxes.get(lcallboxes.size() - 1);
        assertThat(testLcallbox.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLcallbox.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testLcallbox.getDateofbirth()).isEqualTo(UPDATED_DATEOFBIRTH);
        assertThat(testLcallbox.getQuatier()).isEqualTo(UPDATED_QUATIER);
        assertThat(testLcallbox.getMastersim()).isEqualTo(UPDATED_MASTERSIM);
        assertThat(testLcallbox.getSex()).isEqualTo(UPDATED_SEX);
    }

    @Test
    @Transactional
    public void deleteLcallbox() throws Exception {
        // Initialize the database
        lcallboxRepository.saveAndFlush(lcallbox);
        int databaseSizeBeforeDelete = lcallboxRepository.findAll().size();

        // Get the lcallbox
        restLcallboxMockMvc.perform(delete("/api/lcallboxes/{id}", lcallbox.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lcallbox> lcallboxes = lcallboxRepository.findAll();
        assertThat(lcallboxes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
