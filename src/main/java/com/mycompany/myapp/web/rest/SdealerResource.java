package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Sdealer;

import com.mycompany.myapp.repository.SdealerRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Sdealer.
 */
@RestController
@RequestMapping("/api")
public class SdealerResource {

    private final Logger log = LoggerFactory.getLogger(SdealerResource.class);
        
    @Inject
    private SdealerRepository sdealerRepository;

    /**
     * POST  /sdealers : Create a new sdealer.
     *
     * @param sdealer the sdealer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new sdealer, or with status 400 (Bad Request) if the sdealer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sdealers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sdealer> createSdealer(@Valid @RequestBody Sdealer sdealer) throws URISyntaxException {
        log.debug("REST request to save Sdealer : {}", sdealer);
        if (sdealer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("sdealer", "idexists", "A new sdealer cannot already have an ID")).body(null);
        }
        Sdealer result = sdealerRepository.save(sdealer);
        return ResponseEntity.created(new URI("/api/sdealers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("sdealer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sdealers : Updates an existing sdealer.
     *
     * @param sdealer the sdealer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sdealer,
     * or with status 400 (Bad Request) if the sdealer is not valid,
     * or with status 500 (Internal Server Error) if the sdealer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/sdealers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sdealer> updateSdealer(@Valid @RequestBody Sdealer sdealer) throws URISyntaxException {
        log.debug("REST request to update Sdealer : {}", sdealer);
        if (sdealer.getId() == null) {
            return createSdealer(sdealer);
        }
        Sdealer result = sdealerRepository.save(sdealer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("sdealer", sdealer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sdealers : get all the sdealers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of sdealers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/sdealers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sdealer>> getAllSdealers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Sdealers");
        Page<Sdealer> page = sdealerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sdealers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sdealers/:id : get the "id" sdealer.
     *
     * @param id the id of the sdealer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sdealer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/sdealers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sdealer> getSdealer(@PathVariable Long id) {
        log.debug("REST request to get Sdealer : {}", id);
        Sdealer sdealer = sdealerRepository.findOne(id);
        return Optional.ofNullable(sdealer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /sdealers/:id : delete the "id" sdealer.
     *
     * @param id the id of the sdealer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/sdealers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSdealer(@PathVariable Long id) {
        log.debug("REST request to delete Sdealer : {}", id);
        sdealerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sdealer", id.toString())).build();
    }

}
