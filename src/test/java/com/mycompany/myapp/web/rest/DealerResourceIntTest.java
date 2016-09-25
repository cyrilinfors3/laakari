package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Dealer;
import com.mycompany.myapp.repository.DealerRepository;

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
 * Test class for the DealerResource REST controller.
 *
 * @see DealerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class DealerResourceIntTest {

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
    private DealerRepository dealerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DealerResource dealerResource = new DealerResource();
        ReflectionTestUtils.setField(dealerResource, "dealerRepository", dealerRepository);
        this.restDealerMockMvc = MockMvcBuilders.standaloneSetup(dealerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dealer createEntity(EntityManager em) {
        Dealer dealer = new Dealer()
                .shopcode(DEFAULT_SHOPCODE)
                .sigle(DEFAULT_SIGLE)
                .mastersim(DEFAULT_MASTERSIM)
                .logo(DEFAULT_LOGO)
                .logoContentType(DEFAULT_LOGO_CONTENT_TYPE);
        return dealer;
    }

    @Before
    public void initTest() {
        dealer = createEntity(em);
    }

    @Test
    @Transactional
    public void createDealer() throws Exception {
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        // Create the Dealer

        restDealerMockMvc.perform(post("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isCreated());

        // Validate the Dealer in the database
        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeCreate + 1);
        Dealer testDealer = dealers.get(dealers.size() - 1);
        assertThat(testDealer.getShopcode()).isEqualTo(DEFAULT_SHOPCODE);
        assertThat(testDealer.getSigle()).isEqualTo(DEFAULT_SIGLE);
        assertThat(testDealer.getMastersim()).isEqualTo(DEFAULT_MASTERSIM);
        assertThat(testDealer.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testDealer.getLogoContentType()).isEqualTo(DEFAULT_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void checkShopcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setShopcode(null);

        // Create the Dealer, which fails.

        restDealerMockMvc.perform(post("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isBadRequest());

        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSigleIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setSigle(null);

        // Create the Dealer, which fails.

        restDealerMockMvc.perform(post("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isBadRequest());

        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMastersimIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerRepository.findAll().size();
        // set the field null
        dealer.setMastersim(null);

        // Create the Dealer, which fails.

        restDealerMockMvc.perform(post("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dealer)))
                .andExpect(status().isBadRequest());

        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDealers() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealers
        restDealerMockMvc.perform(get("/api/dealers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
                .andExpect(jsonPath("$.[*].shopcode").value(hasItem(DEFAULT_SHOPCODE.toString())))
                .andExpect(jsonPath("$.[*].sigle").value(hasItem(DEFAULT_SIGLE.toString())))
                .andExpect(jsonPath("$.[*].mastersim").value(hasItem(DEFAULT_MASTERSIM)))
                .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64Utils.encodeToString(DEFAULT_LOGO))));
    }

    @Test
    @Transactional
    public void getDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);

        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dealer.getId().intValue()))
            .andExpect(jsonPath("$.shopcode").value(DEFAULT_SHOPCODE.toString()))
            .andExpect(jsonPath("$.sigle").value(DEFAULT_SIGLE.toString()))
            .andExpect(jsonPath("$.mastersim").value(DEFAULT_MASTERSIM))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64Utils.encodeToString(DEFAULT_LOGO)));
    }

    @Test
    @Transactional
    public void getNonExistingDealer() throws Exception {
        // Get the dealer
        restDealerMockMvc.perform(get("/api/dealers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        int databaseSizeBeforeUpdate = dealerRepository.findAll().size();

        // Update the dealer
        Dealer updatedDealer = dealerRepository.findOne(dealer.getId());
        updatedDealer
                .shopcode(UPDATED_SHOPCODE)
                .sigle(UPDATED_SIGLE)
                .mastersim(UPDATED_MASTERSIM)
                .logo(UPDATED_LOGO)
                .logoContentType(UPDATED_LOGO_CONTENT_TYPE);

        restDealerMockMvc.perform(put("/api/dealers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDealer)))
                .andExpect(status().isOk());

        // Validate the Dealer in the database
        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeUpdate);
        Dealer testDealer = dealers.get(dealers.size() - 1);
        assertThat(testDealer.getShopcode()).isEqualTo(UPDATED_SHOPCODE);
        assertThat(testDealer.getSigle()).isEqualTo(UPDATED_SIGLE);
        assertThat(testDealer.getMastersim()).isEqualTo(UPDATED_MASTERSIM);
        assertThat(testDealer.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testDealer.getLogoContentType()).isEqualTo(UPDATED_LOGO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteDealer() throws Exception {
        // Initialize the database
        dealerRepository.saveAndFlush(dealer);
        int databaseSizeBeforeDelete = dealerRepository.findAll().size();

        // Get the dealer
        restDealerMockMvc.perform(delete("/api/dealers/{id}", dealer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dealer> dealers = dealerRepository.findAll();
        assertThat(dealers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
