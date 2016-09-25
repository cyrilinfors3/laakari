package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lcallbox;

import com.mycompany.myapp.repository.LcallboxRepository;
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
 * REST controller for managing Lcallbox.
 */
@RestController
@RequestMapping("/api")
public class LcallboxResource {

    private final Logger log = LoggerFactory.getLogger(LcallboxResource.class);
        
    @Inject
    private LcallboxRepository lcallboxRepository;

    /**
     * POST  /lcallboxes : Create a new lcallbox.
     *
     * @param lcallbox the lcallbox to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lcallbox, or with status 400 (Bad Request) if the lcallbox has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lcallboxes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcallbox> createLcallbox(@Valid @RequestBody Lcallbox lcallbox) throws URISyntaxException {
        log.debug("REST request to save Lcallbox : {}", lcallbox);
        if (lcallbox.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lcallbox", "idexists", "A new lcallbox cannot already have an ID")).body(null);
        }
        Lcallbox result = lcallboxRepository.save(lcallbox);
        return ResponseEntity.created(new URI("/api/lcallboxes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lcallbox", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lcallboxes : Updates an existing lcallbox.
     *
     * @param lcallbox the lcallbox to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lcallbox,
     * or with status 400 (Bad Request) if the lcallbox is not valid,
     * or with status 500 (Internal Server Error) if the lcallbox couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lcallboxes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcallbox> updateLcallbox(@Valid @RequestBody Lcallbox lcallbox) throws URISyntaxException {
        log.debug("REST request to update Lcallbox : {}", lcallbox);
        if (lcallbox.getId() == null) {
            return createLcallbox(lcallbox);
        }
        Lcallbox result = lcallboxRepository.save(lcallbox);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lcallbox", lcallbox.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lcallboxes : get all the lcallboxes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lcallboxes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lcallboxes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lcallbox>> getAllLcallboxes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lcallboxes");
        Page<Lcallbox> page = lcallboxRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lcallboxes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lcallboxes/:id : get the "id" lcallbox.
     *
     * @param id the id of the lcallbox to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lcallbox, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lcallboxes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcallbox> getLcallbox(@PathVariable Long id) {
        log.debug("REST request to get Lcallbox : {}", id);
        Lcallbox lcallbox = lcallboxRepository.findOne(id);
        return Optional.ofNullable(lcallbox)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lcallboxes/:id : delete the "id" lcallbox.
     *
     * @param id the id of the lcallbox to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lcallboxes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLcallbox(@PathVariable Long id) {
        log.debug("REST request to delete Lcallbox : {}", id);
        lcallboxRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lcallbox", id.toString())).build();
    }

}
