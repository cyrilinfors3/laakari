package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lprofil;
import com.mycompany.myapp.repository.LprofilRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LprofilResource REST controller.
 *
 * @see LprofilResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LprofilResourceIntTest {

    private static final String DEFAULT_LOGIN = "AAAAA";
    private static final String UPDATED_LOGIN = "BBBBB";
    private static final String DEFAULT_PASS = "AAAAA";
    private static final String UPDATED_PASS = "BBBBB";

    private static final Integer DEFAULT_TEL = 1;
    private static final Integer UPDATED_TEL = 2;

    private static final byte[] DEFAULT_PIC = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PIC = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PIC_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PIC_CONTENT_TYPE = "image/png";

    @Inject
    private LprofilRepository lprofilRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLprofilMockMvc;

    private Lprofil lprofil;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LprofilResource lprofilResource = new LprofilResource();
        ReflectionTestUtils.setField(lprofilResource, "lprofilRepository", lprofilRepository);
        this.restLprofilMockMvc = MockMvcBuilders.standaloneSetup(lprofilResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lprofil createEntity(EntityManager em) {
        Lprofil lprofil = new Lprofil()
                .login(DEFAULT_LOGIN)
                .pass(DEFAULT_PASS)
                .tel(DEFAULT_TEL)
                .pic(DEFAULT_PIC)
                .picContentType(DEFAULT_PIC_CONTENT_TYPE);
        return lprofil;
    }

    @Before
    public void initTest() {
        lprofil = createEntity(em);
    }

    @Test
    @Transactional
    public void createLprofil() throws Exception {
        int databaseSizeBeforeCreate = lprofilRepository.findAll().size();

        // Create the Lprofil

        restLprofilMockMvc.perform(post("/api/lprofils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lprofil)))
                .andExpect(status().isCreated());

        // Validate the Lprofil in the database
        List<Lprofil> lprofils = lprofilRepository.findAll();
        assertThat(lprofils).hasSize(databaseSizeBeforeCreate + 1);
        Lprofil testLprofil = lprofils.get(lprofils.size() - 1);
        assertThat(testLprofil.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testLprofil.getPass()).isEqualTo(DEFAULT_PASS);
        assertThat(testLprofil.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testLprofil.getPic()).isEqualTo(DEFAULT_PIC);
        assertThat(testLprofil.getPicContentType()).isEqualTo(DEFAULT_PIC_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = lprofilRepository.findAll().size();
        // set the field null
        lprofil.setLogin(null);

        // Create the Lprofil, which fails.

        restLprofilMockMvc.perform(post("/api/lprofils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lprofil)))
                .andExpect(status().isBadRequest());

        List<Lprofil> lprofils = lprofilRepository.findAll();
        assertThat(lprofils).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPassIsRequired() throws Exception {
        int databaseSizeBeforeTest = lprofilRepository.findAll().size();
        // set the field null
        lprofil.setPass(null);

        // Create the Lprofil, which fails.

        restLprofilMockMvc.perform(post("/api/lprofils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lprofil)))
                .andExpect(status().isBadRequest());

        List<Lprofil> lprofils = lprofilRepository.findAll();
        assertThat(lprofils).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = lprofilRepository.findAll().size();
        // set the field null
        lprofil.setTel(null);

        // Create the Lprofil, which fails.

        restLprofilMockMvc.perform(post("/api/lprofils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lprofil)))
                .andExpect(status().isBadRequest());

        List<Lprofil> lprofils = lprofilRepository.findAll();
        assertThat(lprofils).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLprofils() throws Exception {
        // Initialize the database
        lprofilRepository.saveAndFlush(lprofil);

        // Get all the lprofils
        restLprofilMockMvc.perform(get("/api/lprofils?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lprofil.getId().intValue())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].pass").value(hasItem(DEFAULT_PASS.toString())))
                .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
                .andExpect(jsonPath("$.[*].picContentType").value(hasItem(DEFAULT_PIC_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].pic").value(hasItem(Base64Utils.encodeToString(DEFAULT_PIC))));
    }

    @Test
    @Transactional
    public void getLprofil() throws Exception {
        // Initialize the database
        lprofilRepository.saveAndFlush(lprofil);

        // Get the lprofil
        restLprofilMockMvc.perform(get("/api/lprofils/{id}", lprofil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lprofil.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.pass").value(DEFAULT_PASS.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.picContentType").value(DEFAULT_PIC_CONTENT_TYPE))
            .andExpect(jsonPath("$.pic").value(Base64Utils.encodeToString(DEFAULT_PIC)));
    }

    @Test
    @Transactional
    public void getNonExistingLprofil() throws Exception {
        // Get the lprofil
        restLprofilMockMvc.perform(get("/api/lprofils/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLprofil() throws Exception {
        // Initialize the database
        lprofilRepository.saveAndFlush(lprofil);
        int databaseSizeBeforeUpdate = lprofilRepository.findAll().size();

        // Update the lprofil
        Lprofil updatedLprofil = lprofilRepository.findOne(lprofil.getId());
        updatedLprofil
                .login(UPDATED_LOGIN)
                .pass(UPDATED_PASS)
                .tel(UPDATED_TEL)
                .pic(UPDATED_PIC)
                .picContentType(UPDATED_PIC_CONTENT_TYPE);

        restLprofilMockMvc.perform(put("/api/lprofils")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLprofil)))
                .andExpect(status().isOk());

        // Validate the Lprofil in the database
        List<Lprofil> lprofils = lprofilRepository.findAll();
        assertThat(lprofils).hasSize(databaseSizeBeforeUpdate);
        Lprofil testLprofil = lprofils.get(lprofils.size() - 1);
        assertThat(testLprofil.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testLprofil.getPass()).isEqualTo(UPDATED_PASS);
        assertThat(testLprofil.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testLprofil.getPic()).isEqualTo(UPDATED_PIC);
        assertThat(testLprofil.getPicContentType()).isEqualTo(UPDATED_PIC_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteLprofil() throws Exception {
        // Initialize the database
        lprofilRepository.saveAndFlush(lprofil);
        int databaseSizeBeforeDelete = lprofilRepository.findAll().size();

        // Get the lprofil
        restLprofilMockMvc.perform(delete("/api/lprofils/{id}", lprofil.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lprofil> lprofils = lprofilRepository.findAll();
        assertThat(lprofils).hasSize(databaseSizeBeforeDelete - 1);
    }
}
