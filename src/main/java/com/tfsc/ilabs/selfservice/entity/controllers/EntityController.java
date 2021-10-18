package com.tfsc.ilabs.selfservice.entity.controllers;

import com.tfsc.ilabs.selfservice.common.exception.NoSuchResourceException;
import com.tfsc.ilabs.selfservice.entity.models.EntityInstance;
import com.tfsc.ilabs.selfservice.entity.services.api.EntityService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EntityController {
    @Autowired
    private EntityService entityService;

    @PostMapping("/entity/discard/{entityId}")
    public void discardAnEntity(@PathVariable Integer entityId) {
        Optional<EntityInstance> optionalEntityInstance = entityService.getInstance(entityId);
        if (optionalEntityInstance.isPresent()) {
            entityService.markEntityInstanceStatusDiscarded(optionalEntityInstance.get());
        } else {
            throw new NoSuchResourceException(EntityInstance.class, entityId);
        }
    }
}
