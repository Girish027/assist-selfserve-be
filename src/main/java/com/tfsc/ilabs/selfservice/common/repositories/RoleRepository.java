package com.tfsc.ilabs.selfservice.common.repositories;

import com.tfsc.ilabs.selfservice.common.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * Created by jonathan.paul on 28/05/2020
 */
@RepositoryRestResource
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    Optional<Role> findByStandardRole(String standardRole);
}
