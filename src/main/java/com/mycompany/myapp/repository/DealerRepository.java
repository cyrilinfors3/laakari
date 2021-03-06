package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dealer;
import com.mycompany.myapp.domain.Dmanager;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dealer entity.
 */
@SuppressWarnings("unused")
public interface DealerRepository extends JpaRepository<Dealer,Long> {

	List<Dealer> findByShopcodeContaining(String reg);

}
