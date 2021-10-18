package com.tfsc.ilabs.selfservice.workflow.repositories;

import com.tfsc.ilabs.selfservice.workflow.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Integer> {
    List<Menu> findAllByMenuGroupName(String menuGroupName);

}
