package com.tfsc.ilabs.selfservice.common.repositories;

import com.tfsc.ilabs.selfservice.common.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by jonathan.paul on 28/05/2020
 */
@RepositoryRestResource
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
