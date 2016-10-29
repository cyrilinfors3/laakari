package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Dealer;
import com.mycompany.myapp.domain.Dmanager;
import com.mycompany.myapp.repository.DealerRepository;
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
 * REST controller for managing Dealer.
 */
@RestController
@RequestMapping("/api")
public class DealerResource {

    private final Logger log = LoggerFactory.getLogger(DealerResource.class);
        
    @Inject
    private DealerRepository dealerRepository;

    /**
     * POST  /dealers : Create a new dealer.
     *
     * @param dealer the dealer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dealer, or with status 400 (Bad Request) if the dealer has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dealers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dealer> createDealer(@Valid @RequestBody Dealer dealer) throws URISyntaxException {
        log.debug("REST request to save Dealer : {}", dealer);
        if (dealer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dealer", "idexists", "A new dealer cannot already have an ID")).body(null);
        }
        dealer.setShopcode(this.getMaxDealerRangByregion(dealer.getShopcode())); 
        Dealer result = dealerRepository.save(dealer);
        
        return ResponseEntity.created(new URI("/api/dealers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dealer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dealers : Updates an existing dealer.
     *
     * @param dealer the dealer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dealer,
     * or with status 400 (Bad Request) if the dealer is not valid,
     * or with status 500 (Internal Server Error) if the dealer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/dealers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dealer> updateDealer(@Valid @RequestBody Dealer dealer) throws URISyntaxException {
        log.debug("REST request to update Dealer : {}", dealer);
        if (dealer.getId() == null) {
            return createDealer(dealer);
        }
        Dealer result = dealerRepository.save(dealer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dealer", dealer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dealers : get all the dealers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dealers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/dealers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Dealer>> getAllDealers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Dealers");
        Page<Dealer> page = dealerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dealers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dealers/:id : get the "id" dealer.
     *
     * @param id the id of the dealer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dealer, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/dealers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dealer> getDealer(@PathVariable Long id) {
        log.debug("REST request to get Dealer : {}", id);
        Dealer dealer = dealerRepository.findOne(id);
       
        return Optional.ofNullable(dealer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dealers/:id : delete the "id" dealer.
     *
     * @param id the id of the dealer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/dealers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id) {
        log.debug("REST request to delete Dealer : {}", id);
     
        dealerRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dealer", id.toString())).build();
    }
 
    /**
     * GET  /dmrang:reg : get max  dmanagers rang by region and produce code.
     */
    @RequestMapping(value = "/drang/{reg}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
        @Timed
        public String getMaxDealerRangByregion(@PathVariable String reg)
             {
            log.debug("REST request to get max Dmanagers rang by region");
            List<Dealer> listD = dealerRepository.findByShopcodeContaining(reg);
            String code1="0";
            String result=reg+"0001";
            if (!listD.isEmpty()) {
            	Dealer dealer = (listD.get(0).getId() <= listD.get(listD.size()-1).getId()) ? listD.get(listD.size()-1) : listD.get(0);
            	if(dealer .getShopcode().length()>3){
            		code1 =dealer.getShopcode().substring(3, dealer.getShopcode().length());
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
}
