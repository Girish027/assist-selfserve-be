package com.tfsc.ilabs.selfservice.page.dto.response;

import com.tfsc.ilabs.selfservice.page.models.PageTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by ravi.b on 07-06-2019.
 */
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PageTemplateDTO implements Serializable {
    private String id;

    private String title;

    private String description;

    private PageDefinitionDTO definition;

    public PageTemplateDTO(PageTemplate pageTemplate) {
        this.id = pageTemplate.getId();
        this.title = pageTemplate.getTitle();
        this.definition = pageTemplate.getDefinition().toDTO();
        this.description = pageTemplate.getDescription();
    }
}
