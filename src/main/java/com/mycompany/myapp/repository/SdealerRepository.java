package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Sdealer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sdealer entity.
 */
@SuppressWarnings("unused")
public interface SdealerRepository extends JpaRepository<Sdealer,Long> {

}
