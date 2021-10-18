package com.tfsc.ilabs.selfservice.action.dto.response;

import lombok.Data;
import org.springframework.hateoas.core.Relation;

@Data
@Relation(collectionRelation = "actions")
public class ActionResponse {

    private Integer id;

}
