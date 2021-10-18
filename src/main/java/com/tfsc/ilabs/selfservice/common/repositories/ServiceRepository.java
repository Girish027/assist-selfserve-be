package com.tfsc.ilabs.selfservice.common.repositories;

import com.tfsc.ilabs.selfservice.common.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ServiceRepository extends JpaRepository<Service, String> {
}
