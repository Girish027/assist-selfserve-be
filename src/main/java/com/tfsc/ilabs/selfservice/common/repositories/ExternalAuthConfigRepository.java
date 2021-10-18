package com.tfsc.ilabs.selfservice.common.repositories;

import com.tfsc.ilabs.selfservice.common.models.ExternalServiceAuthConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author Sushil.Kumar
 */
@RepositoryRestResource
public interface ExternalAuthConfigRepository extends JpaRepository<ExternalServiceAuthConfig, Integer> {

    @QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    Optional<ExternalServiceAuthConfig> findByNameAndServiceId(String name, String serviceId);
}
