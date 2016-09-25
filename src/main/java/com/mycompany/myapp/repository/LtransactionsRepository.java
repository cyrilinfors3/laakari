package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Ltransactions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ltransactions entity.
 */
@SuppressWarnings("unused")
public interface LtransactionsRepository extends JpaRepository<Ltransactions,Long> {

}
