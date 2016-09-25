package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lprofil;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lprofil entity.
 */
@SuppressWarnings("unused")
public interface LprofilRepository extends JpaRepository<Lprofil,Long> {

}
