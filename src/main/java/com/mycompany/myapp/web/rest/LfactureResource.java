package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lfacture;

import com.mycompany.myapp.repository.LfactureRepository;
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
 * REST controller for managing Lfacture.
 */
@RestController
@RequestMapping("/api")
public class LfactureResource {

    private final Logger log = LoggerFactory.getLogger(LfactureResource.class);
        
    @Inject
    private LfactureRepository lfactureRepository;

    /**
     * POST  /lfactures : Create a new lfacture.
     *
     * @param lfacture the lfacture to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lfacture, or with status 400 (Bad Request) if the lfacture has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lfactures",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lfacture> createLfacture(@Valid @RequestBody Lfacture lfacture) throws URISyntaxException {
        log.debug("REST request to save Lfacture : {}", lfacture);
        if (lfacture.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lfacture", "idexists", "A new lfacture cannot already have an ID")).body(null);
        }
        Lfacture result = lfactureRepository.save(lfacture);
        return ResponseEntity.created(new URI("/api/lfactures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lfacture", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lfactures : Updates an existing lfacture.
     *
     * @param lfacture the lfacture to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lfacture,
     * or with status 400 (Bad Request) if the lfacture is not valid,
     * or with status 500 (Internal Server Error) if the lfacture couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lfactures",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lfacture> updateLfacture(@Valid @RequestBody Lfacture lfacture) throws URISyntaxException {
        log.debug("REST request to update Lfacture : {}", lfacture);
        if (lfacture.getId() == null) {
            return createLfacture(lfacture);
        }
        Lfacture result = lfactureRepository.save(lfacture);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lfacture", lfacture.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lfactures : get all the lfactures.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lfactures in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lfactures",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lfacture>> getAllLfactures(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lfactures");
        Page<Lfacture> page = lfactureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lfactures");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lfactures/:id : get the "id" lfacture.
     *
     * @param id the id of the lfacture to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lfacture, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lfactures/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lfacture> getLfacture(@PathVariable Long id) {
        log.debug("REST request to get Lfacture : {}", id);
        Lfacture lfacture = lfactureRepository.findOne(id);
        return Optional.ofNullable(lfacture)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lfactures/:id : delete the "id" lfacture.
     *
     * @param id the id of the lfacture to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lfactures/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLfacture(@PathVariable Long id) {
        log.debug("REST request to delete Lfacture : {}", id);
        lfactureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lfacture", id.toString())).build();
    }

}
