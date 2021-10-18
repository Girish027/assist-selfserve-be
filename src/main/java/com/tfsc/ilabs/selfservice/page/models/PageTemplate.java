package com.tfsc.ilabs.selfservice.page.models;

import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.utils.PageConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
public class PageTemplate extends AuditableEntity {
    @Id
    @NotNull
    @Column(unique = true)
    private String id;

    @NotNull
    private String title;

    @Column(length = 1024)
    private String description;

    @NotNull
    @Column(columnDefinition = "mediumtext")
    @Convert(converter = PageConverter.class)
    private PageDefinition definition;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    private DefinitionType type;

    public PageTemplate() {
        // for jackson purpose onlypage
    }

    public PageTemplate(@NotNull String id, @NotNull String title, String description, PageDefinition definition,
                        DefinitionType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.definition = definition;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PageTemplate that = (PageTemplate) o;
        return Objects.equals(title, that.title) && Objects.equals(description, that.description)
                && Objects.equals(definition, that.definition) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, definition, type);
    }
}
