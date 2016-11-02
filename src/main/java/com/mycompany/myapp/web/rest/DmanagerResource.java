package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Dmanager;
import com.mycompany.myapp.domain.Lprofil;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.DmanagerRepository;
import com.mycompany.myapp.repository.LprofilRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.MailService;
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
import javax.transaction.Transactional;
import javax.validation.Valid;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Dmanager.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DmanagerResource {

    private final Logger log = LoggerFactory.getLogger(DmanagerResource.class);
        
    @Inject
    private DmanagerRepository dmanagerRepository;
    
    @Inject
    private LprofilRepository lprofilRepository;
    
    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Inject
    private UserService userService;

    /**
     * POST  /dmanagers : Create a new dmanager.
     *
     * @param dmanager the dmanager to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dmanager, or with status 400 (Bad Request) if the dmanager has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dmanagers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed

    public ResponseEntity<Dmanager> createDmanager(@Valid @RequestBody Dmanager dmanager) throws URISyntaxException {
        log.debug("REST request to save Dmanager : {}", dmanager);
        if (dmanager.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dmanager", "idexists", "A new dmanager cannot already have an ID")).body(null);
        }
        dmanager.setAgentcode(this.getMaxDmanagersRangByregion(dmanager.getAgentcode())); 
        Dmanager result = dmanagerRepository.save(dmanager);
        //Lprofil p = newLprofil();
       // log.debug("REST request to save lprofil : {}", p);
        //this.updateDmanager(result);
        return ResponseEntity.created(new URI("/api/dmanagers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dmanager", result.getId().toString()))
            .body(result);
    }
    /**
     * POST  /dmanagers : Create a new dmanagerf test.
     *
     */
    
    @RequestMapping(value = "/dmanagersfull/{tel}/{region}/{email}/{langKey}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Transactional(dontRollbackOn=Exception.class)
        public ResponseEntity<Dmanager> createDmanagertest(@PathVariable Integer tel,@PathVariable String region ,@PathVariable String email,@PathVariable String langKey) throws URISyntaxException {
    	Dmanager dmanager=new Dmanager();
    	
    	log.debug("REST request to save Dmanager : {}", dmanager);
            if (dmanager.getId() != null) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dmanager", "idexists", "A new dmanager cannot already have an ID")).body(null);
            }
            dmanager.setAgentcode(this.getMaxDmanagersRangByregion(region));
            dmanager.setTel(tel);
            Dmanager result = dmanagerRepository.save(dmanager);
            Lprofil p = newLprofil(email);
            p.setDmanager(dmanager);
            String r=this.creatUser(dmanager.getAgentcode(), email, tel+"", true, langKey);
            if(!r.isEmpty())
            {
            	lprofilRepository.delete(p);
            	dmanagerRepository.delete(result);
            	
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", "idexists", r)).body(null);
            }
            log.debug("REST request to save lprofil : {}", p);
            //this.updateDmanager(result);
            return ResponseEntity.created(new URI("/api/dmanagers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dmanager", result.getId().toString()))
                .body(result);
        }
    
    @RequestMapping(value = "/dmanagerssc/{id}/{tel}/{region}/{email}/{langKey}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        @Transactional(dontRollbackOn=Exception.class)
        public ResponseEntity<Lprofil> createDmanagertestsc(@PathVariable Long id,@PathVariable Integer tel,@PathVariable String region ,@PathVariable String email,@PathVariable String langKey) throws URISyntaxException {
    	Dmanager dmanager=dmanagerRepository.findById(id);
    	
    	log.debug("REST request to save Dmanager : {}", dmanager);
            if (dmanager== null) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dmanager", "dmanager not exists", "no dmanager ")).body(null);
            }
            Lprofil p = newLprofil(email);
            p.setDmanager(dmanager);
            String r=this.creatUser(dmanager.getAgentcode()+"souscompt", email, tel+"", true, langKey);
            if(!r.isEmpty())
            {
            	lprofilRepository.delete(p);
            	
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", "idexists", r)).body(null);
            }
            log.debug("REST request to save lprofil : {}", p);
            //this.updateDmanager(result);
            return ResponseEntity.created(new URI("/api/dmanagers/" + p.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dmanager", p.getId().toString()))
                .body(p);
        }

    /**
     * PUT  /dmanagers : Updates an existing dmanager.
     *
     * @param dmanager the dmanager to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dmanager,
     * or with status 400 (Bad Request) if the dmanager is not valid,
     * or with status 500 (Internal Server Error) if the dmanager couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dmanagers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dmanager> updateDmanager(@Valid @RequestBody Dmanager dmanager) throws URISyntaxException {
        log.debug("REST request to update Dmanager : {}", dmanager);
        if (dmanager.getId() == null) {
            return createDmanager(dmanager);
        }
        Dmanager result = dmanagerRepository.save(dmanager);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dmanager", dmanager.getId().toString()))
            .body(result);
    }
    
    

    /**
     * GET  /dmanagers : get all the dmanagers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dmanagers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/dmanagers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dmanager>> getAllDmanagers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dmanagers");
        Page<Dmanager> page = dmanagerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dmanagers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    
    
    /**
     * GET  /dmanagers/:id : get the "id" dmanager.
     *
     * @param id the id of the dmanager to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dmanager, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dmanagers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dmanager> getDmanager(@PathVariable Long id) {
        log.debug("REST request to get Dmanager : {}", id);
        Dmanager dmanager = dmanagerRepository.findOne(id);
        return Optional.ofNullable(dmanager)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dmanagers/:id : delete the "id" dmanager.
     *
     * @param id the id of the dmanager to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dmanagers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDmanager(@PathVariable Long id) {
        log.debug("REST request to delete Dmanager : {}", id);
        dmanagerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dmanager", id.toString())).build();
    }
    
    /**
     * GET  /dmrang:reg : get max  dmanagers rang by region and produce code.
     */
    @RequestMapping(value = "/dmrang/{reg}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public String getMaxDmanagersRangByregion(@PathVariable String reg)
             {
            log.debug("REST request to get max Dmanagers rang by region");
            List<Dmanager> listDM = dmanagerRepository.findByAgentcodeContaining(reg);
            String code1="0";
            String result=reg+"0001";
            if (!listDM.isEmpty()) {
            	Dmanager dmanager = (listDM.get(0).getId() <= listDM.get(listDM.size()-1).getId()) ? listDM.get(listDM.size()-1) : listDM.get(0);
            	if(dmanager.getAgentcode().length()>3){
            		code1 =dmanager.getAgentcode().substring(3, dmanager.getAgentcode().length());
            		int code=Integer.valueOf(code1)+1;
            		
            		result=reg+chiffre(code);
            	}
            	
            }
            return result;
        }
    private String chiffre(int code) {
		// TODO Auto-generated method stub
    	String oo="";
    	if(code<10){
    		oo="000";
    	}else if(code<100)
    	{
    		oo="00";
    	}else if(code<1000)
    	{
    		oo="0";
    	}
		return oo+code;
	}

	/**
     * create new DM and return it ID
     */
    @Timed
    public Dmanager newDmanagerResource(String region, Integer tel){
    	List<Dmanager> listDm=dmanagerRepository.findAll();
    	Dmanager dmanager= new Dmanager();
    	//long rang=this.getMaxDmanagersRangByregion(region)+1;
    	dmanager.setAgentcode(region+" vv");
    	dmanager.setId(listDm.get(listDm.size()-1).getId()+1);
    	dmanager.setTel(tel);
    	 Dmanager result = dmanagerRepository.save(dmanager);
		return result;
    	
    }
    @Timed
    public Lprofil newLprofil(String email){
    	Lprofil p=new Lprofil();
    	p.setEmail(email);
    	p.setLogin("ooooooooooo");
    	p.setPass("dddddddd");
    	p.setTel(22222222);
    	 Lprofil result = lprofilRepository.save(p);
		return result;
    	
    }
    
    public String creatUser(String login,String email,String password,boolean b,String langKey){
        log.debug("REST request to save new generated User : {}");

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(login.toLowerCase()).isPresent()) {
        	log.debug("ERROR User login Exit alredy");
            return "ERROR User login Exit alredy";
        } else if (userRepository.findOneByEmail(email).isPresent()) {
        	log.debug("ERROR User Email Exit alredy");
            return "ERROR User Email Exit alredy";
       } else {
    	   User user=new User();
    	   user.setActivated(b);user.setEmail(email);user.setLangKey(langKey);user.setLogin(login);
    	   user.setPassword(password);
    	   ManagedUserVM muserVM = new ManagedUserVM(user);
    	   
            User newUser = userService.createUser(muserVM);
            return "";
        }
    }
}
