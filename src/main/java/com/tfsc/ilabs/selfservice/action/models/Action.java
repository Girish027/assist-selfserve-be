package com.tfsc.ilabs.selfservice.action.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfsc.ilabs.selfservice.action.config.ActionConfigConverter;
import com.tfsc.ilabs.selfservice.action.config.ActionDefinitionConverter;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionConfig;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionDefinition;
import com.tfsc.ilabs.selfservice.action.models.definition.ActionRestDefinition;
import com.tfsc.ilabs.selfservice.common.models.AuditableEntity;
import com.tfsc.ilabs.selfservice.common.models.DefinitionType;
import com.tfsc.ilabs.selfservice.common.utils.ConfigConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by ravi.b on 31-05-2019.
 */
@Getter
@Setter
@Entity
public class Action extends AuditableEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(length = 1024)
    private String description;

    @Column(columnDefinition = "json")
    @Convert(converter = ActionDefinitionConverter.class)
    private ActionDefinition definition;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private ActionType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 45)
    @NotNull
    private DefinitionType definitionType;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "action_dependencies", joinColumns = @JoinColumn(name = "action_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "dependent_action_id", referencedColumnName = "id"))
    private Set<Action> actionDependencies = new HashSet<>();

    @Column(columnDefinition = "json")
    @Convert(converter = ConfigConverter.class)
    private JsonNode responseDefinition;

    @Column(columnDefinition = "json")
    @Convert(converter = ActionConfigConverter.class)
    private Map<ActionConfigType, ActionConfig> configs;

    public Action() {
        // for jackson purpose only
    }

    public Action(String description, ActionRestDefinition definition, @NotNull ActionType type,
                  DefinitionType definitionType) {
        this.description = description;
        this.definition = definition;
        this.type = type;
        this.definitionType = definitionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Action action = (Action) o;
        return Objects.equals(id, action.id) && Objects.equals(description, action.description)
                && Objects.equals(definition, action.definition) && type == action.type
                && Objects.equals(actionDependencies, action.actionDependencies)
                && definitionType == action.definitionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, definition, type, actionDependencies, definitionType);
    }

    @Override
    public String toString() {
        return "Action{" + "id=" + id + ", description='" + description + '\'' + ", definition='" + definition + '\''
                + ", type=" + type + ", definitionType='" + definitionType + '}';
    }
}
