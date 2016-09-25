package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lcallbox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lcallbox entity.
 */
@SuppressWarnings("unused")
public interface LcallboxRepository extends JpaRepository<Lcallbox,Long> {

}
