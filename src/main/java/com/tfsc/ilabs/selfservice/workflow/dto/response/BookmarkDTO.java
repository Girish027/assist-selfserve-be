package com.tfsc.ilabs.selfservice.workflow.dto.response;

import com.tfsc.ilabs.selfservice.workflow.models.WorkflowTileType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.tfsc.ilabs.selfservice.workflow.models.*;

/**
 * Created by ravi.b on 17-10-2019.
 */
@Data
@EqualsAndHashCode
public class BookmarkDTO extends WorkflowTemplateDTO {
    private String url;
    private UiSchema uiSchema;

    public BookmarkDTO(String id, String title, String description, String url, UiSchema uiSchema) {
        super(id, title, description, WorkflowTileType.BOOKMARK);
        this.url = url;
        this.uiSchema=uiSchema;
    }
}
