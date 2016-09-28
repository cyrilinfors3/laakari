package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Luser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Luser entity.
 */
@SuppressWarnings("unused")
public interface LuserRepository extends JpaRepository<Luser,Long> {

}
