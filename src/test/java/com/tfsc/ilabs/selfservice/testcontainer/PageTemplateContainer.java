package com.tfsc.ilabs.selfservice.testcontainer;

import com.fasterxml.jackson.databind.node.TextNode;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.page.models.PageDefinition;
import com.tfsc.ilabs.selfservice.page.models.PageTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PageTemplateContainer {

    public static List<PageTemplate> getPageTemplateList() {
        PageDefinition pageDefinition = new PageDefinition();
        pageDefinition.setFetch(TextNode.valueOf("fetch"));
        pageDefinition.setSchema(TextNode.valueOf("schema"));
        pageDefinition.setUiSchema(TextNode.valueOf("uischema"));
        pageDefinition.setViewUiSchema(TextNode.valueOf("viewUIschema"));

        PageTemplate pageTemplate1 = new PageTemplate();
        pageTemplate1.setTitle("Page1");
        pageTemplate1.setId("page1");
        pageTemplate1.setDefinition(pageDefinition);
        pageTemplate1.setType(DefinitionType.UPDATE);
        PageTemplate pageTemplate2 = new PageTemplate();
        pageTemplate2.setTitle("Page2");
        pageTemplate2.setId("page2");
        pageTemplate2.setDefinition(pageDefinition);
        List<PageTemplate> pageTemplates = new ArrayList<>();
        pageTemplates.add(pageTemplate1);
        pageTemplates.add(pageTemplate2);
        return pageTemplates;
    }

    public static Optional<PageTemplate> getPageTemplateById(String id) {
        return getPageTemplateList().stream()
                .filter(pageTemplate -> pageTemplate.getId().equals(id)).findAny();
    }

}
