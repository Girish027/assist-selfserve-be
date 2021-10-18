package com.tfsc.ilabs.selfservice.page.repositories;

import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource()
public interface PageTemplateRepository extends JpaRepository<PageTemplate, String> {
}