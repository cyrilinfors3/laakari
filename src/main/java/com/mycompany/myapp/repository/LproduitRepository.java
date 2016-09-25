package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lproduit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lproduit entity.
 */
@SuppressWarnings("unused")
public interface LproduitRepository extends JpaRepository<Lproduit,Long> {

}
