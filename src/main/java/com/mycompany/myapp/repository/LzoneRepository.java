package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Lzone;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Lzone entity.
 */
@SuppressWarnings("unused")
public interface LzoneRepository extends JpaRepository<Lzone,Long> {

    @Query("select distinct lzone from Lzone lzone left join fetch lzone.larrondissements left join fetch lzone.lvilles")
    List<Lzone> findAllWithEagerRelationships();

    @Query("select lzone from Lzone lzone left join fetch lzone.larrondissements left join fetch lzone.lvilles where lzone.id =:id")
    Lzone findOneWithEagerRelationships(@Param("id") Long id);

}
