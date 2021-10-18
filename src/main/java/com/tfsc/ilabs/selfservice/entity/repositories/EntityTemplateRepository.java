package com.tfsc.ilabs.selfservice.entity.repositories;

import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EntityTemplateRepository extends JpaRepository<EntityTemplate, Integer> {
}
