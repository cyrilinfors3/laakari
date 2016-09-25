package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lcall;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lcall entity.
 */
@SuppressWarnings("unused")
public interface LcallRepository extends JpaRepository<Lcall,Long> {

}
