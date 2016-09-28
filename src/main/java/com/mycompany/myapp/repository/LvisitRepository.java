package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lvisit;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lvisit entity.
 */
@SuppressWarnings("unused")
public interface LvisitRepository extends JpaRepository<Lvisit,Long> {

}
