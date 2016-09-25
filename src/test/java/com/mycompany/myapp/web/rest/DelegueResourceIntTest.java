package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Delegue;
import com.mycompany.myapp.repository.DelegueRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DelegueResource REST controller.
 *
 * @see DelegueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class DelegueResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final Integer DEFAULT_MASTERSIM = 1;
    private static final Integer UPDATED_MASTERSIM = 2;

    private static final Integer DEFAULT_NUMID = 1;
    private static final Integer UPDATED_NUMID = 2;

    private static final LocalDate DEFAULT_ISSUEDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUEDATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATEOFBIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATEOFBIRTH = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_QUATIER = "AAAAA";
    private static final String UPDATED_QUATIER = "BBBBB";
    private static final String DEFAULT_SEX = "AAAAA";
    private static final String UPDATED_SEX = "BBBBB";

    private static final byte[] DEFAULT_PIC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIC = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PIC_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIC_CONTENT_TYPE = "image/png";

    @Inject
    private DelegueRepository delegueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDelegueMockMvc;

    private Delegue delegue;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DelegueResource delegueResource = new DelegueResource();
        ReflectionTestUtils.setField(delegueResource, "delegueRepository", delegueRepository);
        this.restDelegueMockMvc = MockMvcBuilders.standaloneSetup(delegueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Delegue createEntity(EntityManager em) {
        Delegue delegue = new Delegue()
                .code(DEFAULT_CODE)
                .mastersim(DEFAULT_MASTERSIM)
                .numid(DEFAULT_NUMID)
                .issuedate(DEFAULT_ISSUEDATE)
                .dateofbirth(DEFAULT_DATEOFBIRTH)
                .quatier(DEFAULT_QUATIER)
                .sex(DEFAULT_SEX)
                .pic(DEFAULT_PIC)
                .picContentType(DEFAULT_PIC_CONTENT_TYPE);
        return delegue;
    }

    @Before
    public void initTest() {
        delegue = createEntity(em);
    }

    @Test
    @Transactional
    public void createDelegue() throws Exception {
        int databaseSizeBeforeCreate = delegueRepository.findAll().size();

        // Create the Delegue

        restDelegueMockMvc.perform(post("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegue)))
                .andExpect(status().isCreated());

        // Validate the Delegue in the database
        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeCreate + 1);
        Delegue testDelegue = delegues.get(delegues.size() - 1);
        assertThat(testDelegue.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testDelegue.getMastersim()).isEqualTo(DEFAULT_MASTERSIM);
        assertThat(testDelegue.getNumid()).isEqualTo(DEFAULT_NUMID);
        assertThat(testDelegue.getIssuedate()).isEqualTo(DEFAULT_ISSUEDATE);
        assertThat(testDelegue.getDateofbirth()).isEqualTo(DEFAULT_DATEOFBIRTH);
        assertThat(testDelegue.getQuatier()).isEqualTo(DEFAULT_QUATIER);
        assertThat(testDelegue.getSex()).isEqualTo(DEFAULT_SEX);
        assertThat(testDelegue.getPic()).isEqualTo(DEFAULT_PIC);
        assertThat(testDelegue.getPicContentType()).isEqualTo(DEFAULT_PIC_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkMastersimIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegueRepository.findAll().size();
        // set the field null
        delegue.setMastersim(null);

        // Create the Delegue, which fails.

        restDelegueMockMvc.perform(post("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegue)))
                .andExpect(status().isBadRequest());

        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIssuedateIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegueRepository.findAll().size();
        // set the field null
        delegue.setIssuedate(null);

        // Create the Delegue, which fails.

        restDelegueMockMvc.perform(post("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegue)))
                .andExpect(status().isBadRequest());

        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateofbirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegueRepository.findAll().size();
        // set the field null
        delegue.setDateofbirth(null);

        // Create the Delegue, which fails.

        restDelegueMockMvc.perform(post("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegue)))
                .andExpect(status().isBadRequest());

        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuatierIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegueRepository.findAll().size();
        // set the field null
        delegue.setQuatier(null);

        // Create the Delegue, which fails.

        restDelegueMockMvc.perform(post("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegue)))
                .andExpect(status().isBadRequest());

        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexIsRequired() throws Exception {
        int databaseSizeBeforeTest = delegueRepository.findAll().size();
        // set the field null
        delegue.setSex(null);

        // Create the Delegue, which fails.

        restDelegueMockMvc.perform(post("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(delegue)))
                .andExpect(status().isBadRequest());

        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDelegues() throws Exception {
        // Initialize the database
        delegueRepository.saveAndFlush(delegue);

        // Get all the delegues
        restDelegueMockMvc.perform(get("/api/delegues?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(delegue.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].mastersim").value(hasItem(DEFAULT_MASTERSIM)))
                .andExpect(jsonPath("$.[*].numid").value(hasItem(DEFAULT_NUMID)))
                .andExpect(jsonPath("$.[*].issuedate").value(hasItem(DEFAULT_ISSUEDATE.toString())))
                .andExpect(jsonPath("$.[*].dateofbirth").value(hasItem(DEFAULT_DATEOFBIRTH.toString())))
                .andExpect(jsonPath("$.[*].quatier").value(hasItem(DEFAULT_QUATIER.toString())))
                .andExpect(jsonPath("$.[*].sex").value(hasItem(DEFAULT_SEX.toString())))
                .andExpect(jsonPath("$.[*].picContentType").value(hasItem(DEFAULT_PIC_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].pic").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIC))));
    }

    @Test
    @Transactional
    public void getDelegue() throws Exception {
        // Initialize the database
        delegueRepository.saveAndFlush(delegue);

        // Get the delegue
        restDelegueMockMvc.perform(get("/api/delegues/{id}", delegue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(delegue.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.mastersim").value(DEFAULT_MASTERSIM))
            .andExpect(jsonPath("$.numid").value(DEFAULT_NUMID))
            .andExpect(jsonPath("$.issuedate").value(DEFAULT_ISSUEDATE.toString()))
            .andExpect(jsonPath("$.dateofbirth").value(DEFAULT_DATEOFBIRTH.toString()))
            .andExpect(jsonPath("$.quatier").value(DEFAULT_QUATIER.toString()))
            .andExpect(jsonPath("$.sex").value(DEFAULT_SEX.toString()))
            .andExpect(jsonPath("$.picContentType").value(DEFAULT_PIC_CONTENT_TYPE))
            .andExpect(jsonPath("$.pic").value(Base64Utils.encodeToString(DEFAULT_PIC)));
    }

    @Test
    @Transactional
    public void getNonExistingDelegue() throws Exception {
        // Get the delegue
        restDelegueMockMvc.perform(get("/api/delegues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDelegue() throws Exception {
        // Initialize the database
        delegueRepository.saveAndFlush(delegue);
        int databaseSizeBeforeUpdate = delegueRepository.findAll().size();

        // Update the delegue
        Delegue updatedDelegue = delegueRepository.findOne(delegue.getId());
        updatedDelegue
                .code(UPDATED_CODE)
                .mastersim(UPDATED_MASTERSIM)
                .numid(UPDATED_NUMID)
                .issuedate(UPDATED_ISSUEDATE)
                .dateofbirth(UPDATED_DATEOFBIRTH)
                .quatier(UPDATED_QUATIER)
                .sex(UPDATED_SEX)
                .pic(UPDATED_PIC)
                .picContentType(UPDATED_PIC_CONTENT_TYPE);

        restDelegueMockMvc.perform(put("/api/delegues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDelegue)))
                .andExpect(status().isOk());

        // Validate the Delegue in the database
        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeUpdate);
        Delegue testDelegue = delegues.get(delegues.size() - 1);
        assertThat(testDelegue.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testDelegue.getMastersim()).isEqualTo(UPDATED_MASTERSIM);
        assertThat(testDelegue.getNumid()).isEqualTo(UPDATED_NUMID);
        assertThat(testDelegue.getIssuedate()).isEqualTo(UPDATED_ISSUEDATE);
        assertThat(testDelegue.getDateofbirth()).isEqualTo(UPDATED_DATEOFBIRTH);
        assertThat(testDelegue.getQuatier()).isEqualTo(UPDATED_QUATIER);
        assertThat(testDelegue.getSex()).isEqualTo(UPDATED_SEX);
        assertThat(testDelegue.getPic()).isEqualTo(UPDATED_PIC);
        assertThat(testDelegue.getPicContentType()).isEqualTo(UPDATED_PIC_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteDelegue() throws Exception {
        // Initialize the database
        delegueRepository.saveAndFlush(delegue);
        int databaseSizeBeforeDelete = delegueRepository.findAll().size();

        // Get the delegue
        restDelegueMockMvc.perform(delete("/api/delegues/{id}", delegue.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Delegue> delegues = delegueRepository.findAll();
        assertThat(delegues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
