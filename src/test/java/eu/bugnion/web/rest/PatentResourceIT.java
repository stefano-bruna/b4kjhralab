package eu.bugnion.web.rest;

import eu.bugnion.B4KjhralabApp;
import eu.bugnion.domain.Patent;
import eu.bugnion.repository.PatentRepository;
import eu.bugnion.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static eu.bugnion.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatentResource} REST controller.
 */
@SpringBootTest(classes = B4KjhralabApp.class)
public class PatentResourceIT {

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private PatentRepository patentRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPatentMockMvc;

    private Patent patent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PatentResource patentResource = new PatentResource(patentRepository);
        this.restPatentMockMvc = MockMvcBuilders.standaloneSetup(patentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patent createEntity(EntityManager em) {
        Patent patent = new Patent()
            .uid(DEFAULT_UID)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return patent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patent createUpdatedEntity(EntityManager em) {
        Patent patent = new Patent()
            .uid(UPDATED_UID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return patent;
    }

    @BeforeEach
    public void initTest() {
        patent = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatent() throws Exception {
        int databaseSizeBeforeCreate = patentRepository.findAll().size();

        // Create the Patent
        restPatentMockMvc.perform(post("/api/patents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patent)))
            .andExpect(status().isCreated());

        // Validate the Patent in the database
        List<Patent> patentList = patentRepository.findAll();
        assertThat(patentList).hasSize(databaseSizeBeforeCreate + 1);
        Patent testPatent = patentList.get(patentList.size() - 1);
        assertThat(testPatent.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testPatent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createPatentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patentRepository.findAll().size();

        // Create the Patent with an existing ID
        patent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatentMockMvc.perform(post("/api/patents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patent)))
            .andExpect(status().isBadRequest());

        // Validate the Patent in the database
        List<Patent> patentList = patentRepository.findAll();
        assertThat(patentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPatents() throws Exception {
        // Initialize the database
        patentRepository.saveAndFlush(patent);

        // Get all the patentList
        restPatentMockMvc.perform(get("/api/patents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patent.getId().intValue())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getPatent() throws Exception {
        // Initialize the database
        patentRepository.saveAndFlush(patent);

        // Get the patent
        restPatentMockMvc.perform(get("/api/patents/{id}", patent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(patent.getId().intValue()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPatent() throws Exception {
        // Get the patent
        restPatentMockMvc.perform(get("/api/patents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatent() throws Exception {
        // Initialize the database
        patentRepository.saveAndFlush(patent);

        int databaseSizeBeforeUpdate = patentRepository.findAll().size();

        // Update the patent
        Patent updatedPatent = patentRepository.findById(patent.getId()).get();
        // Disconnect from session so that the updates on updatedPatent are not directly saved in db
        em.detach(updatedPatent);
        updatedPatent
            .uid(UPDATED_UID)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restPatentMockMvc.perform(put("/api/patents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatent)))
            .andExpect(status().isOk());

        // Validate the Patent in the database
        List<Patent> patentList = patentRepository.findAll();
        assertThat(patentList).hasSize(databaseSizeBeforeUpdate);
        Patent testPatent = patentList.get(patentList.size() - 1);
        assertThat(testPatent.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testPatent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingPatent() throws Exception {
        int databaseSizeBeforeUpdate = patentRepository.findAll().size();

        // Create the Patent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatentMockMvc.perform(put("/api/patents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(patent)))
            .andExpect(status().isBadRequest());

        // Validate the Patent in the database
        List<Patent> patentList = patentRepository.findAll();
        assertThat(patentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatent() throws Exception {
        // Initialize the database
        patentRepository.saveAndFlush(patent);

        int databaseSizeBeforeDelete = patentRepository.findAll().size();

        // Delete the patent
        restPatentMockMvc.perform(delete("/api/patents/{id}", patent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patent> patentList = patentRepository.findAll();
        assertThat(patentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Patent.class);
        Patent patent1 = new Patent();
        patent1.setId(1L);
        Patent patent2 = new Patent();
        patent2.setId(patent1.getId());
        assertThat(patent1).isEqualTo(patent2);
        patent2.setId(2L);
        assertThat(patent1).isNotEqualTo(patent2);
        patent1.setId(null);
        assertThat(patent1).isNotEqualTo(patent2);
    }
}
