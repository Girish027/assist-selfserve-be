package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by ravi.b on 10-09-2019.
 */
@RepositoryRestResource()
public interface  BookmarkRepository extends JpaRepository<Bookmark, Integer> {
}
