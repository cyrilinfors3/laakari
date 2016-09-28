package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lfacture;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lfacture entity.
 */
@SuppressWarnings("unused")
public interface LfactureRepository extends JpaRepository<Lfacture,Long> {

}
