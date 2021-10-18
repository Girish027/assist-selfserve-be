package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * Created by ravi.b on 05-06-2019.
 */
@CrossOrigin
@RepositoryRestResource()
public interface WorkflowTemplateRepository extends JpaRepository<WorkflowTemplate, String> {

    Optional<WorkflowTemplate> findById(String id);
}
