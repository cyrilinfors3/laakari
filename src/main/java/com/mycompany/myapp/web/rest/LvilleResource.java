package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lville;

import com.mycompany.myapp.repository.LvilleRepository;
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
 * REST controller for managing Lville.
 */
@RestController
@RequestMapping("/api")
public class LvilleResource {

    private final Logger log = LoggerFactory.getLogger(LvilleResource.class);
        
    @Inject
    private LvilleRepository lvilleRepository;

    /**
     * POST  /lvilles : Create a new lville.
     *
     * @param lville the lville to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lville, or with status 400 (Bad Request) if the lville has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lvilles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lville> createLville(@Valid @RequestBody Lville lville) throws URISyntaxException {
        log.debug("REST request to save Lville : {}", lville);
        if (lville.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lville", "idexists", "A new lville cannot already have an ID")).body(null);
        }
        Lville result = lvilleRepository.save(lville);
        return ResponseEntity.created(new URI("/api/lvilles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lville", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lvilles : Updates an existing lville.
     *
     * @param lville the lville to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lville,
     * or with status 400 (Bad Request) if the lville is not valid,
     * or with status 500 (Internal Server Error) if the lville couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lvilles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lville> updateLville(@Valid @RequestBody Lville lville) throws URISyntaxException {
        log.debug("REST request to update Lville : {}", lville);
        if (lville.getId() == null) {
            return createLville(lville);
        }
        Lville result = lvilleRepository.save(lville);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lville", lville.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lvilles : get all the lvilles.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lvilles in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lvilles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lville>> getAllLvilles(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lvilles");
        Page<Lville> page = lvilleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lvilles");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lvilles/:id : get the "id" lville.
     *
     * @param id the id of the lville to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lville, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lvilles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lville> getLville(@PathVariable Long id) {
        log.debug("REST request to get Lville : {}", id);
        Lville lville = lvilleRepository.findOne(id);
        return Optional.ofNullable(lville)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lvilles/:id : delete the "id" lville.
     *
     * @param id the id of the lville to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lvilles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLville(@PathVariable Long id) {
        log.debug("REST request to delete Lville : {}", id);
        lvilleRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lville", id.toString())).build();
    }

}
