package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lcall;

import com.mycompany.myapp.repository.LcallRepository;
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
 * REST controller for managing Lcall.
 */
@RestController
@RequestMapping("/api")
public class LcallResource {

    private final Logger log = LoggerFactory.getLogger(LcallResource.class);
        
    @Inject
    private LcallRepository lcallRepository;

    /**
     * POST  /lcalls : Create a new lcall.
     *
     * @param lcall the lcall to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lcall, or with status 400 (Bad Request) if the lcall has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lcalls",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcall> createLcall(@Valid @RequestBody Lcall lcall) throws URISyntaxException {
        log.debug("REST request to save Lcall : {}", lcall);
        if (lcall.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lcall", "idexists", "A new lcall cannot already have an ID")).body(null);
        }
        Lcall result = lcallRepository.save(lcall);
        return ResponseEntity.created(new URI("/api/lcalls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lcall", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lcalls : Updates an existing lcall.
     *
     * @param lcall the lcall to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lcall,
     * or with status 400 (Bad Request) if the lcall is not valid,
     * or with status 500 (Internal Server Error) if the lcall couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lcalls",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcall> updateLcall(@Valid @RequestBody Lcall lcall) throws URISyntaxException {
        log.debug("REST request to update Lcall : {}", lcall);
        if (lcall.getId() == null) {
            return createLcall(lcall);
        }
        Lcall result = lcallRepository.save(lcall);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lcall", lcall.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lcalls : get all the lcalls.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lcalls in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lcalls",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lcall>> getAllLcalls(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lcalls");
        Page<Lcall> page = lcallRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lcalls");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lcalls/:id : get the "id" lcall.
     *
     * @param id the id of the lcall to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lcall, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lcalls/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcall> getLcall(@PathVariable Long id) {
        log.debug("REST request to get Lcall : {}", id);
        Lcall lcall = lcallRepository.findOne(id);
        return Optional.ofNullable(lcall)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lcalls/:id : delete the "id" lcall.
     *
     * @param id the id of the lcall to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lcalls/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLcall(@PathVariable Long id) {
        log.debug("REST request to delete Lcall : {}", id);
        lcallRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lcall", id.toString())).build();
    }

}
