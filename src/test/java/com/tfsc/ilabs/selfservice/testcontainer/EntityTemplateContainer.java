package com.tfsc.ilabs.selfservice.testcontainer;

import com.tfsc.ilabs.selfservice.entity.models.EntityTemplate;

public class EntityTemplateContainer {

    public static EntityTemplate createEntityTemplate(String name, Integer id) {
        EntityTemplate entityTemplate = new EntityTemplate();
        entityTemplate.setName(name);
        entityTemplate.setId(id);
        return entityTemplate;
    }
}
