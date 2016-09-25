package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Dmanager;

import com.mycompany.myapp.repository.DmanagerRepository;
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
 * REST controller for managing Dmanager.
 */
@RestController
@RequestMapping("/api")
public class DmanagerResource {

    private final Logger log = LoggerFactory.getLogger(DmanagerResource.class);
        
    @Inject
    private DmanagerRepository dmanagerRepository;

    /**
     * POST  /dmanagers : Create a new dmanager.
     *
     * @param dmanager the dmanager to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dmanager, or with status 400 (Bad Request) if the dmanager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dmanagers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dmanager> createDmanager(@Valid @RequestBody Dmanager dmanager) throws URISyntaxException {
        log.debug("REST request to save Dmanager : {}", dmanager);
        if (dmanager.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dmanager", "idexists", "A new dmanager cannot already have an ID")).body(null);
        }
        Dmanager result = dmanagerRepository.save(dmanager);
        return ResponseEntity.created(new URI("/api/dmanagers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dmanager", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dmanagers : Updates an existing dmanager.
     *
     * @param dmanager the dmanager to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dmanager,
     * or with status 400 (Bad Request) if the dmanager is not valid,
     * or with status 500 (Internal Server Error) if the dmanager couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dmanagers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dmanager> updateDmanager(@Valid @RequestBody Dmanager dmanager) throws URISyntaxException {
        log.debug("REST request to update Dmanager : {}", dmanager);
        if (dmanager.getId() == null) {
            return createDmanager(dmanager);
        }
        Dmanager result = dmanagerRepository.save(dmanager);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dmanager", dmanager.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dmanagers : get all the dmanagers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dmanagers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/dmanagers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dmanager>> getAllDmanagers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dmanagers");
        Page<Dmanager> page = dmanagerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dmanagers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dmanagers/:id : get the "id" dmanager.
     *
     * @param id the id of the dmanager to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dmanager, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dmanagers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dmanager> getDmanager(@PathVariable Long id) {
        log.debug("REST request to get Dmanager : {}", id);
        Dmanager dmanager = dmanagerRepository.findOne(id);
        return Optional.ofNullable(dmanager)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dmanagers/:id : delete the "id" dmanager.
     *
     * @param id the id of the dmanager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dmanagers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDmanager(@PathVariable Long id) {
        log.debug("REST request to delete Dmanager : {}", id);
        dmanagerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dmanager", id.toString())).build();
    }

}
