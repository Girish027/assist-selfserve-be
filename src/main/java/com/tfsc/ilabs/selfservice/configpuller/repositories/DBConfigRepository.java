package com.tfsc.ilabs.selfservice.configpuller.repositories;

import com.tfsc.ilabs.selfservice.configpuller.model.Config;
import com.tfsc.ilabs.selfservice.configpuller.model.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.QueryHint;
import java.util.List;

@RepositoryRestResource
public interface DBConfigRepository extends JpaRepository<Config, String> {
    @QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    List<Config> findAll();
    @QueryHints(value = { @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    List<Config> findAllByType(ConfigType configType);
}
