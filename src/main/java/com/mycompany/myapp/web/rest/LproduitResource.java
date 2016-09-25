package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lproduit;

import com.mycompany.myapp.repository.LproduitRepository;
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
 * REST controller for managing Lproduit.
 */
@RestController
@RequestMapping("/api")
public class LproduitResource {

    private final Logger log = LoggerFactory.getLogger(LproduitResource.class);
        
    @Inject
    private LproduitRepository lproduitRepository;

    /**
     * POST  /lproduits : Create a new lproduit.
     *
     * @param lproduit the lproduit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lproduit, or with status 400 (Bad Request) if the lproduit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lproduits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lproduit> createLproduit(@Valid @RequestBody Lproduit lproduit) throws URISyntaxException {
        log.debug("REST request to save Lproduit : {}", lproduit);
        if (lproduit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lproduit", "idexists", "A new lproduit cannot already have an ID")).body(null);
        }
        Lproduit result = lproduitRepository.save(lproduit);
        return ResponseEntity.created(new URI("/api/lproduits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lproduit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lproduits : Updates an existing lproduit.
     *
     * @param lproduit the lproduit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lproduit,
     * or with status 400 (Bad Request) if the lproduit is not valid,
     * or with status 500 (Internal Server Error) if the lproduit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lproduits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lproduit> updateLproduit(@Valid @RequestBody Lproduit lproduit) throws URISyntaxException {
        log.debug("REST request to update Lproduit : {}", lproduit);
        if (lproduit.getId() == null) {
            return createLproduit(lproduit);
        }
        Lproduit result = lproduitRepository.save(lproduit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lproduit", lproduit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lproduits : get all the lproduits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lproduits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lproduits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lproduit>> getAllLproduits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lproduits");
        Page<Lproduit> page = lproduitRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lproduits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lproduits/:id : get the "id" lproduit.
     *
     * @param id the id of the lproduit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lproduit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lproduits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lproduit> getLproduit(@PathVariable Long id) {
        log.debug("REST request to get Lproduit : {}", id);
        Lproduit lproduit = lproduitRepository.findOne(id);
        return Optional.ofNullable(lproduit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lproduits/:id : delete the "id" lproduit.
     *
     * @param id the id of the lproduit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lproduits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLproduit(@PathVariable Long id) {
        log.debug("REST request to delete Lproduit : {}", id);
        lproduitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lproduit", id.toString())).build();
    }

}
