package com.tfsc.ilabs.selfservice.entity.repositories;

import com.tfsc.ilabs.selfservice.entity.models.EntityIdLookup;
import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author Sushil.Kumar
 */
@RepositoryRestResource
public interface EntityIdLookupRepository extends JpaRepository<EntityIdLookup, Integer> {

    EntityIdLookup findByEntityTemplateAndTestEntityId(EntityTemplate entityTemplate, String testEntityId);
    EntityIdLookup findByTestEntityId(String testEntityId);
}
