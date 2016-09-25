package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lroute;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lroute entity.
 */
@SuppressWarnings("unused")
public interface LrouteRepository extends JpaRepository<Lroute,Long> {

}
