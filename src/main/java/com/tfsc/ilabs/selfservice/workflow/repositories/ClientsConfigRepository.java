package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.ClientsConfig;
import com.tfsc.ilabs.selfservice.workflow.models.ConfigNames;
import com.tfsc.ilabs.selfservice.workflow.models.ScopeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientsConfigRepository extends JpaRepository<ClientsConfig, Integer> {
    List<ClientsConfig> findByScopeTypeAndScopeIdInAndConfigName(ScopeType scopeType, List<String> scopeIds, ConfigNames configName);

}