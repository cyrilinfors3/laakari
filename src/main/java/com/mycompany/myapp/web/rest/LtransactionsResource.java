package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Ltransactions;

import com.mycompany.myapp.repository.LtransactionsRepository;
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
 * REST controller for managing Ltransactions.
 */
@RestController
@RequestMapping("/api")
public class LtransactionsResource {

    private final Logger log = LoggerFactory.getLogger(LtransactionsResource.class);
        
    @Inject
    private LtransactionsRepository ltransactionsRepository;

    /**
     * POST  /ltransactions : Create a new ltransactions.
     *
     * @param ltransactions the ltransactions to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ltransactions, or with status 400 (Bad Request) if the ltransactions has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ltransactions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ltransactions> createLtransactions(@Valid @RequestBody Ltransactions ltransactions) throws URISyntaxException {
        log.debug("REST request to save Ltransactions : {}", ltransactions);
        if (ltransactions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ltransactions", "idexists", "A new ltransactions cannot already have an ID")).body(null);
        }
        Ltransactions result = ltransactionsRepository.save(ltransactions);
        return ResponseEntity.created(new URI("/api/ltransactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ltransactions", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ltransactions : Updates an existing ltransactions.
     *
     * @param ltransactions the ltransactions to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ltransactions,
     * or with status 400 (Bad Request) if the ltransactions is not valid,
     * or with status 500 (Internal Server Error) if the ltransactions couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ltransactions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ltransactions> updateLtransactions(@Valid @RequestBody Ltransactions ltransactions) throws URISyntaxException {
        log.debug("REST request to update Ltransactions : {}", ltransactions);
        if (ltransactions.getId() == null) {
            return createLtransactions(ltransactions);
        }
        Ltransactions result = ltransactionsRepository.save(ltransactions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ltransactions", ltransactions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ltransactions : get all the ltransactions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ltransactions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ltransactions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ltransactions>> getAllLtransactions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ltransactions");
        Page<Ltransactions> page = ltransactionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ltransactions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ltransactions/:id : get the "id" ltransactions.
     *
     * @param id the id of the ltransactions to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ltransactions, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ltransactions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ltransactions> getLtransactions(@PathVariable Long id) {
        log.debug("REST request to get Ltransactions : {}", id);
        Ltransactions ltransactions = ltransactionsRepository.findOne(id);
        return Optional.ofNullable(ltransactions)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ltransactions/:id : delete the "id" ltransactions.
     *
     * @param id the id of the ltransactions to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ltransactions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLtransactions(@PathVariable Long id) {
        log.debug("REST request to delete Ltransactions : {}", id);
        ltransactionsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ltransactions", id.toString())).build();
    }

}
