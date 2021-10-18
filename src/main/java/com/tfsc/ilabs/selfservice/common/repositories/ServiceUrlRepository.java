package com.tfsc.ilabs.selfservice.common.repositories;

import com.tfsc.ilabs.selfservice.common.models.Environment;
import com.tfsc.ilabs.selfservice.common.models.ServiceUrls;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ServiceUrlRepository extends JpaRepository<ServiceUrls, Integer> {
    public ServiceUrls findByServiceIdAndEnv(String serviceId, Environment env);
}
