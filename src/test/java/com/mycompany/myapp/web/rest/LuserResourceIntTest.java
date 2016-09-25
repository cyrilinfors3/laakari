package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Luser;
import com.mycompany.myapp.repository.LuserRepository;

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
 * Test class for the LuserResource REST controller.
 *
 * @see LuserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LuserResourceIntTest {

    private static final String DEFAULT_IDENTIFIANT = "AAAAA";
    private static final String UPDATED_IDENTIFIANT = "BBBBB";

    @Inject
    private LuserRepository luserRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLuserMockMvc;

    private Luser luser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LuserResource luserResource = new LuserResource();
        ReflectionTestUtils.setField(luserResource, "luserRepository", luserRepository);
        this.restLuserMockMvc = MockMvcBuilders.standaloneSetup(luserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Luser createEntity(EntityManager em) {
        Luser luser = new Luser()
                .identifiant(DEFAULT_IDENTIFIANT);
        return luser;
    }

    @Before
    public void initTest() {
        luser = createEntity(em);
    }

    @Test
    @Transactional
    public void createLuser() throws Exception {
        int databaseSizeBeforeCreate = luserRepository.findAll().size();

        // Create the Luser

        restLuserMockMvc.perform(post("/api/lusers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(luser)))
                .andExpect(status().isCreated());

        // Validate the Luser in the database
        List<Luser> lusers = luserRepository.findAll();
        assertThat(lusers).hasSize(databaseSizeBeforeCreate + 1);
        Luser testLuser = lusers.get(lusers.size() - 1);
        assertThat(testLuser.getIdentifiant()).isEqualTo(DEFAULT_IDENTIFIANT);
    }

    @Test
    @Transactional
    public void checkIdentifiantIsRequired() throws Exception {
        int databaseSizeBeforeTest = luserRepository.findAll().size();
        // set the field null
        luser.setIdentifiant(null);

        // Create the Luser, which fails.

        restLuserMockMvc.perform(post("/api/lusers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(luser)))
                .andExpect(status().isBadRequest());

        List<Luser> lusers = luserRepository.findAll();
        assertThat(lusers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLusers() throws Exception {
        // Initialize the database
        luserRepository.saveAndFlush(luser);

        // Get all the lusers
        restLuserMockMvc.perform(get("/api/lusers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(luser.getId().intValue())))
                .andExpect(jsonPath("$.[*].identifiant").value(hasItem(DEFAULT_IDENTIFIANT.toString())));
    }

    @Test
    @Transactional
    public void getLuser() throws Exception {
        // Initialize the database
        luserRepository.saveAndFlush(luser);

        // Get the luser
        restLuserMockMvc.perform(get("/api/lusers/{id}", luser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(luser.getId().intValue()))
            .andExpect(jsonPath("$.identifiant").value(DEFAULT_IDENTIFIANT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLuser() throws Exception {
        // Get the luser
        restLuserMockMvc.perform(get("/api/lusers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLuser() throws Exception {
        // Initialize the database
        luserRepository.saveAndFlush(luser);
        int databaseSizeBeforeUpdate = luserRepository.findAll().size();

        // Update the luser
        Luser updatedLuser = luserRepository.findOne(luser.getId());
        updatedLuser
                .identifiant(UPDATED_IDENTIFIANT);

        restLuserMockMvc.perform(put("/api/lusers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLuser)))
                .andExpect(status().isOk());

        // Validate the Luser in the database
        List<Luser> lusers = luserRepository.findAll();
        assertThat(lusers).hasSize(databaseSizeBeforeUpdate);
        Luser testLuser = lusers.get(lusers.size() - 1);
        assertThat(testLuser.getIdentifiant()).isEqualTo(UPDATED_IDENTIFIANT);
    }

    @Test
    @Transactional
    public void deleteLuser() throws Exception {
        // Initialize the database
        luserRepository.saveAndFlush(luser);
        int databaseSizeBeforeDelete = luserRepository.findAll().size();

        // Get the luser
        restLuserMockMvc.perform(delete("/api/lusers/{id}", luser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Luser> lusers = luserRepository.findAll();
        assertThat(lusers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
