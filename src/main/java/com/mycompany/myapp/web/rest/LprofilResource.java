package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Dmanager;
import com.mycompany.myapp.domain.Lprofil;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.LprofilRepository;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.web.rest.vm.ManagedUserVM;

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
 * REST controller for managing Lprofil.
 */
@RestController
@RequestMapping("/api")
public class LprofilResource {

    private final Logger log = LoggerFactory.getLogger(LprofilResource.class);
        
    @Inject
    private LprofilRepository lprofilRepository;

    @Inject
    private DmanagerResource dmanagerResource;
    @Inject
    private UserService userService;
    /**
     * POST  /lprofils : Create a new lprofil.
     *
     * @param lprofil the lprofil to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lprofil, or with status 400 (Bad Request) if the lprofil has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lprofils",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lprofil> createLprofil(@Valid @RequestBody Lprofil lprofil) throws URISyntaxException {
        log.debug("REST request to save Lprofil : {}", lprofil);
        if (lprofil.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lprofil", "idexists", "A new lprofil cannot already have an ID")).body(null);
        }
        Lprofil result = lprofilRepository.save(lprofil);
        return ResponseEntity.created(new URI("/api/lprofils/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lprofil", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lprofils : Updates an existing lprofil.
     *
     * @param lprofil the lprofil to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lprofil,
     * or with status 400 (Bad Request) if the lprofil is not valid,
     * or with status 500 (Internal Server Error) if the lprofil couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lprofils",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lprofil> updateLprofil(@Valid @RequestBody Lprofil lprofil) throws URISyntaxException {
        log.debug("REST request to update Lprofil : {}", lprofil);
        if (lprofil.getId() == null) {
            return createLprofil(lprofil);
        }
        Lprofil result = lprofilRepository.save(lprofil);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lprofil", lprofil.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lprofils : get all the lprofils.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of lprofils in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lprofils",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lprofil>> getAllLprofils(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("delegue-is-null".equals(filter)) {
            log.debug("REST request to get all Lprofils where delegue is null");
            return new ResponseEntity<>(StreamSupport
                .stream(lprofilRepository.findAll().spliterator(), false)
                .filter(lprofil -> lprofil.getDelegue() == null)
                .collect(Collectors.toList()), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Lprofils");
        Page<Lprofil> page = lprofilRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lprofils");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lprofils/:id : get the "id" lprofil.
     *
     * @param id the id of the lprofil to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lprofil, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lprofils/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lprofil> getLprofil(@PathVariable Long id) {
        log.debug("REST request to get Lprofil : {}", id);
        Lprofil lprofil = lprofilRepository.findOne(id);
        return Optional.ofNullable(lprofil)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lprofils/:id : delete the "id" lprofil.
     *
     * @param id the id of the lprofil to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lprofils/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLprofil(@PathVariable Long id) {
        log.debug("REST request to delete Lprofil : {}", id);
        lprofilRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lprofil", id.toString())).build();
    }
    
    /**
     * GET  /lprofils/: langKey,email, region, tel: get the  lprofil created using langKey,email, region, tel .
     *
     * @param langKey,email, region, tel of the lprofil to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lprofil, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lprofils/{langKey}/{email}/{region}/{tel}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    
    public Dmanager getLprofil(@PathVariable String langKey,String email,String region, Integer tel) {
        log.debug("REST request to get a new Lprofil : {}");
        Dmanager dmanager= new  Dmanager();
        dmanager.setAgentcode(region);
        dmanager.setTel(tel);
        try {
			dmanagerResource.createDmanager(dmanager);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//        Dmanager dmanager = dmanagerResource.newDmanagerResource(region, tel);
//        User user = new User();
//        user.setLogin(dmanager.getAgentcode());
//        user.setEmail(email);
//        user.setLangKey(langKey);
//        user.setPassword(dmanager.getAgentcode());
//        User newUser = userService.createUser(new ManagedUserVM(user));
//        Lprofil lprofil= new Lprofil();
//        lprofil.setLogin(newUser.getLogin());
//        lprofil.setEmail(newUser.getEmail());
//        lprofil.setPass(newUser.getPassword());
//        lprofil.setTel(tel);
//        Lprofil result = lprofilRepository.save(lprofil);
		return dmanager;
        
        
//        return Optional.ofNullable(lprofil)
//            .map(result -> new ResponseEntity<>(
//                result,
//                HttpStatus.OK))
//            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
