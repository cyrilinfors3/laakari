package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lcarte;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Lcarte entity.
 */
@SuppressWarnings("unused")
public interface LcarteRepository extends JpaRepository<Lcarte,Long> {

    @Query("select distinct lcarte from Lcarte lcarte left join fetch lcarte.lpoints")
    List<Lcarte> findAllWithEagerRelationships();

    @Query("select lcarte from Lcarte lcarte left join fetch lcarte.lpoints where lcarte.id =:id")
    Lcarte findOneWithEagerRelationships(@Param("id") Long id);

}
