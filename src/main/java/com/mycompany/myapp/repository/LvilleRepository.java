package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lville;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lville entity.
 */
@SuppressWarnings("unused")
public interface LvilleRepository extends JpaRepository<Lville,Long> {

}
