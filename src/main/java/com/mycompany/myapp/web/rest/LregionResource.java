package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lregion;

import com.mycompany.myapp.repository.LregionRepository;
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
 * REST controller for managing Lregion.
 */
@RestController
@RequestMapping("/api")
public class LregionResource {

    private final Logger log = LoggerFactory.getLogger(LregionResource.class);
        
    @Inject
    private LregionRepository lregionRepository;

    /**
     * POST  /lregions : Create a new lregion.
     *
     * @param lregion the lregion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lregion, or with status 400 (Bad Request) if the lregion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lregions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lregion> createLregion(@Valid @RequestBody Lregion lregion) throws URISyntaxException {
        log.debug("REST request to save Lregion : {}", lregion);
        if (lregion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lregion", "idexists", "A new lregion cannot already have an ID")).body(null);
        }
        Lregion result = lregionRepository.save(lregion);
        return ResponseEntity.created(new URI("/api/lregions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lregion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lregions : Updates an existing lregion.
     *
     * @param lregion the lregion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lregion,
     * or with status 400 (Bad Request) if the lregion is not valid,
     * or with status 500 (Internal Server Error) if the lregion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lregions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lregion> updateLregion(@Valid @RequestBody Lregion lregion) throws URISyntaxException {
        log.debug("REST request to update Lregion : {}", lregion);
        if (lregion.getId() == null) {
            return createLregion(lregion);
        }
        Lregion result = lregionRepository.save(lregion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lregion", lregion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lregions : get all the lregions.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of lregions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lregions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lregion>> getAllLregions(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("lzone-is-null".equals(filter)) {
            log.debug("REST request to get all Lregions where lzone is null");
            return new ResponseEntity<>(StreamSupport
                .stream(lregionRepository.findAll().spliterator(), false)
                .filter(lregion -> lregion.getLzone() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Lregions");
        Page<Lregion> page = lregionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lregions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lregions/:id : get the "id" lregion.
     *
     * @param id the id of the lregion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lregion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lregions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lregion> getLregion(@PathVariable Long id) {
        log.debug("REST request to get Lregion : {}", id);
        Lregion lregion = lregionRepository.findOne(id);
        return Optional.ofNullable(lregion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lregions/:id : delete the "id" lregion.
     *
     * @param id the id of the lregion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lregions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLregion(@PathVariable Long id) {
        log.debug("REST request to delete Lregion : {}", id);
        lregionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lregion", id.toString())).build();
    }

}
