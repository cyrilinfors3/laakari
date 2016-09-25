package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Delegue;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Delegue entity.
 */
@SuppressWarnings("unused")
public interface DelegueRepository extends JpaRepository<Delegue,Long> {

}
