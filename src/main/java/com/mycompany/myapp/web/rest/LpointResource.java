package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lpoint;

import com.mycompany.myapp.repository.LpointRepository;
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
 * REST controller for managing Lpoint.
 */
@RestController
@RequestMapping("/api")
public class LpointResource {

    private final Logger log = LoggerFactory.getLogger(LpointResource.class);
        
    @Inject
    private LpointRepository lpointRepository;

    /**
     * POST  /lpoints : Create a new lpoint.
     *
     * @param lpoint the lpoint to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lpoint, or with status 400 (Bad Request) if the lpoint has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lpoints",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lpoint> createLpoint(@Valid @RequestBody Lpoint lpoint) throws URISyntaxException {
        log.debug("REST request to save Lpoint : {}", lpoint);
        if (lpoint.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lpoint", "idexists", "A new lpoint cannot already have an ID")).body(null);
        }
        Lpoint result = lpointRepository.save(lpoint);
        return ResponseEntity.created(new URI("/api/lpoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lpoint", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lpoints : Updates an existing lpoint.
     *
     * @param lpoint the lpoint to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lpoint,
     * or with status 400 (Bad Request) if the lpoint is not valid,
     * or with status 500 (Internal Server Error) if the lpoint couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lpoints",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lpoint> updateLpoint(@Valid @RequestBody Lpoint lpoint) throws URISyntaxException {
        log.debug("REST request to update Lpoint : {}", lpoint);
        if (lpoint.getId() == null) {
            return createLpoint(lpoint);
        }
        Lpoint result = lpointRepository.save(lpoint);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lpoint", lpoint.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lpoints : get all the lpoints.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lpoints in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lpoints",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lpoint>> getAllLpoints(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lpoints");
        Page<Lpoint> page = lpointRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lpoints");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lpoints/:id : get the "id" lpoint.
     *
     * @param id the id of the lpoint to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lpoint, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lpoints/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lpoint> getLpoint(@PathVariable Long id) {
        log.debug("REST request to get Lpoint : {}", id);
        Lpoint lpoint = lpointRepository.findOne(id);
        return Optional.ofNullable(lpoint)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lpoints/:id : delete the "id" lpoint.
     *
     * @param id the id of the lpoint to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lpoints/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLpoint(@PathVariable Long id) {
        log.debug("REST request to delete Lpoint : {}", id);
        lpointRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lpoint", id.toString())).build();
    }

}
