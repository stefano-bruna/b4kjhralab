package eu.bugnion.web.rest;

import eu.bugnion.domain.Patent;
import eu.bugnion.repository.PatentRepository;
import eu.bugnion.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link eu.bugnion.domain.Patent}.
 */
@RestController
@RequestMapping("/api")
public class PatentResource {

    private final Logger log = LoggerFactory.getLogger(PatentResource.class);

    private static final String ENTITY_NAME = "patent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatentRepository patentRepository;

    public PatentResource(PatentRepository patentRepository) {
        this.patentRepository = patentRepository;
    }

    /**
     * {@code POST  /patents} : Create a new patent.
     *
     * @param patent the patent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patent, or with status {@code 400 (Bad Request)} if the patent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patents")
    public ResponseEntity<Patent> createPatent(@RequestBody Patent patent) throws URISyntaxException {
        log.debug("REST request to save Patent : {}", patent);
        if (patent.getId() != null) {
            throw new BadRequestAlertException("A new patent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Patent result = patentRepository.save(patent);
        return ResponseEntity.created(new URI("/api/patents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patents} : Updates an existing patent.
     *
     * @param patent the patent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patent,
     * or with status {@code 400 (Bad Request)} if the patent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patents")
    public ResponseEntity<Patent> updatePatent(@RequestBody Patent patent) throws URISyntaxException {
        log.debug("REST request to update Patent : {}", patent);
        if (patent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Patent result = patentRepository.save(patent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /patents} : get all the patents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patents in body.
     */
    @GetMapping("/patents")
    public List<Patent> getAllPatents() {
        log.debug("REST request to get all Patents");
        return patentRepository.findAll();
    }

    /**
     * {@code GET  /patents/:id} : get the "id" patent.
     *
     * @param id the id of the patent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patents/{id}")
    public ResponseEntity<Patent> getPatent(@PathVariable Long id) {
        log.debug("REST request to get Patent : {}", id);
        Optional<Patent> patent = patentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(patent);
    }

    /**
     * {@code DELETE  /patents/:id} : delete the "id" patent.
     *
     * @param id the id of the patent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patents/{id}")
    public ResponseEntity<Void> deletePatent(@PathVariable Long id) {
        log.debug("REST request to delete Patent : {}", id);
        patentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
