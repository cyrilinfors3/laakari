package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lmessage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lmessage entity.
 */
@SuppressWarnings("unused")
public interface LmessageRepository extends JpaRepository<Lmessage,Long> {

}
