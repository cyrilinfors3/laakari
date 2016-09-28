package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lpoint;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lpoint entity.
 */
@SuppressWarnings("unused")
public interface LpointRepository extends JpaRepository<Lpoint,Long> {

}
