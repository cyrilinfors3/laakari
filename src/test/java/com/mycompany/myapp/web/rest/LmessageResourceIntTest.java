package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.LaakariApp;

import com.mycompany.myapp.domain.Lmessage;
import com.mycompany.myapp.repository.LmessageRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LmessageResource REST controller.
 *
 * @see LmessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaakariApp.class)
public class LmessageResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final LocalDate DEFAULT_SENTDATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SENTDATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_MSGCONTENT = "AAAAA";
    private static final String UPDATED_MSGCONTENT = "BBBBB";

    private static final ZonedDateTime DEFAULT_SENTHOURS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SENTHOURS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SENTHOURS_STR = dateTimeFormatter.format(DEFAULT_SENTHOURS);
    private static final String DEFAULT_SENDER = "AAAAA";
    private static final String UPDATED_SENDER = "BBBBB";
    private static final String DEFAULT_RECIEVER = "AAAAA";
    private static final String UPDATED_RECIEVER = "BBBBB";

    @Inject
    private LmessageRepository lmessageRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLmessageMockMvc;

    private Lmessage lmessage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LmessageResource lmessageResource = new LmessageResource();
        ReflectionTestUtils.setField(lmessageResource, "lmessageRepository", lmessageRepository);
        this.restLmessageMockMvc = MockMvcBuilders.standaloneSetup(lmessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lmessage createEntity(EntityManager em) {
        Lmessage lmessage = new Lmessage()
                .sentdate(DEFAULT_SENTDATE)
                .msgcontent(DEFAULT_MSGCONTENT)
                .senthours(DEFAULT_SENTHOURS)
                .sender(DEFAULT_SENDER)
                .reciever(DEFAULT_RECIEVER);
        return lmessage;
    }

    @Before
    public void initTest() {
        lmessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createLmessage() throws Exception {
        int databaseSizeBeforeCreate = lmessageRepository.findAll().size();

        // Create the Lmessage

        restLmessageMockMvc.perform(post("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lmessage)))
                .andExpect(status().isCreated());

        // Validate the Lmessage in the database
        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeCreate + 1);
        Lmessage testLmessage = lmessages.get(lmessages.size() - 1);
        assertThat(testLmessage.getSentdate()).isEqualTo(DEFAULT_SENTDATE);
        assertThat(testLmessage.getMsgcontent()).isEqualTo(DEFAULT_MSGCONTENT);
        assertThat(testLmessage.getSenthours()).isEqualTo(DEFAULT_SENTHOURS);
        assertThat(testLmessage.getSender()).isEqualTo(DEFAULT_SENDER);
        assertThat(testLmessage.getReciever()).isEqualTo(DEFAULT_RECIEVER);
    }

    @Test
    @Transactional
    public void checkSentdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lmessageRepository.findAll().size();
        // set the field null
        lmessage.setSentdate(null);

        // Create the Lmessage, which fails.

        restLmessageMockMvc.perform(post("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lmessage)))
                .andExpect(status().isBadRequest());

        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMsgcontentIsRequired() throws Exception {
        int databaseSizeBeforeTest = lmessageRepository.findAll().size();
        // set the field null
        lmessage.setMsgcontent(null);

        // Create the Lmessage, which fails.

        restLmessageMockMvc.perform(post("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lmessage)))
                .andExpect(status().isBadRequest());

        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenthoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = lmessageRepository.findAll().size();
        // set the field null
        lmessage.setSenthours(null);

        // Create the Lmessage, which fails.

        restLmessageMockMvc.perform(post("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lmessage)))
                .andExpect(status().isBadRequest());

        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = lmessageRepository.findAll().size();
        // set the field null
        lmessage.setSender(null);

        // Create the Lmessage, which fails.

        restLmessageMockMvc.perform(post("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lmessage)))
                .andExpect(status().isBadRequest());

        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRecieverIsRequired() throws Exception {
        int databaseSizeBeforeTest = lmessageRepository.findAll().size();
        // set the field null
        lmessage.setReciever(null);

        // Create the Lmessage, which fails.

        restLmessageMockMvc.perform(post("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lmessage)))
                .andExpect(status().isBadRequest());

        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLmessages() throws Exception {
        // Initialize the database
        lmessageRepository.saveAndFlush(lmessage);

        // Get all the lmessages
        restLmessageMockMvc.perform(get("/api/lmessages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lmessage.getId().intValue())))
                .andExpect(jsonPath("$.[*].sentdate").value(hasItem(DEFAULT_SENTDATE.toString())))
                .andExpect(jsonPath("$.[*].msgcontent").value(hasItem(DEFAULT_MSGCONTENT.toString())))
                .andExpect(jsonPath("$.[*].senthours").value(hasItem(DEFAULT_SENTHOURS_STR)))
                .andExpect(jsonPath("$.[*].sender").value(hasItem(DEFAULT_SENDER.toString())))
                .andExpect(jsonPath("$.[*].reciever").value(hasItem(DEFAULT_RECIEVER.toString())));
    }

    @Test
    @Transactional
    public void getLmessage() throws Exception {
        // Initialize the database
        lmessageRepository.saveAndFlush(lmessage);

        // Get the lmessage
        restLmessageMockMvc.perform(get("/api/lmessages/{id}", lmessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lmessage.getId().intValue()))
            .andExpect(jsonPath("$.sentdate").value(DEFAULT_SENTDATE.toString()))
            .andExpect(jsonPath("$.msgcontent").value(DEFAULT_MSGCONTENT.toString()))
            .andExpect(jsonPath("$.senthours").value(DEFAULT_SENTHOURS_STR))
            .andExpect(jsonPath("$.sender").value(DEFAULT_SENDER.toString()))
            .andExpect(jsonPath("$.reciever").value(DEFAULT_RECIEVER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLmessage() throws Exception {
        // Get the lmessage
        restLmessageMockMvc.perform(get("/api/lmessages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLmessage() throws Exception {
        // Initialize the database
        lmessageRepository.saveAndFlush(lmessage);
        int databaseSizeBeforeUpdate = lmessageRepository.findAll().size();

        // Update the lmessage
        Lmessage updatedLmessage = lmessageRepository.findOne(lmessage.getId());
        updatedLmessage
                .sentdate(UPDATED_SENTDATE)
                .msgcontent(UPDATED_MSGCONTENT)
                .senthours(UPDATED_SENTHOURS)
                .sender(UPDATED_SENDER)
                .reciever(UPDATED_RECIEVER);

        restLmessageMockMvc.perform(put("/api/lmessages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLmessage)))
                .andExpect(status().isOk());

        // Validate the Lmessage in the database
        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeUpdate);
        Lmessage testLmessage = lmessages.get(lmessages.size() - 1);
        assertThat(testLmessage.getSentdate()).isEqualTo(UPDATED_SENTDATE);
        assertThat(testLmessage.getMsgcontent()).isEqualTo(UPDATED_MSGCONTENT);
        assertThat(testLmessage.getSenthours()).isEqualTo(UPDATED_SENTHOURS);
        assertThat(testLmessage.getSender()).isEqualTo(UPDATED_SENDER);
        assertThat(testLmessage.getReciever()).isEqualTo(UPDATED_RECIEVER);
    }

    @Test
    @Transactional
    public void deleteLmessage() throws Exception {
        // Initialize the database
        lmessageRepository.saveAndFlush(lmessage);
        int databaseSizeBeforeDelete = lmessageRepository.findAll().size();

        // Get the lmessage
        restLmessageMockMvc.perform(delete("/api/lmessages/{id}", lmessage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Lmessage> lmessages = lmessageRepository.findAll();
        assertThat(lmessages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
