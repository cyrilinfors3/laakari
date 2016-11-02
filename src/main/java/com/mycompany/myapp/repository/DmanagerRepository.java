package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Dmanager;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Dmanager entity.
 */
@SuppressWarnings("unused")
public interface DmanagerRepository extends JpaRepository<Dmanager,Long> {
	List<Dmanager> findByAgentcodeContaining(String reg);

	Dmanager findById(Long id);
}
