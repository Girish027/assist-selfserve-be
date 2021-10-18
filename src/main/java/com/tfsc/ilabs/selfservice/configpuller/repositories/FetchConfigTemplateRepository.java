package com.tfsc.ilabs.selfservice.configpuller.repositories;

import com.tfsc.ilabs.selfservice.configpuller.model.FetchConfigTemplate;
import com.tfsc.ilabs.selfservice.configpuller.model.FetchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource()
public interface FetchConfigTemplateRepository extends JpaRepository<FetchConfigTemplate, Integer> {
    List<FetchConfigTemplate> findAllByFetchFor(String fetchFor);
    List<FetchConfigTemplate> findByFetchForAndTypeInOrderByExecutionOrderAsc(String fetchFor, List<FetchType> fetchTypes);
}
