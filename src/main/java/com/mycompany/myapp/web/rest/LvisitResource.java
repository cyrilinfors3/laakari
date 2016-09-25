package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lvisit;

import com.mycompany.myapp.repository.LvisitRepository;
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
 * REST controller for managing Lvisit.
 */
@RestController
@RequestMapping("/api")
public class LvisitResource {

    private final Logger log = LoggerFactory.getLogger(LvisitResource.class);
        
    @Inject
    private LvisitRepository lvisitRepository;

    /**
     * POST  /lvisits : Create a new lvisit.
     *
     * @param lvisit the lvisit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lvisit, or with status 400 (Bad Request) if the lvisit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lvisits",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lvisit> createLvisit(@Valid @RequestBody Lvisit lvisit) throws URISyntaxException {
        log.debug("REST request to save Lvisit : {}", lvisit);
        if (lvisit.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lvisit", "idexists", "A new lvisit cannot already have an ID")).body(null);
        }
        Lvisit result = lvisitRepository.save(lvisit);
        return ResponseEntity.created(new URI("/api/lvisits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lvisit", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lvisits : Updates an existing lvisit.
     *
     * @param lvisit the lvisit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lvisit,
     * or with status 400 (Bad Request) if the lvisit is not valid,
     * or with status 500 (Internal Server Error) if the lvisit couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lvisits",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lvisit> updateLvisit(@Valid @RequestBody Lvisit lvisit) throws URISyntaxException {
        log.debug("REST request to update Lvisit : {}", lvisit);
        if (lvisit.getId() == null) {
            return createLvisit(lvisit);
        }
        Lvisit result = lvisitRepository.save(lvisit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lvisit", lvisit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lvisits : get all the lvisits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lvisits in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lvisits",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lvisit>> getAllLvisits(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lvisits");
        Page<Lvisit> page = lvisitRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lvisits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lvisits/:id : get the "id" lvisit.
     *
     * @param id the id of the lvisit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lvisit, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lvisits/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lvisit> getLvisit(@PathVariable Long id) {
        log.debug("REST request to get Lvisit : {}", id);
        Lvisit lvisit = lvisitRepository.findOne(id);
        return Optional.ofNullable(lvisit)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lvisits/:id : delete the "id" lvisit.
     *
     * @param id the id of the lvisit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lvisits/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLvisit(@PathVariable Long id) {
        log.debug("REST request to delete Lvisit : {}", id);
        lvisitRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lvisit", id.toString())).build();
    }

}
