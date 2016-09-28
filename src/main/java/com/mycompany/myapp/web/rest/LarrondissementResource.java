package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Larrondissement;

import com.mycompany.myapp.repository.LarrondissementRepository;
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
 * REST controller for managing Larrondissement.
 */
@RestController
@RequestMapping("/api")
public class LarrondissementResource {

    private final Logger log = LoggerFactory.getLogger(LarrondissementResource.class);
        
    @Inject
    private LarrondissementRepository larrondissementRepository;

    /**
     * POST  /larrondissements : Create a new larrondissement.
     *
     * @param larrondissement the larrondissement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new larrondissement, or with status 400 (Bad Request) if the larrondissement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/larrondissements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Larrondissement> createLarrondissement(@Valid @RequestBody Larrondissement larrondissement) throws URISyntaxException {
        log.debug("REST request to save Larrondissement : {}", larrondissement);
        if (larrondissement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("larrondissement", "idexists", "A new larrondissement cannot already have an ID")).body(null);
        }
        Larrondissement result = larrondissementRepository.save(larrondissement);
        return ResponseEntity.created(new URI("/api/larrondissements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("larrondissement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /larrondissements : Updates an existing larrondissement.
     *
     * @param larrondissement the larrondissement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated larrondissement,
     * or with status 400 (Bad Request) if the larrondissement is not valid,
     * or with status 500 (Internal Server Error) if the larrondissement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/larrondissements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Larrondissement> updateLarrondissement(@Valid @RequestBody Larrondissement larrondissement) throws URISyntaxException {
        log.debug("REST request to update Larrondissement : {}", larrondissement);
        if (larrondissement.getId() == null) {
            return createLarrondissement(larrondissement);
        }
        Larrondissement result = larrondissementRepository.save(larrondissement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("larrondissement", larrondissement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /larrondissements : get all the larrondissements.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of larrondissements in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/larrondissements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Larrondissement>> getAllLarrondissements(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Larrondissements");
        Page<Larrondissement> page = larrondissementRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/larrondissements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /larrondissements/:id : get the "id" larrondissement.
     *
     * @param id the id of the larrondissement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the larrondissement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/larrondissements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Larrondissement> getLarrondissement(@PathVariable Long id) {
        log.debug("REST request to get Larrondissement : {}", id);
        Larrondissement larrondissement = larrondissementRepository.findOne(id);
        return Optional.ofNullable(larrondissement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /larrondissements/:id : delete the "id" larrondissement.
     *
     * @param id the id of the larrondissement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/larrondissements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLarrondissement(@PathVariable Long id) {
        log.debug("REST request to delete Larrondissement : {}", id);
        larrondissementRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("larrondissement", id.toString())).build();
    }

}
