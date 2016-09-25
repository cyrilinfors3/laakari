package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lcarte;

import com.mycompany.myapp.repository.LcarteRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Lcarte.
 */
@RestController
@RequestMapping("/api")
public class LcarteResource {

    private final Logger log = LoggerFactory.getLogger(LcarteResource.class);
        
    @Inject
    private LcarteRepository lcarteRepository;

    /**
     * POST  /lcartes : Create a new lcarte.
     *
     * @param lcarte the lcarte to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lcarte, or with status 400 (Bad Request) if the lcarte has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lcartes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcarte> createLcarte(@Valid @RequestBody Lcarte lcarte) throws URISyntaxException {
        log.debug("REST request to save Lcarte : {}", lcarte);
        if (lcarte.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lcarte", "idexists", "A new lcarte cannot already have an ID")).body(null);
        }
        Lcarte result = lcarteRepository.save(lcarte);
        return ResponseEntity.created(new URI("/api/lcartes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lcarte", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lcartes : Updates an existing lcarte.
     *
     * @param lcarte the lcarte to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lcarte,
     * or with status 400 (Bad Request) if the lcarte is not valid,
     * or with status 500 (Internal Server Error) if the lcarte couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lcartes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcarte> updateLcarte(@Valid @RequestBody Lcarte lcarte) throws URISyntaxException {
        log.debug("REST request to update Lcarte : {}", lcarte);
        if (lcarte.getId() == null) {
            return createLcarte(lcarte);
        }
        Lcarte result = lcarteRepository.save(lcarte);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lcarte", lcarte.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lcartes : get all the lcartes.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of lcartes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lcartes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lcarte>> getAllLcartes(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("lzone-is-null".equals(filter)) {
            log.debug("REST request to get all Lcartes where lzone is null");
            return new ResponseEntity<>(StreamSupport
                .stream(lcarteRepository.findAll().spliterator(), false)
                .filter(lcarte -> lcarte.getLzone() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Lcartes");
        Page<Lcarte> page = lcarteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lcartes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lcartes/:id : get the "id" lcarte.
     *
     * @param id the id of the lcarte to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lcarte, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lcartes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lcarte> getLcarte(@PathVariable Long id) {
        log.debug("REST request to get Lcarte : {}", id);
        Lcarte lcarte = lcarteRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(lcarte)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lcartes/:id : delete the "id" lcarte.
     *
     * @param id the id of the lcarte to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lcartes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLcarte(@PathVariable Long id) {
        log.debug("REST request to delete Lcarte : {}", id);
        lcarteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lcarte", id.toString())).build();
    }

}
