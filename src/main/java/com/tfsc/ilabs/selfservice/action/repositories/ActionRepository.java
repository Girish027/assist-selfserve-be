package com.tfsc.ilabs.selfservice.action.repositories;

import com.tfsc.ilabs.selfservice.action.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ravi.b on 05-06-2019.
 */
@RepositoryRestResource()
public interface ActionRepository extends JpaRepository<Action, Integer> {
}
