package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Dmanager;
import com.mycompany.myapp.repository.DmanagerRepository;

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
 * Test class for the DmanagerResource REST controller.
 *
 * @see DmanagerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class DmanagerResourceIntTest {


    private static final Integer DEFAULT_TEL = 1;
    private static final Integer UPDATED_TEL = 2;
    private static final String DEFAULT_AGENTCODE = "AAAAA";
    private static final String UPDATED_AGENTCODE = "BBBBB";

    @Inject
    private DmanagerRepository dmanagerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDmanagerMockMvc;

    private Dmanager dmanager;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DmanagerResource dmanagerResource = new DmanagerResource();
        ReflectionTestUtils.setField(dmanagerResource, "dmanagerRepository", dmanagerRepository);
        this.restDmanagerMockMvc = MockMvcBuilders.standaloneSetup(dmanagerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dmanager createEntity(EntityManager em) {
        Dmanager dmanager = new Dmanager()
                .tel(DEFAULT_TEL)
                .agentcode(DEFAULT_AGENTCODE);
        return dmanager;
    }

    @Before
    public void initTest() {
        dmanager = createEntity(em);
    }

    @Test
    @Transactional
    public void createDmanager() throws Exception {
        int databaseSizeBeforeCreate = dmanagerRepository.findAll().size();

        // Create the Dmanager

        restDmanagerMockMvc.perform(post("/api/dmanagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dmanager)))
                .andExpect(status().isCreated());

        // Validate the Dmanager in the database
        List<Dmanager> dmanagers = dmanagerRepository.findAll();
        assertThat(dmanagers).hasSize(databaseSizeBeforeCreate + 1);
        Dmanager testDmanager = dmanagers.get(dmanagers.size() - 1);
        assertThat(testDmanager.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testDmanager.getAgentcode()).isEqualTo(DEFAULT_AGENTCODE);
    }

    @Test
    @Transactional
    public void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = dmanagerRepository.findAll().size();
        // set the field null
        dmanager.setTel(null);

        // Create the Dmanager, which fails.

        restDmanagerMockMvc.perform(post("/api/dmanagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dmanager)))
                .andExpect(status().isBadRequest());

        List<Dmanager> dmanagers = dmanagerRepository.findAll();
        assertThat(dmanagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgentcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dmanagerRepository.findAll().size();
        // set the field null
        dmanager.setAgentcode(null);

        // Create the Dmanager, which fails.

        restDmanagerMockMvc.perform(post("/api/dmanagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dmanager)))
                .andExpect(status().isBadRequest());

        List<Dmanager> dmanagers = dmanagerRepository.findAll();
        assertThat(dmanagers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDmanagers() throws Exception {
        // Initialize the database
        dmanagerRepository.saveAndFlush(dmanager);

        // Get all the dmanagers
        restDmanagerMockMvc.perform(get("/api/dmanagers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dmanager.getId().intValue())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
                .andExpect(jsonPath("$.[*].agentcode").value(hasItem(DEFAULT_AGENTCODE.toString())));
    }

    @Test
    @Transactional
    public void getDmanager() throws Exception {
        // Initialize the database
        dmanagerRepository.saveAndFlush(dmanager);

        // Get the dmanager
        restDmanagerMockMvc.perform(get("/api/dmanagers/{id}", dmanager.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dmanager.getId().intValue()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.agentcode").value(DEFAULT_AGENTCODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDmanager() throws Exception {
        // Get the dmanager
        restDmanagerMockMvc.perform(get("/api/dmanagers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDmanager() throws Exception {
        // Initialize the database
        dmanagerRepository.saveAndFlush(dmanager);
        int databaseSizeBeforeUpdate = dmanagerRepository.findAll().size();

        // Update the dmanager
        Dmanager updatedDmanager = dmanagerRepository.findOne(dmanager.getId());
        updatedDmanager
                .tel(UPDATED_TEL)
                .agentcode(UPDATED_AGENTCODE);

        restDmanagerMockMvc.perform(put("/api/dmanagers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDmanager)))
                .andExpect(status().isOk());

        // Validate the Dmanager in the database
        List<Dmanager> dmanagers = dmanagerRepository.findAll();
        assertThat(dmanagers).hasSize(databaseSizeBeforeUpdate);
        Dmanager testDmanager = dmanagers.get(dmanagers.size() - 1);
        assertThat(testDmanager.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testDmanager.getAgentcode()).isEqualTo(UPDATED_AGENTCODE);
    }

    @Test
    @Transactional
    public void deleteDmanager() throws Exception {
        // Initialize the database
        dmanagerRepository.saveAndFlush(dmanager);
        int databaseSizeBeforeDelete = dmanagerRepository.findAll().size();

        // Get the dmanager
        restDmanagerMockMvc.perform(delete("/api/dmanagers/{id}", dmanager.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dmanager> dmanagers = dmanagerRepository.findAll();
        assertThat(dmanagers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
