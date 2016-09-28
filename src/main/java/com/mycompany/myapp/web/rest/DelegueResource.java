package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Delegue;

import com.mycompany.myapp.repository.DelegueRepository;
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
 * REST controller for managing Delegue.
 */
@RestController
@RequestMapping("/api")
public class DelegueResource {

    private final Logger log = LoggerFactory.getLogger(DelegueResource.class);
        
    @Inject
    private DelegueRepository delegueRepository;

    /**
     * POST  /delegues : Create a new delegue.
     *
     * @param delegue the delegue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new delegue, or with status 400 (Bad Request) if the delegue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/delegues",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delegue> createDelegue(@Valid @RequestBody Delegue delegue) throws URISyntaxException {
        log.debug("REST request to save Delegue : {}", delegue);
        if (delegue.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("delegue", "idexists", "A new delegue cannot already have an ID")).body(null);
        }
        Delegue result = delegueRepository.save(delegue);
        return ResponseEntity.created(new URI("/api/delegues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("delegue", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /delegues : Updates an existing delegue.
     *
     * @param delegue the delegue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated delegue,
     * or with status 400 (Bad Request) if the delegue is not valid,
     * or with status 500 (Internal Server Error) if the delegue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/delegues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delegue> updateDelegue(@Valid @RequestBody Delegue delegue) throws URISyntaxException {
        log.debug("REST request to update Delegue : {}", delegue);
        if (delegue.getId() == null) {
            return createDelegue(delegue);
        }
        Delegue result = delegueRepository.save(delegue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("delegue", delegue.getId().toString()))
            .body(result);
    }

    /**
     * GET  /delegues : get all the delegues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of delegues in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/delegues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Delegue>> getAllDelegues(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Delegues");
        Page<Delegue> page = delegueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/delegues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /delegues/:id : get the "id" delegue.
     *
     * @param id the id of the delegue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the delegue, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/delegues/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Delegue> getDelegue(@PathVariable Long id) {
        log.debug("REST request to get Delegue : {}", id);
        Delegue delegue = delegueRepository.findOne(id);
        return Optional.ofNullable(delegue)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /delegues/:id : delete the "id" delegue.
     *
     * @param id the id of the delegue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/delegues/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDelegue(@PathVariable Long id) {
        log.debug("REST request to delete Delegue : {}", id);
        delegueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("delegue", id.toString())).build();
    }

}
