package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Luser;

import com.mycompany.myapp.repository.LuserRepository;
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
 * REST controller for managing Luser.
 */
@RestController
@RequestMapping("/api")
public class LuserResource {

    private final Logger log = LoggerFactory.getLogger(LuserResource.class);
        
    @Inject
    private LuserRepository luserRepository;

    /**
     * POST  /lusers : Create a new luser.
     *
     * @param luser the luser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new luser, or with status 400 (Bad Request) if the luser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lusers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Luser> createLuser(@Valid @RequestBody Luser luser) throws URISyntaxException {
        log.debug("REST request to save Luser : {}", luser);
        if (luser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("luser", "idexists", "A new luser cannot already have an ID")).body(null);
        }
        Luser result = luserRepository.save(luser);
        return ResponseEntity.created(new URI("/api/lusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("luser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lusers : Updates an existing luser.
     *
     * @param luser the luser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated luser,
     * or with status 400 (Bad Request) if the luser is not valid,
     * or with status 500 (Internal Server Error) if the luser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lusers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Luser> updateLuser(@Valid @RequestBody Luser luser) throws URISyntaxException {
        log.debug("REST request to update Luser : {}", luser);
        if (luser.getId() == null) {
            return createLuser(luser);
        }
        Luser result = luserRepository.save(luser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("luser", luser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lusers : get all the lusers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lusers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lusers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Luser>> getAllLusers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Lusers");
        Page<Luser> page = luserRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lusers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lusers/:id : get the "id" luser.
     *
     * @param id the id of the luser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the luser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lusers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Luser> getLuser(@PathVariable Long id) {
        log.debug("REST request to get Luser : {}", id);
        Luser luser = luserRepository.findOne(id);
        return Optional.ofNullable(luser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lusers/:id : delete the "id" luser.
     *
     * @param id the id of the luser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lusers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLuser(@PathVariable Long id) {
        log.debug("REST request to delete Luser : {}", id);
        luserRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("luser", id.toString())).build();
    }

}
