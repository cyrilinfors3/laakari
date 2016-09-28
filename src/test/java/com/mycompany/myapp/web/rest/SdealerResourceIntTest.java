package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Sdealer;
import com.mycompany.myapp.repository.SdealerRepository;

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
 * Test class for the SdealerResource REST controller.
 *
 * @see SdealerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class SdealerResourceIntTest {

    private static final String DEFAULT_SHOPCODE = "AAAAA";
    private static final String UPDATED_SHOPCODE = "BBBBB";
    private static final String DEFAULT_SIGLE = "AAAAA";
    private static final String UPDATED_SIGLE = "BBBBB";

    private static final Integer DEFAULT_MASTERSIM = 1;
    private static final Integer UPDATED_MASTERSIM = 2;

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    @Inject
    private SdealerRepository sdealerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSdealerMockMvc;

    private Sdealer sdealer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SdealerResource sdealerResource = new SdealerResource();
        ReflectionTestUtils.setField(sdealerResource, "sdealerRepository", sdealerRepository);
        this.restSdealerMockMvc = MockMvcBuilders.standaloneSetup(sdealerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sdealer createEntity(EntityManager em) {
        Sdealer sdealer = new Sdealer()
                .shopcode(DEFAULT_SHOPCODE)
                .sigle(DEFAULT_SIGLE)
                .mastersim(DEFAULT_MASTERSIM)
                .logo(DEFAULT_LOGO)
                .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return sdealer;
    }

    @Before
    public void initTest() {
        sdealer = createEntity(em);
    }

    @Test
    @Transactional
    public void createSdealer() throws Exception {
        int databaseSizeBeforeCreate = sdealerRepository.findAll().size();

        // Create the Sdealer

        restSdealerMockMvc.perform(post("/api/sdealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sdealer)))
                .andExpect(status().isCreated());

        // Validate the Sdealer in the database
        List<Sdealer> sdealers = sdealerRepository.findAll();
        assertThat(sdealers).hasSize(databaseSizeBeforeCreate + 1);
        Sdealer testSdealer = sdealers.get(sdealers.size() - 1);
        assertThat(testSdealer.getShopcode()).isEqualTo(DEFAULT_SHOPCODE);
        assertThat(testSdealer.getSigle()).isEqualTo(DEFAULT_SIGLE);
        assertThat(testSdealer.getMastersim()).isEqualTo(DEFAULT_MASTERSIM);
        assertThat(testSdealer.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testSdealer.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkShopcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = sdealerRepository.findAll().size();
        // set the field null
        sdealer.setShopcode(null);

        // Create the Sdealer, which fails.

        restSdealerMockMvc.perform(post("/api/sdealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sdealer)))
                .andExpect(status().isBadRequest());

        List<Sdealer> sdealers = sdealerRepository.findAll();
        assertThat(sdealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSigleIsRequired() throws Exception {
        int databaseSizeBeforeTest = sdealerRepository.findAll().size();
        // set the field null
        sdealer.setSigle(null);

        // Create the Sdealer, which fails.

        restSdealerMockMvc.perform(post("/api/sdealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sdealer)))
                .andExpect(status().isBadRequest());

        List<Sdealer> sdealers = sdealerRepository.findAll();
        assertThat(sdealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMastersimIsRequired() throws Exception {
        int databaseSizeBeforeTest = sdealerRepository.findAll().size();
        // set the field null
        sdealer.setMastersim(null);

        // Create the Sdealer, which fails.

        restSdealerMockMvc.perform(post("/api/sdealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sdealer)))
                .andExpect(status().isBadRequest());

        List<Sdealer> sdealers = sdealerRepository.findAll();
        assertThat(sdealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSdealers() throws Exception {
        // Initialize the database
        sdealerRepository.saveAndFlush(sdealer);

        // Get all the sdealers
        restSdealerMockMvc.perform(get("/api/sdealers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sdealer.getId().intValue())))
                .andExpect(jsonPath("$.[*].shopcode").value(hasItem(DEFAULT_SHOPCODE.toString())))
                .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE.toString())))
                .andExpect(jsonPath("$.[*].mastersim").value(hasItem(DEFAULT_MASTERSIM)))
                .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    public void getSdealer() throws Exception {
        // Initialize the database
        sdealerRepository.saveAndFlush(sdealer);

        // Get the sdealer
        restSdealerMockMvc.perform(get("/api/sdealers/{id}", sdealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sdealer.getId().intValue()))
            .andExpect(jsonPath("$.shopcode").value(DEFAULT_SHOPCODE.toString()))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE.toString()))
            .andExpect(jsonPath("$.mastersim").value(DEFAULT_MASTERSIM))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    public void getNonExistingSdealer() throws Exception {
        // Get the sdealer
        restSdealerMockMvc.perform(get("/api/sdealers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSdealer() throws Exception {
        // Initialize the database
        sdealerRepository.saveAndFlush(sdealer);
        int databaseSizeBeforeUpdate = sdealerRepository.findAll().size();

        // Update the sdealer
        Sdealer updatedSdealer = sdealerRepository.findOne(sdealer.getId());
        updatedSdealer
                .shopcode(UPDATED_SHOPCODE)
                .sigle(UPDATED_SIGLE)
                .mastersim(UPDATED_MASTERSIM)
                .logo(UPDATED_LOGO)
                .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restSdealerMockMvc.perform(put("/api/sdealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSdealer)))
                .andExpect(status().isOk());

        // Validate the Sdealer in the database
        List<Sdealer> sdealers = sdealerRepository.findAll();
        assertThat(sdealers).hasSize(databaseSizeBeforeUpdate);
        Sdealer testSdealer = sdealers.get(sdealers.size() - 1);
        assertThat(testSdealer.getShopcode()).isEqualTo(UPDATED_SHOPCODE);
        assertThat(testSdealer.getSigle()).isEqualTo(UPDATED_SIGLE);
        assertThat(testSdealer.getMastersim()).isEqualTo(UPDATED_MASTERSIM);
        assertThat(testSdealer.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testSdealer.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteSdealer() throws Exception {
        // Initialize the database
        sdealerRepository.saveAndFlush(sdealer);
        int databaseSizeBeforeDelete = sdealerRepository.findAll().size();

        // Get the sdealer
        restSdealerMockMvc.perform(delete("/api/sdealers/{id}", sdealer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sdealer> sdealers = sdealerRepository.findAll();
        assertThat(sdealers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
