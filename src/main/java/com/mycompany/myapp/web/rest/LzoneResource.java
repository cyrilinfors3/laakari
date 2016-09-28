package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Lzone;

import com.mycompany.myapp.repository.LzoneRepository;
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
 * REST controller for managing Lzone.
 */
@RestController
@RequestMapping("/api")
public class LzoneResource {

    private final Logger log = LoggerFactory.getLogger(LzoneResource.class);
        
    @Inject
    private LzoneRepository lzoneRepository;

    /**
     * POST  /lzones : Create a new lzone.
     *
     * @param lzone the lzone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lzone, or with status 400 (Bad Request) if the lzone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lzones",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lzone> createLzone(@Valid @RequestBody Lzone lzone) throws URISyntaxException {
        log.debug("REST request to save Lzone : {}", lzone);
        if (lzone.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lzone", "idexists", "A new lzone cannot already have an ID")).body(null);
        }
        Lzone result = lzoneRepository.save(lzone);
        return ResponseEntity.created(new URI("/api/lzones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lzone", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lzones : Updates an existing lzone.
     *
     * @param lzone the lzone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lzone,
     * or with status 400 (Bad Request) if the lzone is not valid,
     * or with status 500 (Internal Server Error) if the lzone couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lzones",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lzone> updateLzone(@Valid @RequestBody Lzone lzone) throws URISyntaxException {
        log.debug("REST request to update Lzone : {}", lzone);
        if (lzone.getId() == null) {
            return createLzone(lzone);
        }
        Lzone result = lzoneRepository.save(lzone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lzone", lzone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lzones : get all the lzones.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of lzones in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lzones",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lzone>> getAllLzones(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("luser-is-null".equals(filter)) {
            log.debug("REST request to get all Lzones where luser is null");
            return new ResponseEntity<>(StreamSupport
                .stream(lzoneRepository.findAll().spliterator(), false)
                .filter(lzone -> lzone.getLuser() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Lzones");
        Page<Lzone> page = lzoneRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lzones");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lzones/:id : get the "id" lzone.
     *
     * @param id the id of the lzone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lzone, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lzones/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lzone> getLzone(@PathVariable Long id) {
        log.debug("REST request to get Lzone : {}", id);
        Lzone lzone = lzoneRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(lzone)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lzones/:id : delete the "id" lzone.
     *
     * @param id the id of the lzone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lzones/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLzone(@PathVariable Long id) {
        log.debug("REST request to delete Lzone : {}", id);
        lzoneRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lzone", id.toString())).build();
    }

}
