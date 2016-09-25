package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lroute;

import com.mycompany.myapp.repository.LrouteRepository;
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
 * REST controller for managing Lroute.
 */
@RestController
@RequestMapping("/api")
public class LrouteResource {

    private final Logger log = LoggerFactory.getLogger(LrouteResource.class);
        
    @Inject
    private LrouteRepository lrouteRepository;

    /**
     * POST  /lroutes : Create a new lroute.
     *
     * @param lroute the lroute to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lroute, or with status 400 (Bad Request) if the lroute has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lroutes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lroute> createLroute(@Valid @RequestBody Lroute lroute) throws URISyntaxException {
        log.debug("REST request to save Lroute : {}", lroute);
        if (lroute.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lroute", "idexists", "A new lroute cannot already have an ID")).body(null);
        }
        Lroute result = lrouteRepository.save(lroute);
        return ResponseEntity.created(new URI("/api/lroutes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lroute", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lroutes : Updates an existing lroute.
     *
     * @param lroute the lroute to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lroute,
     * or with status 400 (Bad Request) if the lroute is not valid,
     * or with status 500 (Internal Server Error) if the lroute couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lroutes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lroute> updateLroute(@Valid @RequestBody Lroute lroute) throws URISyntaxException {
        log.debug("REST request to update Lroute : {}", lroute);
        if (lroute.getId() == null) {
            return createLroute(lroute);
        }
        Lroute result = lrouteRepository.save(lroute);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lroute", lroute.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lroutes : get all the lroutes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lroutes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lroutes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lroute>> getAllLroutes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lroutes");
        Page<Lroute> page = lrouteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lroutes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lroutes/:id : get the "id" lroute.
     *
     * @param id the id of the lroute to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lroute, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lroutes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lroute> getLroute(@PathVariable Long id) {
        log.debug("REST request to get Lroute : {}", id);
        Lroute lroute = lrouteRepository.findOne(id);
        return Optional.ofNullable(lroute)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lroutes/:id : delete the "id" lroute.
     *
     * @param id the id of the lroute to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lroutes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLroute(@PathVariable Long id) {
        log.debug("REST request to delete Lroute : {}", id);
        lrouteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lroute", id.toString())).build();
    }

}
